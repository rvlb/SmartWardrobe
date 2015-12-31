package com.arara.smartwardrobe;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.telecom.Call;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class ServerRequest {

    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://192.168.25.85/~Renato/";

    public ServerRequest(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait...");
    }

    public void fetchUserDataInBackground(User user, Callback callback) {
        progressDialog.show();
        new FetchUserDataAsyncTask(user, callback).execute();
    }

    public class FetchUserDataAsyncTask extends AsyncTask<Void, Void, ServerResponse> {

        User user;
        Callback callback;

        public FetchUserDataAsyncTask(User user, Callback callback) {
            this.user = user;
            this.callback = callback;
        }

        @Override
        protected ServerResponse doInBackground(Void... params) {
            HashMap<String, String> dataToSend = new HashMap<>();
            dataToSend.put("name", user.name);
            dataToSend.put("password", user.password);
            String response = "error";
            ServerResponse serverResponse = new ServerResponse(null, null, response);

            try {
                URL url = new URL(SERVER_ADDRESS + "login_user.php");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(CONNECTION_TIMEOUT);
                con.setConnectTimeout(CONNECTION_TIMEOUT);
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);

                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(Misc.getPostDataString(dataToSend));
                writer.flush();
                writer.close();
                os.close();

                int code = con.getResponseCode();
                Log.d("code", code + "");

                InputStream responseStream = new BufferedInputStream(con.getInputStream());
                BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
                response = responseStreamReader.readLine();
                responseStreamReader.close();

                serverResponse.returnedUser = user;
                serverResponse.response = response.replace("\"","");
                serverResponse.response = serverResponse.response.replace("<br />","");

                Log.d("user", serverResponse.returnedUser.name);
                Log.d("response", serverResponse.response);

            } catch(Exception e) {
                e.printStackTrace();
            }
            return serverResponse;
        }

        @Override
        protected void onPostExecute(ServerResponse serverResponse) {
            progressDialog.dismiss();
            callback.done(serverResponse);
            super.onPostExecute(serverResponse);
        }
    }

    public void storeUserDataInBackground(User user, Callback callback) {
        progressDialog.show();
        new StoreUserDataAsyncTask(user, callback).execute();
    }

    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, ServerResponse> {

        User user;
        Callback callback;

        public StoreUserDataAsyncTask(User user, Callback callback) {
            this.user = user;
            this.callback = callback;
        }

        @Override
        protected ServerResponse doInBackground(Void... params) {

            String response = "error";
            ServerResponse serverResponse = new ServerResponse(null, null, response);

            try {
                URL url = new URL(SERVER_ADDRESS + "register_user.php");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(CONNECTION_TIMEOUT);
                con.setConnectTimeout(CONNECTION_TIMEOUT);
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);

                Log.d("name", user.name);
                Log.d("password", user.password);
                Log.d("password_rep", user.passwordRep);

                Uri.Builder builder = new Uri.Builder();
                builder.appendQueryParameter("name", user.name);
                builder.appendQueryParameter("password", user.password);
                builder.appendQueryParameter("password_rep", user.passwordRep);
                String query = builder.build().getEncodedQuery();
                Log.d("query",query);

                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                int code = con.getResponseCode();
                Log.d("code", code + "");

                InputStream responseStream = new BufferedInputStream(con.getInputStream());
                BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
                response = responseStreamReader.readLine();
                responseStreamReader.close();

                serverResponse.response = response.replace("\"","");
                serverResponse.response = serverResponse.response.replace("<br />","");

                Log.d("response", serverResponse.response);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return serverResponse;
        }

        @Override
        protected void onPostExecute(ServerResponse serverResponse) {
            progressDialog.dismiss();
            callback.done(serverResponse);
            super.onPostExecute(serverResponse);
        }
    }

    public void storeWearableDataInBackground(Wearable wearable, Callback callback) {
        progressDialog.show();
        new StoreWearableDataAsyncTask(wearable, callback).execute();
    }

    public class StoreWearableDataAsyncTask extends AsyncTask<Void, Void, ServerResponse> {

        Wearable wearable;
        Callback callback;

        public StoreWearableDataAsyncTask(Wearable wearable, Callback callback) {
            this.wearable = wearable;
            this.callback = callback;
        }

        @Override
        protected ServerResponse doInBackground(Void... params) {

            String response = "error";
            ServerResponse serverResponse = new ServerResponse(null, null, response);

            try {
                URL url = new URL(SERVER_ADDRESS + "insert_wearable.php");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(CONNECTION_TIMEOUT);
                con.setConnectTimeout(CONNECTION_TIMEOUT);
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);

                Log.d("colors", wearable.colors);
                Log.d("type", wearable.type);
                Log.d("brand", wearable.brand);
                Log.d("description", wearable.description);

                Uri.Builder builder = new Uri.Builder();
                builder.appendQueryParameter("colors", wearable.colors);
                builder.appendQueryParameter("type", wearable.type);
                builder.appendQueryParameter("brand", wearable.brand);
                builder.appendQueryParameter("description", wearable.description);
                String query = builder.build().getEncodedQuery();
                Log.d("query",query);

                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                int code = con.getResponseCode();
                Log.d("code", code + "");

                InputStream responseStream = new BufferedInputStream(con.getInputStream());
                BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
                response = responseStreamReader.readLine();
                responseStreamReader.close();

                serverResponse.response = response.replace("\"","");
                serverResponse.response = serverResponse.response.replace("<br />","");

                Log.d("response", serverResponse.response);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return serverResponse;
        }

        @Override
        protected void onPostExecute(ServerResponse serverResponse) {
            progressDialog.dismiss();
            callback.done(serverResponse);
            super.onPostExecute(serverResponse);
        }
    }

    public void fetchUserWearableDataInBackground(User user, Callback callback) {
        progressDialog.show();
        new FetchUserWearableDataAsyncTask(user, callback).execute();
    }

    public class FetchUserWearableDataAsyncTask extends AsyncTask<Void, Void, ServerResponse> {

        User user;
        Callback callback;

        public FetchUserWearableDataAsyncTask(User user, Callback callback) {
            this.user = user;
            this.callback = callback;
        }

        @Override
        protected ServerResponse doInBackground(Void... params) {
            HashMap<String, String> dataToSend = new HashMap<>();
            dataToSend.put("tag_owner", user.name);
            String response = "error";
            ServerResponse serverResponse = new ServerResponse(null, null, response);

            try {
                URL url = new URL(SERVER_ADDRESS + "get_user_wearables.php");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(CONNECTION_TIMEOUT);
                con.setConnectTimeout(CONNECTION_TIMEOUT);
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);

                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(Misc.getPostDataString(dataToSend));
                writer.flush();
                writer.close();
                os.close();

                int code = con.getResponseCode();
                Log.d("code", code + "");

                InputStream responseStream = new BufferedInputStream(con.getInputStream());
                BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
                response = responseStreamReader.readLine();
                responseStreamReader.close();

                serverResponse.response = response.replace("\"","");
                serverResponse.response = serverResponse.response.replace("<br />", "");

                Log.d("response", serverResponse.response);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return serverResponse;
        }

        @Override
        protected void onPostExecute(ServerResponse serverResponse) {
            progressDialog.dismiss();
            callback.done(serverResponse);
            super.onPostExecute(serverResponse);
        }
    }

    public void fetchTagDataInBackground(String tag, Callback callback) {
        progressDialog.show();
        new FetchTagDataAsyncTask(tag, callback).execute();
    }

    public class FetchTagDataAsyncTask extends AsyncTask<Void, Void, ServerResponse> {

        String tag;
        Callback callback;

        public FetchTagDataAsyncTask(String tag, Callback callback) {
            this.tag = tag;
            this.callback = callback;
        }

        @Override
        protected ServerResponse doInBackground(Void... params) {
            HashMap<String, String> dataToSend = new HashMap<>();
            dataToSend.put("tag_id", tag);
            String response = "error";
            ServerResponse serverResponse = new ServerResponse(null, null, response);

            try {
                URL url = new URL(SERVER_ADDRESS + "get_tag_wearable.php");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(CONNECTION_TIMEOUT);
                con.setConnectTimeout(CONNECTION_TIMEOUT);
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);

                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(Misc.getPostDataString(dataToSend));
                writer.flush();
                writer.close();
                os.close();

                int code = con.getResponseCode();
                Log.d("code", code + "");

                InputStream responseStream = new BufferedInputStream(con.getInputStream());
                BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
                response = responseStreamReader.readLine();
                responseStreamReader.close();

                serverResponse.response = response.replace("\"","");
                serverResponse.response = serverResponse.response.replace("<br />", "");

                Log.d("response", serverResponse.response);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return serverResponse;
        }

        @Override
        protected void onPostExecute(ServerResponse serverResponse) {
            progressDialog.dismiss();
            callback.done(serverResponse);
            super.onPostExecute(serverResponse);
        }
    }

    public void fetchFriendshipDataInBackground(User user, Callback callback) {
        progressDialog.show();
        new FetchFriendshipDataAsyncTask(user, callback).execute();
    }

    public class FetchFriendshipDataAsyncTask extends AsyncTask<Void, Void, ServerResponse> {

        User user;
        Callback callback;

        public FetchFriendshipDataAsyncTask(User user, Callback callback) {
            this.user = user;
            this.callback = callback;
        }

        @Override
        protected ServerResponse doInBackground(Void... params) {
            HashMap<String, String> dataToSend = new HashMap<>();
            dataToSend.put("name", user.name);
            String response = "error";
            ServerResponse serverResponse = new ServerResponse(null, null, response);

            try {
                URL url = new URL(SERVER_ADDRESS + "get_user_friends.php");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(CONNECTION_TIMEOUT);
                con.setConnectTimeout(CONNECTION_TIMEOUT);
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);

                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(Misc.getPostDataString(dataToSend));
                writer.flush();
                writer.close();
                os.close();

                int code = con.getResponseCode();
                Log.d("code", code + "");

                InputStream responseStream = new BufferedInputStream(con.getInputStream());
                BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
                response = responseStreamReader.readLine();
                responseStreamReader.close();

                serverResponse.response = response.replace("\"","");
                serverResponse.response = serverResponse.response.replace("<br />", "");

                Log.d("response", serverResponse.response);

            } catch(Exception e) {
                e.printStackTrace();
            }

            return serverResponse;
        }

        @Override
        protected void onPostExecute(ServerResponse serverResponse) {
            progressDialog.dismiss();
            callback.done(serverResponse);
            super.onPostExecute(serverResponse);
        }
    }

    public void storeFriendshipDataInBackground(Friendship friendship, Callback callback) {
        progressDialog.show();
        new StoreFriendshipDataAsyncTask(friendship, callback).execute();
    }

    public class StoreFriendshipDataAsyncTask extends AsyncTask<Void, Void, ServerResponse> {

        Friendship friendship;
        Callback callback;

        public StoreFriendshipDataAsyncTask(Friendship friendship, Callback callback) {
            this.friendship = friendship;
            this.callback = callback;
        }

        @Override
        protected ServerResponse doInBackground(Void... params) {

            String response = "error";
            ServerResponse serverResponse = new ServerResponse(null, null, response);

            try {
                URL url = new URL(SERVER_ADDRESS + "insert_friendship.php");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(CONNECTION_TIMEOUT);
                con.setConnectTimeout(CONNECTION_TIMEOUT);
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);

                Log.d("user_name", friendship.userName);
                Log.d("friend_name", friendship.friendName);

                Uri.Builder builder = new Uri.Builder();
                builder.appendQueryParameter("user_name", friendship.userName);
                builder.appendQueryParameter("friend_name", friendship.friendName);
                String query = builder.build().getEncodedQuery();
                Log.d("query",query);

                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                int code = con.getResponseCode();
                Log.d("code", code + "");

                InputStream responseStream = new BufferedInputStream(con.getInputStream());
                BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
                response = responseStreamReader.readLine();
                responseStreamReader.close();

                serverResponse.response = response.replace("\"","");
                serverResponse.response = serverResponse.response.replace("<br />","");

                Log.d("response", serverResponse.response);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return serverResponse;
        }

        @Override
        protected void onPostExecute(ServerResponse serverResponse) {
            progressDialog.dismiss();
            callback.done(serverResponse);
            super.onPostExecute(serverResponse);
        }
    }
}
