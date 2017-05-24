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
import com.app.maththpt.adapter.CategoryAdapter;
import com.app.maththpt.config.MathThptService;
import com.app.maththpt.databinding.FragmentCategoryBinding;
import com.app.maththpt.model.Category;
import com.app.maththpt.modelresult.CategoryResult;
import com.app.maththpt.realm.CategoryModule;
import com.app.maththpt.utils.CLog;
import com.app.maththpt.viewmodel.CategoryViewModel;
import com.app.maththpt.widget.PullToRefreshHeader;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {
    private static final String TAG = CategoryFragment.class.getSimpleName();
    private FragmentCategoryBinding categoryBinding;
    private CategoryViewModel categoryViewModel;
    private Subscription mSubscription;
    private CategoryResult mCategoryResult;
    private Realm realm;
    CategoryAdapter adapter;

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        categoryBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_category, container, false);
        categoryViewModel = new CategoryViewModel();
        categoryBinding.setCategoryViewModel(categoryViewModel);
        View view = categoryBinding.getRoot();
        initUI();
        bindData();
        setRefresh();
        return view;
    }

    private void bindData() {
        Realm.init(getActivity());
        RealmConfiguration settingConfig = new RealmConfiguration.Builder()
                .name("category.realm")
                .modules(Realm.getDefaultModule(), new CategoryModule())
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(MyApplication.with(getActivity()).REALM_VERSION)
                .build();
        realm = Realm.getInstance(settingConfig);

        callAPIGetData();

//        List<Category> list = realm.where(Category.class).findAll();
//        adapter.addAll(list);
//        if (categoryBinding.ptrTests.isRefreshing()) {
//            categoryBinding.ptrTests.refreshComplete();
//        }
//        categoryBinding.rvChuyenDe.setAdapter(adapter);
    }

    MathThptService apiService;

    private void callAPIGetData() {
        adapter = new CategoryAdapter(getActivity(), new ArrayList<>());
        if (adapter != null && adapter.getItemCount() > 0) {
            adapter.clear();
        }
        apiService = MyApplication.with(getActivity()).getMaththptSerivce();
        if (mSubscription != null && !mSubscription.isUnsubscribed()) mSubscription.unsubscribe();
        mSubscription = apiService.getCategory()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CategoryResult>() {
                    @Override
                    public void onCompleted() {
                        if (categoryBinding.ptrTests.isRefreshing()) {
                            categoryBinding.ptrTests.refreshComplete();
                        }

                        if (mCategoryResult != null
                                && mCategoryResult.data != null
                                && mCategoryResult.data.size() > 0) {
                            categoryViewModel.setErrorVisiable(false);
                            for (int i = 0; i < mCategoryResult.data.size(); i++) {
                                boolean isExits = realm
                                        .where(Category.class)
                                        .equalTo("id", mCategoryResult.data.get(i).id)
                                        .count() > 0;
                                if (!isExits) {
                                    realm.beginTransaction();
                                    switch (mCategoryResult.data.get(i).id) {
                                        case 1:
                                            mCategoryResult.data.get(i).icon
                                                    = R.mipmap.icon_khaosat;
                                            break;
                                        case 2:
                                            mCategoryResult.data.get(i).icon
                                                    = R.mipmap.ic_luythua;
                                            break;
                                        case 3:
                                            mCategoryResult.data.get(i).icon
                                                    = R.mipmap.ic_tichphan;
                                            break;
                                        case 4:
                                            mCategoryResult.data.get(i).icon
                                                    = R.mipmap.ic_sophuc;
                                            break;
                                        case 5:
                                            mCategoryResult.data.get(i).icon
                                                    = R.mipmap.ic_khoidadien;
                                            break;
                                        case 6:
                                            mCategoryResult.data.get(i).icon
                                                    = R.mipmap.ic_khoitronxoay;
                                            break;
                                        case 7:
                                            mCategoryResult.data.get(i).icon
                                                    = R.mipmap.ic_oxyz;
                                            break;
                                        default:
                                            mCategoryResult.data.get(i).icon
                                                    = R.mipmap.ic_launcher;
                                            break;
                                    }
                                    realm.copyToRealmOrUpdate(mCategoryResult.data.get(i));
                                    realm.commitTransaction();

                                } else {
                                    Category category = realm
                                            .where(Category.class)
                                            .equalTo("id", mCategoryResult.data.get(i).id)
                                            .findFirst();
                                    realm.beginTransaction();
                                    category.countTotalQuestion
                                            = mCategoryResult.data.get(i).countTotalQuestion;
                                    realm.copyToRealmOrUpdate(category);
                                    realm.commitTransaction();
                                    mCategoryResult.data.get(i).icon = category.icon;
                                    mCategoryResult.data.get(i).countViewQuestion
                                            = category.countViewQuestion;
                                }
                            }

                            categoryBinding.rvChuyenDe.setAdapter(adapter);

                            adapter.addAll(mCategoryResult.data);
                        } else {
                            categoryViewModel.setVisiableError(true);
                            categoryViewModel.setMessageError(getString(R.string.no_data));
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (categoryBinding.ptrTests.isRefreshing()) {
                            categoryBinding.ptrTests.refreshComplete();
                        }

                        List<Category> categoryListCache = realm.where(Category.class).findAll();
                        if (categoryListCache != null && categoryListCache.size() > 0) {
                            categoryBinding.rvChuyenDe.setAdapter(adapter);
                            adapter.addAll(categoryListCache);
                        } else {
                            categoryViewModel.setErrorVisiable(true);
                            categoryViewModel.setMessageError(getString(R.string.error_connect));
                            CLog.d(TAG, "getListTest Error");
                        }
                    }

                    @Override
                    public void onNext(CategoryResult categoryResult) {
                        mCategoryResult = categoryResult;
                    }
                });
    }

    private void setRefresh() {
        PullToRefreshHeader headerView = new PullToRefreshHeader(getContext());

        categoryBinding.ptrTests.setHeaderView(headerView);
        categoryBinding.ptrTests.addPtrUIHandler(headerView);
        categoryBinding.ptrTests.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                if (headerView.getCurrentPosY() < (1.5 * frame.getHeaderHeight())) {
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame,
                            categoryBinding.rvChuyenDe, header);
                }
                return false;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                bindData();
                ((MainActivity) getActivity()).ads();
            }
        });
    }

    private void initUI() {
        categoryBinding.rvChuyenDe.setDivider();
    }

}
