package com.example.zeave.crud;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText username, password;

    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (EditText) findViewById(R.id.etUsername);
        password = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);
    }

    public void OnLogin(View view) {
        String username1 = username.getText().toString();
        String password1 = password.getText().toString();
        String type = "login";

        BackgroundWorker bgk = new BackgroundWorker(this);
        bgk.execute(type, username1, password1);
    }

    public void clear(){
        username.setText("");
        password.setText("");
    }
}
