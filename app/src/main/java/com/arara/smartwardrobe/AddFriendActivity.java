package com.arara.smartwardrobe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddFriendActivity extends AppCompatActivity implements View.OnClickListener {

    Button bAddFriend;
    EditText etFriendUsername;

    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        bAddFriend = (Button) findViewById(R.id.bAddFriend);
        etFriendUsername = (EditText) findViewById(R.id.etFriendUsername);

        bAddFriend.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bAddFriend:
                String userName = userLocalStore.getLoggedUser().name;
                String friendName = etFriendUsername.getText().toString();
                Friendship newFriendship = new Friendship(userName, friendName);
                addFriendship(newFriendship);
                break;
        }
    }

    private void addFriendship(Friendship friendship) {
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.storeFriendshipDataInBackground(friendship, new Callback() {
            @Override
            public void done(ServerResponse serverResponse) {
                Log.d("serverResponseAddF", serverResponse.response);
                if(serverResponse.response.equals("blank fields")) {
                    AlertMessage.show("Please fill all the fields.", "Ok", AddFriendActivity.this);
                } else if(serverResponse.response.equals("same user")) {
                    AlertMessage.show("Please choose a user that isn't you.", "Ok", AddFriendActivity.this);
                } else if(serverResponse.response.equals("friendship exists")) {
                    AlertMessage.show("Friendship already exists.", "Ok", AddFriendActivity.this);
                } else if(serverResponse.response.equals("invalid user")) {
                    AlertMessage.show("Invalid user.", "Ok", AddFriendActivity.this);
                } else if(serverResponse.response.equals("error")) {
                    AlertMessage.show("An error occurred while trying to connect.", "Ok", AddFriendActivity.this);
                } else if(serverResponse.response.equals("failure")) {
                    AlertMessage.show("An error occurred.", "Ok", AddFriendActivity.this);
                } else if(serverResponse.response.equals("success")) {
                    AlertMessage.show("Friendship successfully started!", "Ok", AddFriendActivity.this);
                    startActivity(new Intent(AddFriendActivity.this, MainActivity.class));
                }
            }
        });
    }
}
