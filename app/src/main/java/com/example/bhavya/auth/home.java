package com.example.bhavya.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.R.attr.port;

/**
 * Created by bhavya on 16/3/18.
 */

public class home extends AppCompatActivity {
    private static final String ip = "192.168.1.6";
    private static final String port = "5454";
    private final String USER_AGENT = "Mozilla/5.0";
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);

            Button logout= (Button) findViewById(R.id.logout);
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final StringBuffer response = new StringBuffer();
                    Thread thread=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                URL url = new URL("http://" + ip + ":" + port + "/logout");
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setRequestMethod("GET");
                                conn.getResponseCode();
                                BufferedReader in = new BufferedReader(
                                        new InputStreamReader(conn.getInputStream()));
                                String inputLine;
                                while ((inputLine = in.readLine()) != null) {
                                    response.append(inputLine);
                                }
                                in.close();
                                conn.disconnect();
                            }catch (Exception e)
                            {
                                Log.d("error",e.toString());
                            }
                        }
                    });
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (response.toString().equals("back")) {
                        startActivity(new Intent(home.this,MainActivity.class));
                        finish();
                    }

                }
            });
        }
}
