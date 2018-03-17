package com.example.bhavya.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by bhavya on 16/3/18.
 */

public class login extends AppCompatActivity {
    private static final String ip = "192.168.1.6";
    private static final String port = "5454";

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final StringBuffer response = new StringBuffer();
                EditText username = (EditText) findViewById(R.id.username);
                EditText password = (EditText) findViewById(R.id.password);
                TextView message = (TextView) findViewById(R.id.message);
                final String json = "{\"username\":\"" + username.getText().toString() + "\",\"password\":\"" + password.getText().toString() + "\"}";
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL("http://" + ip + ":" + port + "/login");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setDoOutput(true);
                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("Content-Type", "application/json");
                            OutputStream os = null;
                            os = conn.getOutputStream();
                            os.write(json.getBytes());
                            os.flush();
                            os.close();
                            conn.getResponseCode();
                            BufferedReader in = new BufferedReader(
                                    new InputStreamReader(conn.getInputStream()));
                            String inputLine;
                            while ((inputLine = in.readLine()) != null) {
                                response.append(inputLine);
                            }
                            in.close();
                            conn.disconnect();
                        } catch (Exception e) {
                            //error occured
                            Log.d("error", e.toString());
                        }
                    }
                });
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (response.toString().equals("okay")) {
                    startActivity(new Intent(login.this, home.class));
                    finish();
                }else
                if (response.toString().equals("notFound")) {
                    username.setText(" ");
                    password.setText(" ");
                    message.setVisibility(View.VISIBLE);
                }
                else
                if (response.toString().equals("incorrect")) {
                    username.setText(" ");
                    password.setText(" ");
                    message.setText("password is incorrect...");
                    message.setVisibility(View.VISIBLE);

                }else
                {
                    username.setText(" ");
                    password.setText(" ");
                    message.setText("username or password is incorrect...");
                    message.setVisibility(View.VISIBLE);

                }
            }
        });
    }
}
