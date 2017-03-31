package com.app.maththpt.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.maththpt.R;
import com.app.maththpt.adapter.CategoryAdapter;
import com.app.maththpt.databinding.FragmentCategoryBinding;
import com.app.maththpt.model.Category;
import com.app.maththpt.realm.CategoryModule;

import java.util.ArrayList;
import java.util.List;

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
        return view;
    }

    private void bindData() {
        CategoryAdapter adapter = new CategoryAdapter(getActivity(), new ArrayList<>());
        Realm.init(getActivity());
        RealmConfiguration settingConfig = new RealmConfiguration.Builder()
                .name("category.realm")
                .modules(Realm.getDefaultModule(), new CategoryModule())
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm realm = Realm.getInstance(settingConfig);
        List<Category> list = realm.where(Category.class).findAll();
        adapter.addAll(list);
        categoryBinding.rvChuyenDe.setAdapter(adapter);
    }

    private void initUI() {
        categoryBinding.rvChuyenDe.setDivider();
    }

}
