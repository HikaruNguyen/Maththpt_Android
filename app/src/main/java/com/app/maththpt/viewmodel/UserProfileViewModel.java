package com.app.maththpt.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.app.maththpt.R;
import com.app.maththpt.database.HistoryDBHelper;

/**
 * Created by FRAMGIA\nguyen.duc.manh on 15/02/2017.
 */

public class UserProfileViewModel extends BaseViewModel {
    @Bindable
    public String userNameProfile;
    @Bindable
    public String emailProfile;

    @Bindable
    public String averagePoint;
    @Bindable
    public String countPoint;
    @Bindable
    public String classification;


    public UserProfileViewModel(Activity activity, String title, String userName, String email) {
        super(activity, title);
        this.userNameProfile = userName;
        this.emailProfile = email;
        setAveragePoint();
    }

    public UserProfileViewModel(String userNameProfile, String emailProfile) {
        this.userNameProfile = userNameProfile;
        this.emailProfile = emailProfile;
        setAveragePoint();
    }

    private void setAveragePoint() {
        HistoryDBHelper.HistoryDatabase historyDatabase = new HistoryDBHelper.HistoryDatabase(activity);
        historyDatabase.open();
        int count = historyDatabase.getCountHistory();
        if (count > 0) {
            float diem = historyDatabase.getAveragePoint();
            averagePoint = String.format("%.1f", diem);
            countPoint = count + "";
            if (diem >= 8) {
                classification = activity.getString(R.string.good);
            } else if (diem >= 6.5) {
                classification = activity.getString(R.string.rather);
            } else if (diem >= 4.5) {
                classification = activity.getString(R.string.medium);
            } else {
                classification = activity.getString(R.string.weak);
            }
        } else {
            classification = activity.getString(R.string.no_data);
            countPoint = activity.getString(R.string.no_data);
            averagePoint = activity.getString(R.string.no_data);

        }
        historyDatabase.close();
    }

}
