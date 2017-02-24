package com.app.maththpt.viewmodel;

import android.app.Activity;
import android.databinding.Bindable;
import android.text.Editable;
import android.text.TextWatcher;

import com.app.maththpt.model.User;

/**
 * Created by FRAMGIA\nguyen.duc.manh on 21/02/2017.
 */

public class RegisterViewModel extends BaseViewModel {
    @Bindable
    public User user;

    @Bindable
    public String repass;

    public RegisterViewModel(Activity activity, String title) {
        super(activity, title);
        user = new User();
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
            user.username = s.toString();
            notifyChange();
        }
    };

    public TextWatcher watcherFullName = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            user.fullname = s.toString();
            notifyChange();
        }
    };

    public TextWatcher watcherEmail = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            user.email = s.toString();
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
            user.password = s.toString();
            notifyChange();
        }
    };
    public TextWatcher watcherRepassword = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            repass = s.toString();
            notifyChange();
        }
    };
}
