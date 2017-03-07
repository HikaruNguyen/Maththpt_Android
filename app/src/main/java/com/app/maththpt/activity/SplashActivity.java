package com.app.maththpt.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.app.maththpt.R;
import com.app.maththpt.config.Configuaration;
import com.app.maththpt.model.Category;
import com.app.maththpt.realm.CategoryModule;
import com.app.maththpt.utils.FacebookUtils;
import com.facebook.login.LoginManager;

import io.realm.Realm;
import io.realm.RealmConfiguration;

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
        initCategory();
        new Thread(new Task()).start();
    }

    private void initCategory() {
        Realm.init(this);
        RealmConfiguration settingConfig = new RealmConfiguration.Builder()
                .name("category.realm")
                .modules(Realm.getDefaultModule(), new CategoryModule())
                .build();
        Realm realm = Realm.getInstance(settingConfig);
        realm.beginTransaction();
        Category category = new Category(1, "Khảo sát đồ thị hàm số", R.mipmap.icon_khaosat);
        realm.copyToRealmOrUpdate(category);
        category = new Category(2, "Lũy thừa, logarit", R.mipmap.ic_luythua);
        realm.copyToRealmOrUpdate(category);
        category = new Category(3, "Nguyên hàm, tích phân", R.mipmap.ic_tichphan);
        realm.copyToRealmOrUpdate(category);
        category = new Category(4, "Số phức", R.mipmap.ic_sophuc);
        realm.copyToRealmOrUpdate(category);
        category = new Category(5, "Thể tích khối đa diện", R.mipmap.ic_khoidadien);
        realm.copyToRealmOrUpdate(category);
        category = new Category(6, "Thể tích khối tròn xoay", R.mipmap.ic_khoitronxoay);
        realm.copyToRealmOrUpdate(category);
        category = new Category(7, "Phương pháp tọa độ trong không gian Oxyz", R.mipmap.ic_oxyz);
        realm.copyToRealmOrUpdate(category);
        realm.commitTransaction();
    }

    private class Task implements Runnable {
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
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}
