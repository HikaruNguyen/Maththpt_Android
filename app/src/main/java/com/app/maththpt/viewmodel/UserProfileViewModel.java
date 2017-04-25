package com.app.maththpt.viewmodel;

import android.app.Activity;
import android.databinding.Bindable;

import com.app.maththpt.R;
import com.app.maththpt.activity.MyApplication;
import com.app.maththpt.model.Point;
import com.app.maththpt.realm.HistoryModule;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by FRAMGIA\nguyen.duc.manh on 15/02/2017.
 */

public class UserProfileViewModel extends BaseViewModel {
    @Bindable
    public String userNameProfile;
    @Bindable
    public String emailProfile;

    @Bindable
    public String avatarProfile;
    @Bindable
    public String averagePoint;
    @Bindable
    public String countPoint;
    @Bindable
    public String classification;

    private String userID;

    public UserProfileViewModel(Activity activity, String title, String userName, String email,
                                String avatarProfile, String userId) {
        super(activity, title);
        this.userNameProfile = userName;
        this.emailProfile = email;
        this.avatarProfile = avatarProfile;
        this.userID = userId;
        setAveragePoint();
    }

    public UserProfileViewModel(String userNameProfile, String emailProfile) {
        this.userNameProfile = userNameProfile;
        this.emailProfile = emailProfile;
        setAveragePoint();
    }

    private void setAveragePoint() {
        Realm.init(activity);
        RealmConfiguration settingConfig = new RealmConfiguration.Builder()
                .name("history.realm")
                .modules(Realm.getDefaultModule(), new HistoryModule())
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(MyApplication.with(activity).REALM_VERSION)
                .build();

        Realm realm = Realm.getInstance(settingConfig);
        long count = realm.where(Point.class).equalTo("userID", userID).count();
        if (count > 0) {
            double diem = realm.where(Point.class).equalTo("userID", userID).average("point");
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
    }

}
