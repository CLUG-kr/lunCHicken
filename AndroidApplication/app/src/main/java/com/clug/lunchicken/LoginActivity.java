package com.clug.lunchicken;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.clug.lunchicken.googleAuth.Auth;
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
        setContentView(R.layout.activity_login);

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
