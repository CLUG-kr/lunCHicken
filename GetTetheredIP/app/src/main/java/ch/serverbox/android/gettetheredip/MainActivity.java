package ch.serverbox.android.gettetheredip;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import  ch.serverbox.android.gettetheredip.ClientScanResult;
import  ch.serverbox.android.gettetheredip.WifiApManager;


//import com.whitebyte.hotspotclients.R;
public class MainActivity extends Activity {
    TextView textView1;
    WifiApManager wifiApManager;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView1 = (TextView) findViewById(R.id.textView1);
        wifiApManager = new WifiApManager(this);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                scan();
            }
        });
        t.start();
    }

    private void scan() {
        ArrayList<ClientScanResult> clients = wifiApManager.getClientList(false);

        textView1.append("Clients: \n");
        int check = 0;
        for (ClientScanResult clientScanResult : clients) {
            check++;
            textView1.append("####################\n");
            textView1.append("IpAddr: " + clientScanResult.getIpAddr() + "\n");
            textView1.append("Device: " + clientScanResult.getDevice() + "\n");
            textView1.append("HWAddr: " + clientScanResult.getHWAddr() + "\n");
            textView1.append("isReachable: " + clientScanResult.isReachable() + "\n");
        }
        Log.d("CHECK", "check" + Integer.toString(check));
    }
}
