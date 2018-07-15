package com.lunCHicken;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LobbyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        Intent accountIDIntent = new Intent(this.getIntent());
        String account_id = accountIDIntent.getStringExtra("account_id");
        TextView textView=(TextView)findViewById(R.id.textview);
        textView.setText(account_id);
    }
}
