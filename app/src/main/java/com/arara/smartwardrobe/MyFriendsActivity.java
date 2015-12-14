package com.arara.smartwardrobe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyFriendsActivity extends AppCompatActivity implements View.OnClickListener {

    Button bAddFriend;
    UserLocalStore userLocalStore;

    List<String> friendsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friends);

        bAddFriend = (Button) findViewById(R.id.bAddFriend);

        bAddFriend.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);

        loadFriendsList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bAddFriend:
                startActivity(new Intent(this, AddFriendActivity.class));
                break;
        }
    }

    private void loadFriendsList() {
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.fetchFriendshipDataInBackground(userLocalStore.getLoggedUser(), new Callback() {
            @Override
            public void done(ServerResponse serverResponse) {
                Log.d("serverResponseMyFr", serverResponse.response);
                if(serverResponse.response.equals("error")) {
                    Misc.showAlertMsg("An error occurred while trying to connect.", "Ok", MyFriendsActivity.this);
                    startActivity(new Intent(MyFriendsActivity.this, MainActivity.class));
                } else if(serverResponse.response.equals("no friends")) {
                    Misc.showAlertMsg("No friend found.", "Ok", MyFriendsActivity.this);
                } else {
                    buildFriendsList(serverResponse.response);
                    //Friend list loaded, now put it in the ListView
                }
            }
        });
    }

    private void buildFriendsList(String friendshipsData) {

        friendsList = new ArrayList<>();

        Log.d("friendshipsData", friendshipsData);

        List<String> friendshipStrings = Arrays.asList(friendshipsData.split("\\$"));

        Log.d("friendshipsStrings", friendshipStrings.toString());

        for (String friendship: friendshipStrings) {

            List<String> friends = Arrays.asList(friendship.split("#"));

            String friendName = (friends.get(0).equals(userLocalStore.getLoggedUser().name) ?
                                 friends.get(1) : friends.get(0));

            Log.d("friend", friendName);

            friendsList.add(friendName);
        }
    }
}
