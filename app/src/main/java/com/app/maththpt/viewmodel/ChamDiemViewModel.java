package com.app.maththpt.viewmodel;

import android.app.Activity;
import android.databinding.Bindable;

import com.app.maththpt.R;

/**
 * Created by FRAMGIA\nguyen.duc.manh on 16/02/2017.
 */

public class ChamDiemViewModel extends BaseViewModel {
    @Bindable
    public String yourPoint;

    public ChamDiemViewModel(Activity activity, String title) {
        super(activity, title);
    }

    public void setYourPoint(float point) {
        yourPoint = activity.getString(R.string.yourPoint) + ": " + String.format("%.2f", point);
        notifyChange();
    }
}
