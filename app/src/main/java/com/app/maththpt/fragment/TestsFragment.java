package com.app.maththpt.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.maththpt.R;
import com.app.maththpt.activity.MainActivity;
import com.app.maththpt.activity.MyApplication;
import com.app.maththpt.adapter.TestsAdapter;
import com.app.maththpt.config.MathThptService;
import com.app.maththpt.databinding.FragmentTestsBinding;
import com.app.maththpt.model.Tests;
import com.app.maththpt.modelresult.TestsResult;
import com.app.maththpt.realm.TestsModule;
import com.app.maththpt.utils.CLog;
import com.app.maththpt.viewmodel.TestsViewModel;
import com.app.maththpt.widget.PullToRefreshHeader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.Sort;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.app.maththpt.model.Tests.TestsComparator.ID_SORT;
import static com.app.maththpt.model.Tests.TestsComparator.SEEN_SORT;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestsFragment extends Fragment {
    private static final String TAG = TestsFragment.class.getSimpleName();
    private FragmentTestsBinding testsBinding;
    private TestsViewModel testsViewModel;
    private TestsAdapter adapter;
    private Subscription mSubscription;
    private TestsResult mTestResult;
    private Realm realm;

    public TestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        testsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_tests, container, false);
        testsViewModel = new TestsViewModel();
        testsBinding.setTestsViewModel(testsViewModel);
        View view = testsBinding.getRoot();
        testsBinding.rvTests.setDivider();
        bindData();
        setRefresh();
        return view;
    }

    private void setRefresh() {
        PullToRefreshHeader headerView = new PullToRefreshHeader(getContext());

        testsBinding.ptrTests.setHeaderView(headerView);
        testsBinding.ptrTests.addPtrUIHandler(headerView);
        testsBinding.ptrTests.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                if (headerView.getCurrentPosY() < (1.5 * frame.getHeaderHeight())) {
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame,
                            testsBinding.rvTests, header);
                }
                return false;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                callAPIGetData();
                ((MainActivity) getActivity()).ads();
            }
        });
    }

    private void bindData() {
        Realm.init(getActivity());
        RealmConfiguration settingConfig = new RealmConfiguration.Builder()
                .name("tests.realm")
                .modules(Realm.getDefaultModule(), new TestsModule())
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(MyApplication.with(getActivity()).REALM_VERSION)
                .build();

        realm = Realm.getInstance(settingConfig);

        callAPIGetData();
    }

    MathThptService apiService;

    private void callAPIGetData() {
        adapter = new TestsAdapter(getActivity(), new ArrayList<>());
        if (adapter != null && adapter.getItemCount() > 0) {
            adapter.clear();
        }
        apiService = MyApplication.with(getActivity()).getMaththptSerivce();
        if (mSubscription != null && !mSubscription.isUnsubscribed()) mSubscription.unsubscribe();
        mSubscription = apiService.getTests()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TestsResult>() {
                    @Override
                    public void onCompleted() {
                        if (testsBinding.ptrTests.isRefreshing()) {
                            testsBinding.ptrTests.refreshComplete();
                        }

                        if (mTestResult != null
                                && mTestResult.data != null
                                && mTestResult.data.size() > 0) {
                            testsViewModel.setErrorVisiable(false);
//                            Collections.reverse(mTestResult.data);
                            for (int i = 0; i < mTestResult.data.size(); i++) {
                                boolean isExits = realm
                                        .where(Tests.class)
                                        .equalTo("id", mTestResult.data.get(i).id)
                                        .count() > 0;
                                if (!isExits) {
                                    realm.beginTransaction();
                                    realm.copyToRealmOrUpdate(mTestResult.data.get(i));
                                    realm.commitTransaction();
                                    mTestResult.data.get(i).isNew = true;
                                    mTestResult.data.get(i).isCompleted = false;
                                } else {
                                    mTestResult.data.get(i).isNew = false;
                                    Tests tests = realm
                                            .where(Tests.class)
                                            .equalTo("id", mTestResult.data.get(i).id).findFirst();
                                    mTestResult.data.get(i).isSeen = tests.isSeen;
                                    mTestResult.data.get(i).isCompleted = tests.isCompleted;

                                }
                            }
                            Collections.sort(mTestResult.data, Tests.TestsComparator.decending(
                                    Tests.TestsComparator.getComparator(SEEN_SORT, ID_SORT)));
                            testsBinding.rvTests.setAdapter(adapter);
                            adapter.addAll(mTestResult.data);
                        } else {
                            testsViewModel.setVisiableError(true);
                            testsViewModel.setMessageError(getString(R.string.no_data));
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (testsBinding.ptrTests.isRefreshing()) {
                            testsBinding.ptrTests.refreshComplete();
                        }

                        List<Tests> testsListCache = realm
                                .where(Tests.class)
                                .findAllSorted("isSeen", Sort.ASCENDING, "id", Sort.DESCENDING);
                        if (testsListCache != null && testsListCache.size() > 0) {
                            testsBinding.rvTests.setAdapter(adapter);
                            adapter.addAll(testsListCache);
                        } else {
                            testsViewModel.setErrorVisiable(true);
                            testsViewModel.setMessageError(getString(R.string.error_connect));
                            CLog.d(TAG, "getListTest Error");
                        }
                    }

                    @Override
                    public void onNext(TestsResult testsResult) {
                        mTestResult = testsResult;
//                        Toast.makeText(getActivity(), testsResult.success + "", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        mSubscription = null;
        if (realm != null && !realm.isClosed()) {
            realm.close();
        }
    }
}
