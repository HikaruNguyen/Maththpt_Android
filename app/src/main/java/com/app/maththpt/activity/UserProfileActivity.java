package com.app.maththpt.activity;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.app.maththpt.R;
import com.app.maththpt.config.Configuaration;
import com.app.maththpt.databinding.ActivityUserProfileBinding;
import com.app.maththpt.viewmodel.UserProfileViewModel;

public class UserProfileActivity extends BaseActivity {
    private ActivityUserProfileBinding userProfileBinding;
    private UserProfileViewModel userProfileViewModel;
    private SharedPreferences sharedPreferences;
    private String userName;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userProfileBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile);
        sharedPreferences = getSharedPreferences(Configuaration.Pref, MODE_PRIVATE);
        userName = sharedPreferences.getString(Configuaration.KEY_NAME, "");
        email = sharedPreferences.getString(Configuaration.KEY_EMAIL, "");
        setSupportActionBar(userProfileBinding.toolbar);
        setBackButtonToolbar();
        userProfileViewModel = new UserProfileViewModel(this, getString(R.string.user_profile), userName, email);

        userProfileBinding.setUserProfileViewModel(userProfileViewModel);

//        setBackButtonToolbar();
    }
}
