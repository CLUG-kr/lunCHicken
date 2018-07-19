package com.clug.lunchicken;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.clug.lunchicken.data.LoginData;
import com.clug.lunchicken.googleAuth.Auth;
import com.clug.lunchicken.loginSocket.LoginConstant;
import com.clug.lunchicken.loginSocket.LoginSocketHandler;
import com.clug.lunchicken.loginSocket.LoginSocketListener;
import com.clug.lunchicken.loginSocket.MessageHandler;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "LoginActivity";

    // socket 통신 관련
    private LoginSocketHandler loginSocketHandler = LoginSocketHandler.getInstance();
    private LoginSocketListener loginListener = new LoginSocketListener() {
        @Override
        public void onMessage(String action, JSONObject data) {
            //if (!data.has("response")) return;
            Log.d(TAG, "Hi" + data.toString());
            try {
                int response = Integer.valueOf(data.getString("response"));
                Message msg = null;
                switch (response){
                    case LoginConstant.ERR_WRONG_TOKEN:
                        msg = showToastHandler.obtainMessage();
                        msg.obj = "잘못된 토큰 오류. 관리자에게 문의하세요.";
                        showToastHandler.sendMessage(msg);
                        break;
                    case LoginConstant.ERR_UNKNOW:
                        msg = showToastHandler.obtainMessage();
                        msg.obj = "알 수 없는 오류. 관리자에게 문의하세요.";
                        showToastHandler.sendMessage(msg);
                        break;
                    case LoginConstant.ERR_NOT_VERIFIED:
                        msg = showToastHandler.obtainMessage();
                        msg.obj = "이메일 인증되지 않은 계정입니다.";
                        showToastHandler.sendMessage(msg);
                        break;
                    case LoginConstant.ERR_WRONG_INFORMATION:
                        msg = showToastHandler.obtainMessage();
                        msg.obj = "잘못된 정보입니다.";
                        showToastHandler.sendMessage(msg);
                        break;
                    case LoginConstant.ERR_ALREADY_LOGIN:
                        msg = showToastHandler.obtainMessage();
                        msg.obj = "이미 로그인 되어 있습니다.";
                        showToastHandler.sendMessage(msg);
                    case LoginConstant.SUCCESS_LOGIN:

                        LoginData.getInstance().setToken(data.getString("account_login_token"));
                        Log.d(TAG, "My 토큰은 " + data.getString("account_login_token"));

                        msg = goToRobbyHandler.obtainMessage();
                        goToRobbyHandler.sendMessage(msg);
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            loginSocketHandler.unregisterListener(action, this);
        }
    };

    private Context mContext;
    private Handler showToastHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, msg.obj.toString());
            Toast.makeText(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    private Handler goToRobbyHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
            Intent goToRobbyIntent = new Intent(LoginActivity.this, RobbyActivity.class);
            startActivity(goToRobbyIntent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = getApplicationContext();
        loginSocketHandler.connectSocket();
        findViewById(R.id.register_button).setOnClickListener(this);
        // [START customize_button]
        // Set the dimensions of the sign-in button.
        findViewById(R.id.sign_in_login_button).setOnClickListener(this);
        SignInButton signInButton = findViewById(R.id.sign_in_login_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setColorScheme(SignInButton.COLOR_LIGHT);
        // [END customize_button]

    }

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == Auth.RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    // [END onActivityResult]

    // [START handleSignInResult]
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            try{
                String idToken = account.getIdToken();
                if (idToken != null){
                    loginSocketHandler.registerListener("login", loginListener);
                    loginSocketHandler.send(MessageHandler.generateLoginSendData(idToken).toString());
                }
            } catch (NullPointerException e){}
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }
    // [END handleSignInResult]

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_login_button:
                Auth.getInstance().signIn(this);
                break;
            case R.id.register_button:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

}
