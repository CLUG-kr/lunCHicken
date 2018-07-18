package com.lunCHicken;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

public class StartActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "StartActivity";
    private SignIn signin;
    private static final int RC_SIGN_IN = 9001;

    private MessageHandler messageHandler;

    private Handler handler;

    // socket 통신 관련
    private LoginSocketHandler loginSocketHandler = LoginSocketHandler.getInstance();
    private LoginSocketListener loginListener = new LoginSocketListener() {
        @Override
        public void onMessage(String action, JSONObject data) {
            try {
                Log.d(TAG, data.getString("response"));
                int response = Integer.valueOf(data.getString("response"));
                switch (response){
                    case 200:
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            loginSocketHandler.unregisterListener(action, this);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        signin = new SignIn(this);
        messageHandler = new MessageHandler();
        loginSocketHandler.connectSocket();
        findViewById(R.id.sign_in_login_button).setOnClickListener(this);
        findViewById(R.id.register_button).setOnClickListener(this);

    }


    private void getMessage()
    {
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.d(TAG, "msg: " + msg);
            }
        };
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_login_button:
                signin.signIn();
                getMessage();
                break;
            case R.id.register_button:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
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
                String idToken = account.getIdToken();
                if (idToken != null){
                    loginSocketHandler.registerListener("login", loginListener);
                    loginSocketHandler.send(messageHandler.generateLoginSendData(idToken).toString());
                }
            } catch (NullPointerException e){}



            /*try {
                //LoginSocket.send(messageHander.generateLoginSendData(idToken));l
                Log.d(TAG, "sended data");
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }
    }
    // [END onActivityResult]

}
