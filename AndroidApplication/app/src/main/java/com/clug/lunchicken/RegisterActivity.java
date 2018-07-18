package com.clug.lunchicken;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.clug.lunchicken.googleAuth.Auth;
import com.clug.lunchicken.loginSocket.LoginConstant;
import com.clug.lunchicken.loginSocket.LoginSocketHandler;
import com.clug.lunchicken.loginSocket.LoginSocketListener;
import com.clug.lunchicken.loginSocket.MessageHandler;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private Auth googleAuth = Auth.getInstance();
    private LoginSocketHandler loginSocketHandler;

    private EditText editTextId;
    private boolean isLogIn = false;
    private String idToken = null;
    private LoginSocketListener register = new LoginSocketListener() {
        @Override
        public void onMessage(String action, JSONObject data) {
            try {
                int response = Integer.valueOf(data.getString("response"));
                Message msg = null;
                switch (response){
                    case LoginConstant.ERR_NOT_VERIFIED:
                        msg = showMsgHandler.obtainMessage();
                        msg.obj = "인증되지 않은 구글 계정입니다.";
                        showMsgHandler.sendMessage(msg);
                        break;
                    case LoginConstant.ERR_WRONG_TOKEN:
                        msg = showMsgHandler.obtainMessage();
                        msg.obj = "잘 못 된 토큰입니다.";
                        showMsgHandler.sendMessage(msg);
                        break;
                    case LoginConstant.ERR_UNKNOW:
                        msg = showMsgHandler.obtainMessage();
                        msg.obj = "알 수 없는 오류입니다.";
                        showMsgHandler.sendMessage(msg);
                        break;
                    case LoginConstant.ERR_DUPLICATED_ID:
                        msg = showMsgHandler.obtainMessage();
                        msg.obj = "이미 존재하는 아이디입니다.";
                        showMsgHandler.sendMessage(msg);
                        break;
                    case LoginConstant.ERR_DUPLICATED_EMAIl:
                        msg = showMsgHandler.obtainMessage();
                        msg.obj = "이미 존재하는 이메일입니다.";
                        showMsgHandler.sendMessage(msg);
                        break;
                    case LoginConstant.SUCCESS_REGISTER:
                    msg = goToLoginHandler.obtainMessage();
                    goToLoginHandler.sendMessage(msg);
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private Handler showMsgHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            showToast(msg.obj.toString());
        }
    };

    private Handler goToLoginHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            finish();
        }
    };

    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loginSocketHandler = LoginSocketHandler.getInstance();
        final Activity activity = this;
        findViewById(R.id.sign_in_register_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleAuth.signIn(activity);
            }
        });
        findViewById(R.id.register_apply_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLogIn){
                    showToast("구글 로그인 인증이 되지 않았습니다.");
                    return;
                }

                if (editTextId.getText().length() < 4){
                    showToast("아이디는 4글자 이상만 가능합니다.");
                    return ;
                }
                loginSocketHandler.registerListener("register", register);
                loginSocketHandler.send(MessageHandler.generateRegisterSendData(idToken, editTextId.getText().toString()).toString());
            }
        });
        editTextId = findViewById(R.id.id_text);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Auth.RC_SIGN_IN){
            // The Task returned from this call is always completed, no need to attach
            // a listener.5
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount account = null;
            try {
                account = task.getResult(ApiException.class);
            } catch (ApiException e) {
                e.printStackTrace();
            }
            try{
                idToken = account.getIdToken();
                isLogIn = true;
                showToast("구글 인증 성공");
            } catch (NullPointerException e){}
        }
    }
}
