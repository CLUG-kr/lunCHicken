package com.example.seolhee.imagetest;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seolhee.imagetest.gettetheredip.ClientScanResult;
import com.example.seolhee.imagetest.gettetheredip.WifiApManager;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ImageTestActivity extends AppCompatActivity {

    private static final String TAG = "ImageTestActivity";

    private WifiApManager wifiApManager;
    private WriteBinarytoFile fw;

    private Handler handler;
    TextView logView;
    ImageView imageView;
    private ImageSocketHandler imageSocketHandler = ImageSocketHandler.getInstance();
    private ImageSocketListener imageSocketListener = new ImageSocketListener() {
        @Override
        public void onMessage(String input) {

            if(input != null) {
                Log.d("onMessage", "Received data: " + input);
                imageSocketHandler.send("id token\n");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_test);

        logView = (TextView) findViewById(R.id.log_view);
        logView.append("LOG\n");
        Log.d(TAG, "start activity");

        //tethering test
        wifiApManager = new WifiApManager(this);
        new ConnectRpiTask().execute();

        //home test
//
//        imageSocketHandler.connectSocket("192.168.0.11", 19191);
//        imageSocketHandler.setCurrentActivity(this);
//        imageSocketHandler.registerListener("image", imageSocketListener);

        //fw = new WriteBinarytoFile();



    }

    // 테더링 연결된 장치 IP 받아오는 작업을 스레드로 처리하고 결과를 String으로 IP 반환하는 작업
    private class ConnectRpiTask extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String rpiIP = null;
            ArrayList<ClientScanResult> clients = wifiApManager.getClientList(false);
            int check = 0;
            for (ClientScanResult clientScanResult : clients) {
                check++;
                rpiIP = clientScanResult.getIpAddr();
            }
            if (check > 1) {
                Log.w(TAG, "connected device is more than 1");
            }
            return rpiIP;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d(TAG, "tethered device IP: "+ result);
            logView.append("tethered device IP: " + result + "\n");

            handleSocket(result);
        }
    }
    private void handleSocket(String ip) {
        imageSocketHandler.connectSocket(ip, 19191);
        imageSocketHandler.setCurrentActivity(this);
        imageSocketHandler.registerListener("image", imageSocketListener);
    }
}
