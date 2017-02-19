package com.app.maththpt.viewmodel;

import android.app.Activity;
import android.databinding.Bindable;
import android.view.View;

/**
 * Created by FRAMGIA\nguyen.duc.manh on 17/02/2017.
 */

public class QuestionViewModel extends BaseViewModel {
    @Bindable
    public int visiablePre;
    @Bindable
    public int visiableNext;

    private int postion;
    private int size = 0;

    public QuestionViewModel(Activity activity, String title) {
        super(activity, title);
        visiableError = View.GONE;
        visiablePre = View.GONE;
        visiableNext = View.GONE;
    }

    public int getPostion() {
        return postion;
    }

    public void setPostion(int postion) {
        this.postion = postion;
        setVisiable();
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setVisiable() {
        if (size == 0 || size == 1) {
            return;
        }
        if (getPostion() > 0) {
            visiablePre = View.VISIBLE;
        } else {
            visiablePre = View.GONE;
        }
        if (getPostion() < size - 1) {
            visiableNext = View.VISIBLE;
        } else {
            visiableNext = View.GONE;
        }
        notifyChange();
    }
}

