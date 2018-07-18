package com.clug.lunchicken;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.clug.lunchicken.data.LoginData;
import com.clug.lunchicken.loginSocket.LoginSocketHandler;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
    }

    private void sigletoneLoad(){
        LoginSocketHandler.getInstance();
        LoginData.getInstance();
    }

}
