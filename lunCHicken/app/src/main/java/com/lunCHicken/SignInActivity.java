package com.lunCHicken;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.io.PrintWriter;

import io.socket.client.Socket;

/**
 * Activity to demonstrate basic retrieval of the Google user's ID, email address, and basic
 * profile.
 */
public class SignInActivity extends AppCompatActivity implements
        View.OnClickListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleSignInClient mGoogleSignInClient;
    private TextView mStatusTextView;

    private Socket mSocket;

    //test
    private java.net.Socket socket;
    PrintWriter socket_out;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Views
        mStatusTextView = findViewById(R.id.status);

        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [END build_client]

        // [START customize_button]
        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setColorScheme(SignInButton.COLOR_LIGHT);
        // [END customize_button]
    }

    @Override
    public void onStart() {
        super.onStart();

        // [START on_start_sign_in]
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleSignInClient)
        updateUI(account);
        // [END on_start_sign_in]
    }

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
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
            final String idToken = account.getIdToken();
/*
            Thread worker = new Thread() {
                public void run() {
                    String testStr ="{'action':'register','data':{'account_id':'(accountId)','account_email':'(accountEmail)'}}";
                    JSONObject data = null;
                    final JSONObject testJson;
                    try {
                        data = new JSONObject();
                        data.put("account_idtoken", idToken);
                        testJson = new JSONObject(testStr);
                    } catch(JSONException e) {
                        e.printStackTrace();
                    }
                    try{
                        socket = new java.net.Socket("owlsogul.iptime.org", 17777);
                        socket_out = new PrintWriter(socket.getOutputStream(), true);
                        socket_out.println(data);
                        socket_out.flush();
                        Log.d("send", "hihi");
                    }catch(IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            worker.start();
*/
            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }
    // [END handleSignInResult]

    // [START signIn]
    protected void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

    // [START signOut]
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]

    //계정 삭제
    // [START revokeAccess]
    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]

    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) { // hide sign-in button & start next activity
            mStatusTextView.setText(getString(R.string.signed_in_fmt, account.getDisplayName()));

            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else { // display sign-in button
            mStatusTextView.setText(R.string.signed_out);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
            case R.id.disconnect_button:
                revokeAccess();
                break;
        }
    }

    //test
    protected void onStop() {
        super.onStop();
        try{
            socket.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
 }
