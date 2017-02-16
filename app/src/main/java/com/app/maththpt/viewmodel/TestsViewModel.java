package com.app.maththpt.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

/**
 * Created by FRAMGIA\nguyen.duc.manh on 16/02/2017.
 */

public class TestsViewModel extends BaseObservable {

    @Bindable
    public int errorVisiable;

    public TestsViewModel() {
        errorVisiable = View.GONE;
    }

    public void setErrorVisiable(boolean isVisable) {
        if (isVisable) {
            errorVisiable = View.VISIBLE;
        } else {
            errorVisiable = View.GONE;
        }
        notifyChange();
    }
}
