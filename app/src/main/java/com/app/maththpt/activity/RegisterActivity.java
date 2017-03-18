package com.app.maththpt.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.widget.Toast;

import com.app.maththpt.R;
import com.app.maththpt.config.MathThptService;
import com.app.maththpt.databinding.ActivityRegisterBinding;
import com.app.maththpt.modelresult.BaseResult;
import com.app.maththpt.viewmodel.RegisterViewModel;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegisterActivity extends BaseActivity {
    private ActivityRegisterBinding registerBinding;
    private RegisterViewModel registerViewModel;
    private Subscription mSubscription;
    private BaseResult mBaseResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        registerViewModel = new RegisterViewModel(this, getString(R.string.register));
        setSupportActionBar(registerBinding.toolbar);
        setBackButtonToolbar();
        registerBinding.setRegisterViewModel(registerViewModel);
        event();
    }

    private void event() {
        registerBinding.btnRegister.setOnClickListener(v -> {
            if (registerViewModel.user.username == null
                    || registerViewModel.user.username.isEmpty()) {
                Toast.makeText(
                        RegisterActivity.this,
                        getString(R.string.empty_username),
                        Toast.LENGTH_SHORT).show();
            } else if (registerViewModel.user.fullname == null
                    || registerViewModel.user.fullname.isEmpty()) {
                Toast.makeText(
                        RegisterActivity.this,
                        getString(R.string.empty_fullname),
                        Toast.LENGTH_SHORT).show();
            } else if (registerViewModel.user.password == null
                    || registerViewModel.user.password.isEmpty()) {
                Toast.makeText(
                        RegisterActivity.this,
                        getString(R.string.empty_password),
                        Toast.LENGTH_SHORT).show();
            } else if (registerViewModel.repass == null
                    || registerViewModel.repass.isEmpty()) {
                Toast.makeText(RegisterActivity.this,
                        getString(R.string.empty_repassword),
                        Toast.LENGTH_SHORT).show();
            } else if (registerViewModel.user.email == null
                    || registerViewModel.user.email.isEmpty()) {
                Toast.makeText(RegisterActivity.this,
                        getString(R.string.empty_email),
                        Toast.LENGTH_SHORT).show();
            } else if (!registerViewModel.user.password.equals(registerViewModel.repass)) {
                Toast.makeText(RegisterActivity.this,
                        getString(R.string.password_invalid),
                        Toast.LENGTH_SHORT).show();
            } else {
                registerAPI();
            }
        });

    }

    private void registerAPI() {
        MathThptService apiService = MyApplication.with(this).getMaththptSerivce();
        if (mSubscription != null && !mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();
        mSubscription = apiService.postRegister(registerViewModel.user.username,
                registerViewModel.user.password, registerViewModel.user.fullname,
                registerViewModel.user.email)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResult>() {
                    @Override
                    public void onCompleted() {
                        if (mBaseResult.success && mBaseResult.status == 200) {
                            Intent intent = new Intent();
                            intent.putExtra("username", registerViewModel.user.username);
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            Toast.makeText(
                                    RegisterActivity.this,
                                    mBaseResult.message, Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(
                                RegisterActivity.this,
                                getString(R.string.error_connect), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(BaseResult baseResult) {
                        if (baseResult != null) {
                            mBaseResult = baseResult;
                        }
                    }
                });
    }
}
