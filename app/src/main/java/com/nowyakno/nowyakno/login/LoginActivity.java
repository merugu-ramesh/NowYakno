package com.nowyakno.nowyakno.login;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.nowyakno.nowyakno.R;
import com.nowyakno.nowyakno.home.HomeActivity;
import com.nowyakno.nowyakno.home.MainActivity;
import com.nowyakno.nowyakno.preferances.Preferance;
import com.nowyakno.nowyakno.util.Constant;
import com.nowyakno.nowyakno.util.UtilityDialog;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.fabric.sdk.android.Fabric;

/**
 * Created by LENOVO on 24-03-2017.
 */

public class LoginActivity extends Activity implements View.OnClickListener {
    Button btnSubmt;
    EditText userNameEdt, pwdEdt;
    CheckBox checkRemember;
    TextView registerTextView;
    TextView forgotTextView;

    CallbackManager callbackManager;
    LoginButton login;
    Preferance preferance;
    SharedPreferences preferanceInstance;
    private TwitterLoginButton twitterLoginButton;
    Context context;
    public static String TWITTER_CONSUMER_KEY = "KH6IketimscukGtYjguE8idyG";
    public static String TWITTER_SECRET_key = "PVQbv5aHBcAEHq2nAjqpHFkwzWAP0seyU3zxuWDZ1wkvPJjdPo";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_CONSUMER_KEY, TWITTER_SECRET_key);
        Fabric.with(this, new Twitter(authConfig));


        getViews();
        context = LoginActivity.this;
        btnSubmt.setOnClickListener(this);
        checkRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            }
        });
        callbackManager = CallbackManager.Factory.create();
        preferance = new Preferance(this);
        preferanceInstance = preferance.getPreferanceInstance();
        boolean isRemember = preferanceInstance.getBoolean(Constant.IS_REMEMBER, false);
        if (isRemember) {
            checkRemember.setChecked(true);
            String uname = preferanceInstance.getString(Constant.USERNAME, null);
            String pwd = preferanceInstance.getString(Constant.PASSWORD, null);
            userNameEdt.setText(uname);
            pwdEdt.setText(pwd);
        } else {
            userNameEdt.setText("");
            pwdEdt.setText("");
            checkRemember.setChecked(false);
        }
    }

    private void getViews() {
        userNameEdt = (EditText) findViewById(R.id.userNameEdt);
        pwdEdt = (EditText) findViewById(R.id.pwdEdt);
        btnSubmt = (Button) findViewById(R.id.submit_btn);
        registerTextView = (TextView) findViewById(R.id.registerTextView);
        checkRemember = (CheckBox) findViewById(R.id.checkRemember);
        registerTextView.setOnClickListener(this);
        forgotTextView = (TextView) findViewById(R.id.forgotTextView);
        forgotTextView.setOnClickListener(this);

        //Login Button
        callbackManager = CallbackManager.Factory.create();

        login = (LoginButton) findViewById(R.id.login_button);

        login.setReadPermissions("public_profile email");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Facebook CANCEL", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Facebook Error" + error, Toast.LENGTH_LONG).show();
            }
        });
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.nowyakno.nowyakno", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }


        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! ";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                startActivity(new Intent(context, HomeActivity.class));
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
                Toast.makeText(getApplicationContext(), "Exception :: " + exception.getMessage().toString(), Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_btn:
                String username = userNameEdt.getText().toString();
                String password = pwdEdt.getText().toString();
                if (username.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Username  should not be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Password  should not be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if ((username.equals("manoj")) && (password.equals("manoj"))) {
                    SharedPreferences.Editor editor = null;
                    if (checkRemember.isChecked()) {
                        editor = preferance.getEditor(preferanceInstance);
                        editor.putString(Constant.USERNAME, username);           // Saving boolean - true/false
                        editor.putString(Constant.PASSWORD, password);
                        editor.putBoolean(Constant.IS_REMEMBER, true);
                    } else {
                        editor.putBoolean(Constant.IS_REMEMBER, false);
                    }
                    editor.apply();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    new UtilityDialog(context).showAlertDialog("OOPS!!!", "Enter Valid Credentials");
                    return;
                }


                break;
            case R.id.registerTextView:
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
                break;
            case R.id.forgotTextView:
                openForgotPasswordDialog();
                break;
        }
    }

    private void openForgotPasswordDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.forgot_dlg);
        Button forgotBtn = (Button) dialog.findViewById(R.id.btnSubmitForgot);
        final EditText forgotEditText = (EditText) dialog.findViewById(R.id.forgotPwdEdt);
        forgotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailId = forgotEditText.getText().toString();
                Toast.makeText(context, "Forgot password link sent to your emailId.", Toast.LENGTH_LONG).show();
                //TODO NEED TO SEND TO THE SERVER
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the login button.
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }
}
