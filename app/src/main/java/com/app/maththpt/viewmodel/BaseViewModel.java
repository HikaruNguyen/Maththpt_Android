package com.app.maththpt.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

/**
 * Created by manhi on 11/2/2017.
 */

public class BaseViewModel extends BaseObservable {
    @Bindable
    public String title;
    public Activity activity;
    @Bindable
    public int visiableError;

    @Bindable
    public String messageError;

    BaseViewModel() {
        visiableError = View.GONE;
    }

    BaseViewModel(Activity activity, String title) {
        this.activity = activity;
        this.title = title;
        visiableError = View.GONE;
    }
    BaseViewModel(Activity activity) {
        this.activity = activity;
        visiableError = View.GONE;
    }
    public void setTitle(String title) {
        this.title = title;
        notifyChange();
    }

    public void setVisiableError(boolean isVisable) {
        if (isVisable) {
            visiableError = View.VISIBLE;
        } else {
            visiableError = View.GONE;
        }
        notifyChange();
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
        notifyChange();
    }
}
