package com.lunCHicken;

import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SignIn {

    private Activity StartActivity;

    private static final String TAG = "SignIn";
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;

    private Socket socket;
    private PrintWriter socket_out;
    private BufferedReader socket_in;

    public SignIn(Activity StartActivity) {
        this.StartActivity = StartActivity;
    }

    // [START signIn]
    protected void signIn() {

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(StartActivity.getString(R.string.server_client_id))
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(StartActivity, gso);
        // [END build_client]

        // [START on_start_sign_in]
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(StartActivity);
//        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleSignInClient)
        if (account != null) {
            mGoogleSignInClient.signOut(); //이미 로그인 되어있을 때 처리해야하는데 일단 걍 로그아웃 시켜버리자
        }
        // [END on_start_sign_in]

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        StartActivity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

}