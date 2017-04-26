package com.app.maththpt.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.app.maththpt.R;
import com.app.maththpt.config.Configuaration;
import com.app.maththpt.config.MathThptService;
import com.app.maththpt.model.Category;
import com.app.maththpt.model.Point;
import com.app.maththpt.modelresult.BaseResult;
import com.app.maththpt.realm.CategoryModule;
import com.app.maththpt.realm.HistoryModule;
import com.app.maththpt.utils.CLog;
import com.app.maththpt.utils.FacebookUtils;
import com.app.maththpt.utils.Utils;
import com.facebook.login.LoginManager;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.Sort;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();
    MathThptService apiService;
    private Subscription mSubscription;
    private BaseResult mBaseResult;

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
        if (Utils.isNetworkConnected(this)) {
            syncData();
        } else {
            new Thread(new Task()).start();
        }
//        new Thread(new Task()).start();
    }

    Realm realm;
    List<Point> listPoints;

    private void syncData() {
        Realm.init(this);
        RealmConfiguration settingConfig = new RealmConfiguration.Builder()
                .name("history.realm")
                .modules(Realm.getDefaultModule(), new HistoryModule())
                .schemaVersion(MyApplication.with(this).REALM_VERSION)
//                .migration(new HistoryMigration())
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(settingConfig);
        listPoints = realm
                .where(Point.class)
                .equalTo("isSynced", 0)
                .findAllSorted("time", Sort.DESCENDING);
        if (listPoints != null && listPoints.size() > 0) {
//            List<Point> list = new ArrayList<>();
            String param = "";
            if (listPoints.size() > 0) {
                param += listPoints.get(0).getParamSync();
            }
            if (listPoints.size() > 1) {
                for (int i = 1; i < listPoints.size(); i++) {
                    param += ";" + listPoints.get(i).getParamSync();
                }
            }
            CLog.d(TAG, "param: " + param);
//            Gson gson = new Gson();
//            String datajson = gson.toJson(list);
//            CLog.d(TAG, "json object: " + URLEncoder.encode(datajson));
//            CLog.d(TAG, "json object: " + URLDecoder.decode(URLEncoder.encode(datajson)));


            apiService = MyApplication.with(this).getMaththptSerivce();
            if (mSubscription != null && !mSubscription.isUnsubscribed())
                mSubscription.unsubscribe();
            mSubscription = apiService.postSyncHistory(param)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<BaseResult>() {
                        @Override
                        public void onCompleted() {
                            if (mBaseResult.success && mBaseResult.status == 200) {
                                realm.beginTransaction();
                                for (int i = 0; i < listPoints.size(); i++) {
                                    listPoints.get(i).isSynced = 1;
                                }

                                for (int i = 0; i < listPoints.size(); i++) {
                                    realm.copyToRealmOrUpdate(listPoints.get(i));
                                }
                                realm.commitTransaction();
                            }
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onNext(BaseResult baseResult) {
                            if (baseResult != null) {
                                mBaseResult = baseResult;
                            }
                        }
                    });
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            new Thread(new Task()).start();
        }

    }

    private void initCategory() {
        Realm.init(this);
        RealmConfiguration settingConfig = new RealmConfiguration.Builder()
                .name("category.realm")
                .modules(Realm.getDefaultModule(), new CategoryModule())
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(MyApplication.with(this).REALM_VERSION)
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null && !mSubscription.isUnsubscribed()) mSubscription.unsubscribe();
        mSubscription = null;
    }
}