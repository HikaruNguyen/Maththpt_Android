package com.app.maththpt.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.maththpt.R;
import com.app.maththpt.activity.MyApplication;
import com.app.maththpt.adapter.TestsAdapter;
import com.app.maththpt.config.MathThptService;
import com.app.maththpt.databinding.FragmentTestsBinding;
import com.app.maththpt.model.Tests;
import com.app.maththpt.modelresult.TestsResult;
import com.app.maththpt.utils.CLog;
import com.app.maththpt.viewmodel.TestsViewModel;
import com.app.maththpt.widget.PullToRefreshHeader;

import java.util.ArrayList;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
                bindData();
            }
        });
    }

    private void bindData() {
        MathThptService apiService = MyApplication.with(getActivity()).getMaththptSerivce();
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
                        testsViewModel.setErrorVisiable(false);
                        adapter = new TestsAdapter(getActivity(), new ArrayList<Tests>());
                        if (mTestResult != null
                                && mTestResult.data != null
                                && mTestResult.data.size() > 0)
                            adapter.addAll(mTestResult.data);
                        testsBinding.rvTests.setAdapter(adapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (testsBinding.ptrTests.isRefreshing()) {
                            testsBinding.ptrTests.refreshComplete();
                        }
                        testsViewModel.setErrorVisiable(true);
                        CLog.d(TAG, "getListTest Error");
//                        Toast.makeText(getActivity(), getString(R.string.error_connect), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(TestsResult testsResult) {
                        mTestResult = testsResult;
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
    }
}
