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
import com.app.maththpt.databinding.FragmentCategoryBinding;
import com.app.maththpt.model.Category;
import com.app.maththpt.realm.CategoryModule;
import com.app.maththpt.widget.PullToRefreshHeader;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    private FragmentCategoryBinding categoryBinding;

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        categoryBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_category, container, false);
        View view = categoryBinding.getRoot();
        initUI();
        bindData();
        setRefresh();
        return view;
    }

    private void bindData() {
        CategoryAdapter adapter = new CategoryAdapter(getActivity(), new ArrayList<>());
        Realm.init(getActivity());
        RealmConfiguration settingConfig = new RealmConfiguration.Builder()
                .name("category.realm")
                .modules(Realm.getDefaultModule(), new CategoryModule())
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(MyApplication.with(getActivity()).REALM_VERSION)
                .build();
        Realm realm = Realm.getInstance(settingConfig);
        List<Category> list = realm.where(Category.class).findAll();
        adapter.addAll(list);
        if (categoryBinding.ptrTests.isRefreshing()) {
            categoryBinding.ptrTests.refreshComplete();
        }
        categoryBinding.rvChuyenDe.setAdapter(adapter);
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
