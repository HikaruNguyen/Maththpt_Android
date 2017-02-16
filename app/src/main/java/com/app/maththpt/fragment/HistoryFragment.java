package com.app.maththpt.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.maththpt.R;
import com.app.maththpt.databinding.FragmentHistoryBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding historyBinding;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        historyBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false);
        View view = historyBinding.getRoot();
        initUI();
        return view;
    }

    private void initUI() {
        historyBinding.rvHistory.setDivider();
    }

}
