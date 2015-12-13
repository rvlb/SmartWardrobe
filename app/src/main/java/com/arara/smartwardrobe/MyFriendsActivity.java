package com.arara.smartwardrobe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MyFriendsActivity extends AppCompatActivity implements View.OnClickListener {

    Button bAddFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friends);

        bAddFriend = (Button) findViewById(R.id.bAddFriend);

        bAddFriend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bAddFriend:
                startActivity(new Intent(this, AddFriendActivity.class));
                break;
        }
    }
}
