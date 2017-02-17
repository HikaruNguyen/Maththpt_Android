package com.app.maththpt.viewmodel;

import android.app.Activity;
import android.databinding.Bindable;
import android.view.View;

/**
 * Created by FRAMGIA\nguyen.duc.manh on 17/02/2017.
 */

public class QuestionViewModel extends BaseViewModel {

    @Bindable
    public int visiableNoData;

    @Bindable
    public String messageError;

    public QuestionViewModel(Activity activity, String title) {
        super(activity, title);
        visiableNoData = View.GONE;
    }

    public void setVisiableNoData(boolean isVisable) {
        if (isVisable) {
            visiableNoData = View.VISIBLE;
        } else {
            visiableNoData = View.GONE;
        }
        notifyChange();
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
        notifyChange();
    }
}
