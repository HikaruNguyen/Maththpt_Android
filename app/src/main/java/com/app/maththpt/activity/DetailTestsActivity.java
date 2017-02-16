package com.app.maththpt.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.app.maththpt.R;
import com.app.maththpt.config.MathThptService;
import com.app.maththpt.databinding.ActivityDetailTestsBinding;
import com.app.maththpt.modelresult.DetailTestsResult;
import com.app.maththpt.utils.CLog;
import com.app.maththpt.viewmodel.DetailTestsViewModel;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DetailTestsActivity extends BaseActivity {
    private static final String TAG = DetailTestsActivity.class.getSimpleName();
    private ActivityDetailTestsBinding detailTestsBinding;
    private DetailTestsViewModel detailTestsViewModel;
    private DetailTestsResult mDetailTestsResult;
    private String testID;
    private Subscription mSubscription;
    private MathThptService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailTestsBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail_tests);
        getData();
        bindData();
    }

    private void bindData() {
        apiService = MyApplication.with(this).getMaththptSerivce();
        if (mSubscription != null && !mSubscription.isUnsubscribed()) mSubscription.unsubscribe();
        mSubscription = apiService.getContent(2, testID, 2)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DetailTestsResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        CLog.d(TAG, "getListTest Error");
//                        Toast.makeText(getActivity(), getString(R.string.error_connect), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(DetailTestsResult detailTestsResult) {
                        mDetailTestsResult = detailTestsResult;
                    }
                });
    }

    private void getData() {
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        testID = intent.getStringExtra("testID");
        detailTestsViewModel = new DetailTestsViewModel(this, title);
        detailTestsBinding.setDetailTestsViewModel(detailTestsViewModel);
        setSupportActionBar(detailTestsBinding.toolbar);
        setBackButtonToolbar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        mSubscription = null;
    }
}
