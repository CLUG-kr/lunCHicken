package com.lunCHicken;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private SignIn googleLogin;
    private LoginSocketHandler loginSocketHandler;
    private static final int RC_SIGN_IN = 9001;
    private MessageHandler messageHandler;

    private EditText editTextId;
    private boolean isLogIn = false;
    private String idToken = null;
    private LoginSocketListener register = new LoginSocketListener() {
        @Override
        public void onMessage(String action, JSONObject data) {
            try {
                int response = Integer.valueOf(data.getString("response"));
                if (response == 200){
                    Log.d("yo","yeah");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        googleLogin = new SignIn(this);
        loginSocketHandler = LoginSocketHandler.getInstance();
        messageHandler = new MessageHandler();
        findViewById(R.id.sign_in_register_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleLogin.signIn();
            }
        });
        findViewById(R.id.register_apply_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLogIn){
                    return;
                }

                if (editTextId.getText().length() < 4){
                    return ;
                }
                loginSocketHandler.registerListener("register", register);
                loginSocketHandler.send(messageHandler.generateRegisterSendData(idToken, editTextId.getText().toString()).toString());
            }
        });
        editTextId = findViewById(R.id.id_text);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN){
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
            } catch (NullPointerException e){}
        }
    }
}
