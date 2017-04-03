package com.app.maththpt.activity;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.app.maththpt.R;
import com.app.maththpt.config.MathThptService;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.messaging.FirebaseMessaging;

import rx.Scheduler;

/**
 * Created by manhi on 4/1/2017.
 */

public class MyApplication extends Application {
    private static final String TAG = MyApplication.class.getSimpleName();
    private MathThptService mathThptService;
    private Scheduler defaultSubscribeScheduler;

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        Log.d(TAG, "Subscribed to news topic");
        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_ad_unit_id));
    }

    public MathThptService getMaththptSerivce() {
        if (mathThptService == null) {
            mathThptService = MathThptService.Factory.create(this);
        }
        return mathThptService;
    }

    public static MyApplication with(Context context) {
        return (MyApplication) context.getApplicationContext();
    }

}
