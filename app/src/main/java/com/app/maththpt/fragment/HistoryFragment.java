package com.app.maththpt.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.maththpt.R;
import com.app.maththpt.adapter.HistoryAdapter;
import com.app.maththpt.config.Configuaration;
import com.app.maththpt.databinding.FragmentHistoryBinding;
import com.app.maththpt.model.Point;
import com.app.maththpt.realm.HistoryModule;
import com.app.maththpt.viewmodel.HistoryViewModel;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding historyBinding;
    private HistoryViewModel historyViewModel;
    private HistoryAdapter historyAdapter;
    private String userID;

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
        SharedPreferences sharedPreferences = getActivity()
                .getSharedPreferences(Configuaration.Pref, Context.MODE_PRIVATE);
        userID = sharedPreferences.getString(Configuaration.KEY_ID, "");
        if (!userID.isEmpty()) {
            Realm.init(getActivity());
            RealmConfiguration settingConfig = new RealmConfiguration.Builder()
                    .name("history.realm")
                    .modules(Realm.getDefaultModule(), new HistoryModule())
                    .build();

            Realm realm = Realm.getInstance(settingConfig);
            RealmResults<Point> listPoints = realm
                    .where(Point.class)
                    .equalTo("userID", userID)
                    .findAllSorted("time", Sort.DESCENDING);
            historyAdapter.addAll(listPoints);
            historyBinding.rvHistory.setAdapter(historyAdapter);
//        historyDatabase.close();
            if (historyAdapter.getItemCount() > 0) {
                historyViewModel.setVisiableError(false);
            } else {
                historyViewModel.setVisiableError(true);
            }
        } else {
            historyViewModel.setVisiableError(true);
        }


    }

    private void initUI() {
        historyBinding.rvHistory.setDivider();
        historyAdapter = new HistoryAdapter(getActivity(), new ArrayList<>());
        historyBinding.rvHistory.setAdapter(historyAdapter);
    }

}
