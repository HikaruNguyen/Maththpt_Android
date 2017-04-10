package com.app.maththpt.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.widget.Toast;

import com.app.maththpt.R;
import com.app.maththpt.config.Configuaration;
import com.app.maththpt.config.MathThptService;
import com.app.maththpt.databinding.ActivityLoginBinding;
import com.app.maththpt.viewmodel.LoginViewModel;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.util.Arrays;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends BaseActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int CODE_REGISTER = 11;
    private CallbackManager callbackManager;
    private SharedPreferences.Editor editor;
    private ActivityLoginBinding loginBinding;
    private LoginViewModel loginViewModel;
    private Subscription mSubscription;
    private com.app.maththpt.modelresult.LoginResult mLoginResult;
    MathThptService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginViewModel = new LoginViewModel(this, getString(R.string.login));
        setSupportActionBar(loginBinding.toolbar);
        loginBinding.setLoginViewModel(loginViewModel);
        setBackButtonToolbar();
        bindData();
        event();
    }

    @SuppressLint("CommitPrefEdits")
    private void bindData() {
        callbackManager = CallbackManager.Factory.create();
        SharedPreferences sharedPreferences = getSharedPreferences(
                Configuaration.Pref, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                (object, response) -> {
                                    if (object != null) {
                                        registerFb(object);
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override

                    public void onCancel() {
                        runOnUiThread(() -> Toast.makeText(
                                LoginActivity.this, "Login Cancel", Toast.LENGTH_LONG).show());

                    }

                    @Override
                    public void onError(final FacebookException error) {
                        runOnUiThread(() -> Toast.makeText(
                                LoginActivity.this, error.getMessage(), Toast.LENGTH_LONG).show());

                    }
                });
    }
    private void registerFb(JSONObject object) {
        String email = object.optString("email");
        String fbid = object.optString("id");
        String name = object.optString("name");
        apiService = MyApplication.with(this).getMaththptSerivce();
        if (mSubscription != null && !mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();
        mSubscription = apiService.postRegisterFb(name, email, fbid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<com.app.maththpt.modelresult.LoginResult>() {
                    @Override
                    public void onCompleted() {
                        if (mLoginResult.success && mLoginResult.status == 200) {
                            editor.putString(Configuaration.KEY_ID, mLoginResult.data.id);
                            editor.putString(Configuaration.KEY_NAME, mLoginResult.data.username);
                            editor.putString(Configuaration.KEY_EMAIL, mLoginResult.data.email);
                            editor.putString(Configuaration.KEY_FBID, mLoginResult.data.fbId);
                            editor.putString(Configuaration.KEY_TOKEN, mLoginResult.data.token);
                            editor.commit();
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(
                                    LoginActivity.this,
                                    mLoginResult.message, Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(
                                LoginActivity.this,
                                getString(R.string.error_connect), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(com.app.maththpt.modelresult.LoginResult loginResult) {
                        if (loginResult != null) {
                            mLoginResult = loginResult;
                        }
                    }
                });
    }
    private void event() {
        loginBinding.btnLoginFB.setOnClickListener(
                view -> LoginManager.getInstance().
                        logInWithReadPermissions(
                                LoginActivity.this,
                                Arrays.asList("public_profile", "user_friends", "email")));
        loginBinding.btnLogin.setOnClickListener(v -> {
            if (loginViewModel.username == null || loginViewModel.username.isEmpty()) {
                Toast.makeText(
                        LoginActivity.this,
                        getString(R.string.empty_username),
                        Toast.LENGTH_SHORT).show();
            } else if (loginViewModel.password == null || loginViewModel.password.isEmpty()) {
                Toast.makeText(
                        LoginActivity.this,
                        getString(R.string.empty_password),
                        Toast.LENGTH_SHORT).show();
            } else {
                loginAPI();
            }

        });
        loginBinding.tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivityForResult(intent, CODE_REGISTER);
        });
    }

    private void loginAPI() {
        apiService = MyApplication.with(this).getMaththptSerivce();
        if (mSubscription != null && !mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();
        mSubscription = apiService.postLogin(loginViewModel.username, loginViewModel.password)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<com.app.maththpt.modelresult.LoginResult>() {
                    @Override
                    public void onCompleted() {
                        if (mLoginResult.success && mLoginResult.status == 200) {
                            editor.putString(Configuaration.KEY_ID, mLoginResult.data.id);
                            editor.putString(Configuaration.KEY_NAME, mLoginResult.data.fullname);
                            editor.putString(Configuaration.KEY_EMAIL, mLoginResult.data.email);
                            editor.putString(Configuaration.KEY_TOKEN, mLoginResult.data.token);
                            editor.commit();
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(
                                    LoginActivity.this,
                                    mLoginResult.message, Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(
                                LoginActivity.this,
                                getString(R.string.error_connect), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(com.app.maththpt.modelresult.LoginResult loginResult) {
                        if (loginResult != null) {
                            mLoginResult = loginResult;
                        }
                    }
                });
    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_REGISTER) {
            if(resultCode==RESULT_OK){
                loginViewModel.username = data.getStringExtra("username");
                loginViewModel.notifyChange();
            }

        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null && !mSubscription.isUnsubscribed()) mSubscription.unsubscribe();
        mSubscription = null;
    }

}
