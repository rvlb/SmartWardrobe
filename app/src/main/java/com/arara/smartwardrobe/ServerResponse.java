package com.arara.smartwardrobe;

public class ServerResponse {

    User returnedUser;
    Wearable returnedWearable;
    String response;

    public ServerResponse(User returnedUser, Wearable returnedWearable, String response) {

        this.returnedUser = returnedUser;
        this.returnedWearable = returnedWearable;
        this.response = response;
    }
}
