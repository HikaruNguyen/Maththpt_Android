package com.app.maththpt.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.widget.Toast;

import com.app.maththpt.R;
import com.app.maththpt.config.Configuaration;
import com.app.maththpt.databinding.ActivityLoginBinding;
import com.app.maththpt.viewmodel.LoginViewModel;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

public class LoginActivity extends BaseActivity {
    private static String TAG = LoginActivity.class.getSimpleName();
    private CallbackManager callbackManager;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ActivityLoginBinding loginBinding;
    private LoginViewModel loginViewModel;

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

    private void bindData() {
        callbackManager = CallbackManager.Factory.create();
        sharedPreferences = getSharedPreferences(Configuaration.Pref, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        (object, response) -> {
                            if (object != null) {
                                String email = object.optString("email");
                                String name = object.optString("name");
                                String fbid = object.optString("id");
                                editor.putString(Configuaration.KEY_NAME, name);
                                editor.putString(Configuaration.KEY_EMAIL, email);
                                editor.commit();
                                setResult(RESULT_OK);
                                finish();
                            } else {

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Login Cancel", Toast.LENGTH_LONG).show());

            }

            @Override
            public void onError(final FacebookException error) {
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_LONG).show());

            }
        });
    }

    private void event() {
        loginBinding.btnLoginFB.setOnClickListener(view -> LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends", "email")));
    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
