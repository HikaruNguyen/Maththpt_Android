package com.app.maththpt.viewmodel;

import android.app.Activity;
import android.databinding.Bindable;
import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by FRAMGIA\nguyen.duc.manh on 15/02/2017.
 */

public class LoginViewModel extends BaseViewModel {
    @Bindable
    public String username;

    @Bindable
    public String password;

    public LoginViewModel(Activity activity, String title) {
        super(activity, title);
        username = "";
        password = "";
    }

    public TextWatcher watcherUserName = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            username = s.toString();
            notifyChange();
        }
    };
    public TextWatcher watcherPassword = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            password = s.toString();
            notifyChange();
        }
    };
}
