package ca.ualberta.t04.medicaltracker;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;


import java.io.IOException;
import java.util.concurrent.ExecutionException;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.DeleteByQuery;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
/*
    The idea of this part is from Lab5 of CMPUT301
 */
public class ElasticSearchController
{
    private static JestClient client = null;
    private static String USER_TYPE = "user";
    private static String INDEX_NAME = "medicaltracker";
    private static String IS_DOCTOR = "isDoctor";

    public static JestClient getClient()
    {
        if(client==null)
            setClient();
        return client;
    }

    public static void deleteIndex(String indexName){
        try {
            client.execute(new Delete.Builder(INDEX_NAME).build());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Used to sign up user
    public static Boolean signUp(User user){
        Boolean done = false;
        try {
            done = new SignUpTask().execute(user).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return done;
    }

    // Used to delete user
    public static void deleteUser(String userName){
        new ElasticSearchController.DeleteUserTask().execute(userName);
    }

    // Used to search user
    public static void updateUser(User user){
        new ElasticSearchController.UpdateUserTask().execute(user);
    }

    // Used to search user
    public static User searchUser(String userName){
        try {
            User user = new ElasticSearchController.SearchUserTask().execute(userName).get();
            if(user.isDoctor()){
                Doctor doctor = (Doctor) user;
                return doctor;
            } else {
                Patient patient = (Patient) user;
                return patient;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class UpdateUserTask extends AsyncTask<User, Void, Void>
    {
        @Override
        protected Void doInBackground(User... users) {
            setClient();

            User user = users[0];
            String userName = user.getUserName();

            // Build the search query
            String query = "{\n" +
                    "    \"query\": {\n" +
                    "        \"query_string\" : {\n" +
                    "            \"query\" : \"(userName:" + userName + " AND _type:" + USER_TYPE + ")\" \n" +
                    "        }\n" +
                    "    }\n" +
                    "}";

            DeleteByQuery deleteByQuery = new DeleteByQuery.Builder(query)
                    .addIndex(INDEX_NAME)
                    .addType(USER_TYPE)
                    .build();

            // Re-add the User
            Index userNameIndex = new Index.Builder(user).index(INDEX_NAME).type(USER_TYPE).build();
            // If searched, then return object, otherwise return null
            try {
                // Execute the delete action
                JestResult jestResult = client.execute(deleteByQuery);
                if(jestResult.isSucceeded()){
                    Log.d("Succeed", "Deleted!");
                    // Execute the add action
                    DocumentResult result = client.execute(userNameIndex);
                    if(result.isSucceeded()){
                        Log.d("Succeed", "Re-added it!");
                    }
                }
                else{
                    Log.d("Succeed", "Nothing Found!");
                }
            } catch (IOException e) {
                Log.d("Succeed", "Failed!");
                e.printStackTrace();
                return null;
            }
            return null;
        }
    }

    public static class DeleteUserTask extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... userNames) {
            setClient();

            String userName = userNames[0];

            // Build the search query
            String query = "{\n" +
                    "    \"query\": {\n" +
                    "        \"query_string\" : {\n" +
                    "            \"query\" : \"(userName:" + userName + " AND _type:" + USER_TYPE + ")\" \n" +
                    "        }\n" +
                    "    }\n" +
                    "}";

            DeleteByQuery deleteByQuery = new DeleteByQuery.Builder(query)
                    .addIndex(INDEX_NAME)
                    .addType(USER_TYPE)
                    .build();

            // If searched, then return object, otherwise return null
            try {
                JestResult jestResult = client.execute(deleteByQuery);
                if(jestResult.isSucceeded()){
                    Log.d("Succeed", "Deleted!");
                }
                else{
                    Log.d("Succeed", "Nothing Found!");
                }
            } catch (IOException e) {
                Log.d("Succeed", "Failed!");
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class SearchUserTask extends AsyncTask<String, Void, User>
    {
        @Override
        protected User doInBackground(String... userNames) {
            setClient();

            String userName = userNames[0];

            // Build the search query
            String query = "{\n" +
                    "    \"query\": {\n" +
                    "        \"query_string\" : {\n" +
                    "            \"query\" : \"(userName:" + userName + " AND _type:" + USER_TYPE + ")\" \n" +
                    "        }\n" +
                    "    }\n" +
                    "}";

            Search search = new Search.Builder(query)
                    // multiple index or types can be added.
                    .addIndex(INDEX_NAME)
                    .addType(USER_TYPE)
                    .build();

            // If searched, then return object, otherwise return null
            try {
                SearchResult searchResult = client.execute(search);
                if(searchResult.isSucceeded()){
                    Log.d("Succeed", searchResult.getSourceAsStringList().get(0));

                    // JsonParser is used to convert source string to JsonObject
                    JsonParser parser = new JsonParser();
                    JsonObject jsonObject = parser.parse(searchResult.getSourceAsStringList().get(0)).getAsJsonObject();

                    // Get the value of "isDoctor"
                    Boolean isDoctor = jsonObject.get(IS_DOCTOR).getAsBoolean();
                    if(isDoctor){
                        Doctor doctor = searchResult.getSourceAsObjectList(Doctor.class).get(0);
                        return doctor;
                    }
                    else{
                        Patient patient = searchResult.getSourceAsObjectList(Patient.class).get(0);
                        return patient;
                    }
                }
                else{
                    Log.d("Succeed", "Nothing Found!");
                }
            } catch (IOException e) {
                Log.d("Succeed", "Failed!");
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class SignUpTask extends AsyncTask<User, Void, Boolean>
    {
        @Override
        protected Boolean doInBackground(User... users) {
            setClient();
            User user = users[0];

            // Check if the userName is unique
            Boolean duplicated = false;

            String query = "{\n" +
                    "    \"query\": {\n" +
                    "        \"query_string\" : {\n" +
                    "            \"query\" : \"(userName:" + user.getUserName() + " AND _type:" + USER_TYPE + ")\" \n" +
                    "        }\n" +
                    "    }\n" +
                    "}";

            Search search = new Search.Builder(query)
                    // multiple index or types can be added.
                    .addIndex(INDEX_NAME)
                    .addType(USER_TYPE)
                    .build();

            try {
                SearchResult searchResult = client.execute(search);
                if(searchResult.isSucceeded()){
                    Log.d("Succeed", searchResult.getJsonString());
                    Log.d("Succeed", searchResult.getTotal().toString());
                    // If the value of total for this userName is not 0, then it means that the userName is duplicated
                    if(searchResult.getTotal()!=0)
                        duplicated = true;
                }
                else{
                    Log.d("Succeed", "Nothing Found!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // If the userName is unique, then sign up
            if(!duplicated){
                Index userNameIndex = new Index.Builder(user).index(INDEX_NAME).type(USER_TYPE).build();
                try
                {
                    DocumentResult result = client.execute(userNameIndex);
                    if(result.isSucceeded())
                        Log.d("Succeed", "Succeed!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
            else
                return false;
        }
    }

    public static void setClient()
    {
        if(client==null)
        {
            DroidClientConfig config = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080/").build();
            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = factory.getObject();
        }
    }
}
