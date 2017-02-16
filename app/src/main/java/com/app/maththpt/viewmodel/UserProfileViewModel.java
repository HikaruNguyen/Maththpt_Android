package com.app.maththpt.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by FRAMGIA\nguyen.duc.manh on 15/02/2017.
 */

public class UserProfileViewModel extends BaseViewModel {
    @Bindable
    public String userNameProfile;
    @Bindable
    public String emailProfile;

    public UserProfileViewModel(Activity activity, String title, String userName, String email) {
        super(activity, title);
        this.userNameProfile = userName;
        this.emailProfile = email;
    }

    public UserProfileViewModel(String userNameProfile, String emailProfile) {
        this.userNameProfile = userNameProfile;
        this.emailProfile = emailProfile;
    }
}
