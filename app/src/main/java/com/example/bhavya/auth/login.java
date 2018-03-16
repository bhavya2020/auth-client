package com.example.bhavya.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by bhavya on 16/3/18.
 */

public class login extends AppCompatActivity {
    private static final String ip = "192.168.1.6";
    private static final String port = "5454";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText username = (EditText) findViewById(R.id.username);
                EditText password = (EditText) findViewById(R.id.password);
                final String json = "{\"username\":\"" + username.getText().toString() + "\",\"password\":\"" + password.getText().toString() + "\"}";
                username.setText(" ");
                password.setText(" ");
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL("http://"+ip+":"+port+"/login");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setDoOutput(true);
                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("Content-Type", "application/json");
                            OutputStream os=null;
                            os = conn.getOutputStream();
                            os.write(json.getBytes());
                            os.flush();
                            conn.getResponseCode();
                            os.close();
                            conn.disconnect();
                            startActivity(new Intent(login.this, home.class));
                        }catch (Exception e){
                            //error occured
                        }
                    }
                });
                thread.start();
            }
        });
    }
}
