package com.arara.smartwardrobe;

import android.content.Context;
import android.content.SharedPreferences;

public class UserLocalStore {

    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDB;

    public UserLocalStore(Context context) {
        userLocalDB = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(User user) {
        SharedPreferences.Editor spEditor = userLocalDB.edit();
        spEditor.putString("name", user.name);
        spEditor.putString("password", user.password);
        spEditor.commit();
    }

    public User getLoggedUser() {
        return (new User(this.userLocalDB.getString("name",""), this.userLocalDB.getString("password", "")));
    }

    public void setUserLogged(boolean logged) {
        SharedPreferences.Editor spEditor = userLocalDB.edit();
        spEditor.putBoolean("logged", logged);
        spEditor.commit();
    }

    public void clearUserData() {
        SharedPreferences.Editor spEditor = userLocalDB.edit();
        spEditor.clear();
        spEditor.commit();
    }
}
