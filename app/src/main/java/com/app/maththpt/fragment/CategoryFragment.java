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

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    private CategoryAdapter adapter;
    private List<Category> list;
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
        adapter = new CategoryAdapter(getActivity(), new ArrayList<>());
        list = new ArrayList<>();
        Category category = new Category(1, "Khảo sát đồ thị hàm số", R.mipmap.icon_khaosat);
        list.add(category);
        category = new Category(2, "Lũy thừa, logarit", R.mipmap.ic_luythua);
        list.add(category);
        category = new Category(3, "Nguyên hàm, tích phân", R.mipmap.ic_tichphan);
        list.add(category);
        category = new Category(4, "Số phức", R.mipmap.ic_sophuc);
        list.add(category);
        category = new Category(5, "Thể tích khối đa diện", R.mipmap.ic_khoidadien);
        list.add(category);
        category = new Category(6, "Thể tích khối tròn xoay", R.mipmap.ic_khoitronxoay);
        list.add(category);
        category = new Category(7, "Phương pháp tọa độ trong không gian Oxyz", R.mipmap.ic_oxyz);
        list.add(category);
        adapter.addAll(list);
        categoryBinding.rvChuyenDe.setAdapter(adapter);
    }

    private void initUI() {
        categoryBinding.rvChuyenDe.setDivider();
    }

}
