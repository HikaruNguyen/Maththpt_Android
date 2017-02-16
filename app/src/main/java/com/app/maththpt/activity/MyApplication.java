package com.app.maththpt.activity;

import android.app.Application;
import android.content.Context;

import com.app.maththpt.config.MathThptService;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created by manhi on 4/1/2017.
 */

public class MyApplication extends Application {
    private MathThptService mathThptService;
    private Scheduler defaultSubscribeScheduler;

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    public MathThptService getMaththptSerivce() {
        if (mathThptService == null) {
            mathThptService = MathThptService.Factory.create(this);
        }
        return mathThptService;
    }

    public Scheduler defaultSubscribeScheduler() {
        if (defaultSubscribeScheduler == null) {
            defaultSubscribeScheduler = Schedulers.io();
        }
        return defaultSubscribeScheduler;
    }
    public static MyApplication with(Context context) {
        return (MyApplication) context.getApplicationContext();
    }

}
