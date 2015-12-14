package com.arara.smartwardrobe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button bLogin;
    EditText etUsername, etPassword;
    TextView tvRegister;

    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.bLogin);
        tvRegister = (TextView) findViewById(R.id.tvRegister);

        bLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bLogin:
                User user = new User(etUsername.getText().toString(), etPassword.getText().toString());
                authenticate(user);
                break;

            case R.id.tvRegister:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

    private void authenticate(User user) {
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.fetchUserDataInBackground(user, new Callback() {
            @Override
            public void done(ServerResponse serverResponse) {
                if(serverResponse.response.equals("error")) {
                    Misc.showAlertMsg("An error occurred while trying to connect.", "Ok", LoginActivity.this);
                } else if(serverResponse.response.equals("blank fields")) {
                    Misc.showAlertMsg("Please fill all the fields.", "Ok", LoginActivity.this);
                } else if(serverResponse.response.equals("invalid user")) {
                    Misc.showAlertMsg("Invalid user.", "Ok", LoginActivity.this);
                } else if(serverResponse.response.equals("wrong password")) {
                    Misc.showAlertMsg("Wrong password.", "Ok", LoginActivity.this);
                } else if(serverResponse.response.equals("success") && serverResponse.returnedUser != null) {
                    logUser(serverResponse.returnedUser);
                }
            }
        });
    }

    private void logUser(User user) {
        userLocalStore.storeUserData(user);
        userLocalStore.setUserLogged(true);
        startActivity(new Intent(this, MainActivity.class));
    }
}