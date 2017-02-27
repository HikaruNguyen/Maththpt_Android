package com.app.maththpt.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.app.maththpt.R;
import com.app.maththpt.config.Configuaration;
import com.app.maththpt.utils.FacebookUtils;
import com.facebook.login.LoginManager;

import bolts.Task;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences sharedPreferences = getSharedPreferences(
                Configuaration.Pref, MODE_PRIVATE);
        if (FacebookUtils.isExpires()) {
            LoginManager.getInstance().logOut();
            if (sharedPreferences.getString(Configuaration.KEY_TOKEN, "").isEmpty()) {
                sharedPreferences.edit().clear().apply();
            }

        }
        new Thread(new Task()).start();
    }

    class Task implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            goToMain();
        }

        private void goToMain() {
            Intent intent = new Intent(SplashActivity.this, DemoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}
