package com.app.maththpt.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.app.maththpt.R;
import com.app.maththpt.databinding.ActivityAnswerDetailBinding;
import com.app.maththpt.utils.AnswerDetailUtils;
import com.app.maththpt.viewmodel.AnswerDetailViewModel;

public class AnswerDetailActivity extends BaseActivity {
    private ActivityAnswerDetailBinding answerDetailBinding;
    private AnswerDetailViewModel answerDetailViewModel;
    String answerDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        answerDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_answer_detail);
        answerDetailViewModel = new AnswerDetailViewModel(this, getString(R.string.dapAnChiTiet));
        setSupportActionBar(answerDetailBinding.toolbar);
        answerDetailBinding.setAnswerDetailViewModel(answerDetailViewModel);
        getData();
        initUI();
        bindData();
    }

    private void initUI() {
        setBackButtonToolbar();
    }

    private void bindData() {
        answerDetailBinding.webView.setProgress_wheel(answerDetailBinding.progressWheel);
        if (answerDetail != null) {
            AnswerDetailUtils answerDetailUtils = new AnswerDetailUtils();
            answerDetailBinding.webView.loadDataWithBaseURL(
                    "file:///android_asset/",
                    answerDetailUtils.htmlContain(answerDetail),
                    "text/html", "UTF-8", null);
            answerDetailViewModel.setVisiableError(false);
        } else {
            answerDetailViewModel.setVisiableError(true);
            answerDetailViewModel.setMessageError(getString(R.string.no_data));
        }

    }

    private void getData() {
        Intent intent = getIntent();
        answerDetail = intent.getStringExtra("answerDetail");
    }


}
