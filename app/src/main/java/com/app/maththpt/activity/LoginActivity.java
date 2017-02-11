package com.app.maththpt.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.maththpt.R;
import com.app.maththpt.config.Configuaration;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends BaseActivity {
    private Button btnLoginFB;
    private CallbackManager callbackManager;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();
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
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(final JSONObject object,
                                                    GraphResponse response) {
                                if (object != null) {
                                    String email = object.optString("email");
                                    String name = object.optString("name");
                                    String fbid = object.optString("id");
//                                    String avatar = Utils.addressAvatarFB(fbid);
                                    editor.putString(Configuaration.KEY_IDFB, fbid);
                                    editor.putString(Configuaration.KEY_NAME, name);
//                                    editor.putString(Configuaration.KEY_AVATAR, avatar);
                                    editor.putString(Configuaration.KEY_EMAIL, email);
                                    editor.commit();
                                    setResult(RESULT_OK);
                                    finish();
                                } else {

                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onError(final FacebookException error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

    private void event() {
        btnLoginFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends", "email"));
            }
        });
    }

    private void initUI() {
        setBackButtonToolbar();
        setTitleToolbar(getString(R.string.login));
        btnLoginFB = (Button) findViewById(R.id.btnLoginFB);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
