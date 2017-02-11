package com.app.maththpt.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by manhi on 11/2/2017.
 */

public class NavHeaderMainViewModel extends BaseObservable {

    @Bindable
    public String getUserName;

    @Bindable
    public String getEmail;

    @Bindable
    public String getUserAvatar;

    public NavHeaderMainViewModel() {

    }

    public void setUserName(String userName){
        this.getUserName = userName;
    }
}
