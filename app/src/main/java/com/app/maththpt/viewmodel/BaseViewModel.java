package com.app.maththpt.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by manhi on 11/2/2017.
 */

public class BaseViewModel extends BaseObservable {
    @Bindable
    public String title;
    public Activity activity;

    public BaseViewModel() {

    }

    public BaseViewModel(Activity activity, String title) {
        this.activity = activity;
        this.title = title;

    }

    public void setTitle(String title) {
        this.title = title;
        notifyChange();
    }

}
