package com.app.maththpt.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.maththpt.R;
import com.app.maththpt.adapter.HistoryAdapter;
import com.app.maththpt.database.HistoryDBHelper;
import com.app.maththpt.databinding.FragmentHistoryBinding;
import com.app.maththpt.viewmodel.HistoryViewModel;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding historyBinding;
    private HistoryViewModel historyViewModel;
    private HistoryAdapter historyAdapter;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        historyBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_history, container, false);
        historyViewModel = new HistoryViewModel();
        historyBinding.setHistoryViewModel(historyViewModel);
        View view = historyBinding.getRoot();
        initUI();
        bindData();
        return view;
    }

    private void bindData() {
        HistoryDBHelper.HistoryDatabase historyDatabase =
                new HistoryDBHelper.HistoryDatabase(getActivity());
        historyDatabase.open();
        historyAdapter.addAll(historyDatabase.getAll());
        historyBinding.rvHistory.setAdapter(historyAdapter);
        historyDatabase.close();
        if (historyAdapter.getItemCount() > 0) {
            historyViewModel.setVisiableError(false);
        }else {
            historyViewModel.setVisiableError(true);
        }

    }

    private void initUI() {
        historyBinding.rvHistory.setDivider();
        historyAdapter = new HistoryAdapter(getActivity(), new ArrayList<>());
        historyBinding.rvHistory.setAdapter(historyAdapter);
    }

}
