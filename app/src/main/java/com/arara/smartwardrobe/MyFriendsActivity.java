package com.arara.smartwardrobe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyFriendsActivity extends AppCompatActivity implements View.OnClickListener {

    Button bAddFriend;
    UserLocalStore userLocalStore;

    List<String> friendsList;
    ListView lvFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friends);

        friendsList = new ArrayList<>();

        bAddFriend = (Button) findViewById(R.id.bAddFriend);
        bAddFriend.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);

        loadFriendsList();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, friendsList);
        Log.d("adapterSize", adapter.getCount() + "");

        lvFriends = (ListView) findViewById(R.id.lvFriends);
        lvFriends.setAdapter(adapter);
        lvFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedFriend = (String) lvFriends.getItemAtPosition(position);
                Misc.showAlertMsg("Clicked on " + selectedFriend, "Ok", MyFriendsActivity.this);
            }
        });
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
                }
            }
        });
    }

    private void buildFriendsList(String friendshipsData) {

        Log.d("debugTest", "Started building");

        Log.d("friendshipsData", friendshipsData);

        List<String> friendshipStrings = Arrays.asList(friendshipsData.split("\\$"));

        Log.d("friendshipsStrings", friendshipStrings.toString());

        for (String friendship: friendshipStrings) {

            List<String> friends = Arrays.asList(friendship.split("#"));

            String friendName = (friends.get(0).equals(userLocalStore.getLoggedUser().name) ?
                                 friends.get(1) : friends.get(0));

            friendsList.add(friendName);

            Log.d("friendsList " + friendsList.size(), friendsList.get(friendsList.size() - 1));
        }

        /*
        TEST CASES FOR CHECKING THE CAPACITY OF INSERTING INTO THE VIEW
        friendsList.add("Amigo Teste 1");
        friendsList.add("Amigo Teste 2");
        friendsList.add("Amigo Teste 3");
        friendsList.add("Amigo Teste 4");
        friendsList.add("Amigo Teste 5");
        friendsList.add("Amigo Teste 6");
        friendsList.add("Amigo Teste 7");
        friendsList.add("Amigo Teste 8");
        friendsList.add("Amigo Teste 9");
        friendsList.add("Amigo Teste 10");
        friendsList.add("Amigo Teste 11");
        friendsList.add("Amigo Teste 12");
        friendsList.add("Amigo Teste 13");
        friendsList.add("Amigo Teste 14");
        friendsList.add("Amigo Teste 15");
        friendsList.add("Amigo Teste 16");
        friendsList.add("Amigo Teste 17");
        friendsList.add("Amigo Teste 18");
        friendsList.add("Amigo Teste 19");
        friendsList.add("Amigo Teste 20");
        */

        Log.d("debugTest", "Finished building");
    }
}
