package com.example.talkingtoy;

import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class MainActivity extends AppCompatActivity {
    private static final int PORT = 8080;

    private MyHTTPD server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView tv = (TextView)findViewById(R.id.txtIp);
//        try {
//            tv.setText(InetAddress.getLocalHost().getHostAddress());
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
//        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
//        tv.setText(ip);
        tv.setText("http://"+Utils.getIPAddress(true)+":"+PORT+"/");

        String page;
        try {
            page = IOUtils.toString(getResources().openRawResource(R.raw.page1));
        } catch (IOException e) {
            e.printStackTrace();
            page = ""+e;
        }

        server = new MyHTTPD(page);
        try {
            server.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (server != null)
            server.stop();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyHTTPD extends NanoHTTPD {
        String page;
        public MyHTTPD(String page) {
            super(PORT);
            this.page = page;
        }

        @Override public Response serve(IHTTPSession session) {
            Method method = session.getMethod();
            String uri = session.getUri();
            System.out.println(method + " '" + uri + "' ");

            String msg = page;
            msg = msg.replace("#URI#", uri);
            Map<String, String> parms = session.getParms();
            if (parms.get("username") != null) {
                msg = msg.replace("#NAME#", parms.get("username"));
            }

            return new NanoHTTPD.Response(msg);
        }
    }
}
