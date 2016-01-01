package com.arara.smartwardrobe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyFriendsActivity extends AppCompatActivity implements View.OnClickListener {

    Button bAddFriend;
    UserLocalStore userLocalStore;

    List<String> friendsList;
    ListView lvFriends;

    TextView tvFriendListName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friends);

        userLocalStore = new UserLocalStore(this);

        friendsList = new ArrayList<>();

        tvFriendListName = (TextView) findViewById(R.id.tvFriendListName);
        tvFriendListName.setText((userLocalStore.getLoggedUser().name + "'s friends").toUpperCase());

        bAddFriend = (Button) findViewById(R.id.bAddFriend);
        bAddFriend.setOnClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, friendsList);
        Log.d("adapterSize", adapter.getCount() + "");
        lvFriends = (ListView) findViewById(R.id.lvFriends);
        lvFriends.setAdapter(adapter);
        lvFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedFriend = (String) lvFriends.getItemAtPosition(position);
                //Misc.showAlertMsg("Clicked on " + selectedFriend, "Ok", MyFriendsActivity.this);
                Intent intent = new Intent(MyFriendsActivity.this, MyWearablesActivity.class);
                intent.putExtra("owner", selectedFriend);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
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
            public void done(String serverResponse) {
                Log.d("serverResponseMyFr", serverResponse);
                if(serverResponse.equals("error")) {
                    Misc.showAlertMsg("An error occurred while trying to connect.", "Ok", MyFriendsActivity.this);
                    finish();
                } else if(serverResponse.equals("no friends")) {
                    Log.d("no friend", "No friend found");
                    //Misc.showAlertMsg("No friend found.", "Ok", MyFriendsActivity.this);
                } else {
                    buildFriendsList(serverResponse);
                    ((BaseAdapter) lvFriends.getAdapter()).notifyDataSetChanged();
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

            if (!friendsList.contains(friendName)) {
                friendsList.add(friendName);
            }

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
