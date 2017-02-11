package com.app.maththpt.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

/**
 * Created by manhi on 11/2/2017.
 */

public class MainViewModel extends BaseObservable {
    private Activity activity;

    public MainViewModel(Activity activity) {
        this.activity = activity;
    }

}
