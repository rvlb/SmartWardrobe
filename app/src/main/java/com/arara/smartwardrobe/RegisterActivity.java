package com.arara.smartwardrobe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    Button bRegister;
    EditText etUsername, etPassword, etPasswordRep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPasswordRep = (EditText) findViewById(R.id.etPasswordRep);
        bRegister = (Button) findViewById(R.id.bRegister);

        bRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bRegister:
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String passwordRep = etPasswordRep.getText().toString();
                User registeredUser = new User(username,password,passwordRep);
                registerUser(registeredUser);
                break;
        }
    }

    private void registerUser(User user) {
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.storeUserDataInBackground(user, new Callback() {
            @Override
            public void done(String serverResponse) {
                Log.d("serverResponseReg", serverResponse);
                if(serverResponse.equals("blank fields")) {
                    Misc.showAlertMsg("Please fill all the fields.", "Ok", RegisterActivity.this);
                } else if(serverResponse.equals("user exists")) {
                    Misc.showAlertMsg("User already exists.", "Ok", RegisterActivity.this);
                } else if(serverResponse.equals("failure")) {
                    Misc.showAlertMsg("An error occurred.", "Ok", RegisterActivity.this);
                } else if(serverResponse.equals("different passwords")) {
                    Misc.showAlertMsg("Passwords don't match.", "Ok", RegisterActivity.this);
                } else if(serverResponse.equals("error")) {
                    Misc.showAlertMsg("An error occurred while trying to connect.", "Ok", RegisterActivity.this);
                    finish();
                } else if(serverResponse.equals("success")) {
                    Misc.showAlertMsg("User successfully registered!", "Ok", RegisterActivity.this);
                    finish();
                }
            }
        });
    }
}