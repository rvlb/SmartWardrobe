package com.arara.smartwardrobe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button bScanLabel, bMyWearables, bMyFriends, bLogout;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bScanLabel = (Button) findViewById(R.id.bScanLabel);
        bMyWearables = (Button) findViewById(R.id.bMyWearables);
        bMyFriends = (Button) findViewById(R.id.bMyFriends);
        bLogout= (Button) findViewById(R.id.bLogout);

        bScanLabel.setOnClickListener(this);
        bMyWearables.setOnClickListener(this);
        bMyFriends.setOnClickListener(this);
        bLogout.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
    }

    @Override
    public void onBackPressed() {
        logout();
    }

    private void logout() {
        userLocalStore.clearUserData();
        userLocalStore.setUserLogged(false);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bScanLabel:
                startActivity(new Intent(this, ScanLabelActivity.class));
                break;

            case R.id.bMyWearables:
                Intent intent = new Intent(this, MyWearablesActivity.class);
                intent.putExtra("owner", userLocalStore.getLoggedUser().name);
                startActivity(intent);
                break;

            case R.id.bMyFriends:
                startActivity(new Intent(this, MyFriendsActivity.class));
                break;

            case R.id.bLogout:
                logout();
                break;
        }
    }
}
