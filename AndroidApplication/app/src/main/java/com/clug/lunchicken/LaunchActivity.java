package com.clug.lunchicken;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.clug.lunchicken.data.LoginData;
import com.clug.lunchicken.loginSocket.LoginSocketHandler;
import com.clug.lunchicken.loginSocket.LoginSocketOpenListener;

public class LaunchActivity extends AppCompatActivity {

    private LoginSocketHandler loginSocketHandler;
    private LoginData loginData;

    private TextView txtLoadingDesc;

    private LoginSocketOpenListener openListener = new LoginSocketOpenListener() {
        @Override
        public void onOpen() {
            try {
                Thread.sleep(500);
                Message msg = changeTxtHandler.obtainMessage();
                msg.obj = "로그인 서버 연결 완료!";
                changeTxtHandler.sendMessage(msg);
                Thread.sleep(500);
                msg = changeTxtHandler.obtainMessage();
                msg.obj = "로그인 서버와 인사하는 중";
                changeTxtHandler.sendMessage(msg);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message msg = goToLoginHandler.obtainMessage();
            goToLoginHandler.sendMessage(msg);
        }
    };

    private Handler changeTxtHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            txtLoadingDesc.setText(msg.obj.toString());
        }
    };

    private Handler goToLoginHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent goToLoginIntent = new Intent(LaunchActivity.this, LoginActivity.class) ;
            startActivity(goToLoginIntent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        sigletoneLoad();

        txtLoadingDesc = findViewById(R.id.launch_loading_desc);
        loginSocketHandler.registerOpenListener(openListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loginSocketHandler.connectSocket();
    }

    private void sigletoneLoad(){
        loginSocketHandler = LoginSocketHandler.getInstance();
        loginData = LoginData.getInstance();
    }

}
