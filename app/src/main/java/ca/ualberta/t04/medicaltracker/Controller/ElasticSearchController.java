package ca.ualberta.t04.medicaltracker.Controller;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ca.ualberta.t04.medicaltracker.Model.Doctor;
import ca.ualberta.t04.medicaltracker.Model.Patient;
import ca.ualberta.t04.medicaltracker.Model.Problem;
import ca.ualberta.t04.medicaltracker.Model.Record;
import ca.ualberta.t04.medicaltracker.Model.User;
import ca.ualberta.t04.medicaltracker.Util.CommonUtil;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import io.searchbox.core.BulkResult;
import io.searchbox.core.Delete;
import io.searchbox.core.DeleteByQuery;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;


import static android.support.constraint.Constraints.TAG;

/**
 * This class is exclusively the entire implementation of elastic search used for the application
 *
 * @author CMPUT301F18T04 Team 04
 * @version Project part 05 1.0
 * @since 1.0
 */

/*
    The idea of this part is from Lab5 of CMPUT301
 */
public class ElasticSearchController
{
    private static JestClient client = null;
    private static String USER_TYPE = "user";
    private static String RECORD_TYPE = "record";
    private static String INDEX_NAME = CommonUtil.INDEX_NAME;
    private static String IS_DOCTOR = "isDoctor";

    // Delete the whole index of ElasticSearch
    public static void deleteIndex(String indexName){
        setClient();
        try {
            client.execute(new Delete.Builder(INDEX_NAME).build());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Used to sign up a user
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

    // Used to delete a user with an exact username
    public static void deleteUser(String userName){
        new ElasticSearchController.DeleteUserTask().execute(userName);
    }

    // Used to update a user
    public static void updateUser(User user){
        new ElasticSearchController.UpdateUserTask().execute(user);
    }

    // Used to search users whose username contains keyword
    public static ArrayList<Patient> fuzzySearchPatient(String userNameKeyWord){
        ArrayList<Patient> patients = new ArrayList<>();
        try {
            patients = new ElasticSearchController.FuzzySearchPatientTask().execute(userNameKeyWord).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return patients;
    }

    // Used to search a user with an exact username
    public static User searchUser(String userName){
        try {
            User user = new ElasticSearchController.SearchUserTask().execute(userName).get();
            if(user==null)
                return null;
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

            // Update the index with the unique userName
            Index userNameIndex = new Index.Builder(user).index(INDEX_NAME).type(USER_TYPE).id(userName).build();

            try {
                // Execute the add action
                DocumentResult result = client.execute(userNameIndex);
                if(result.isSucceeded()) {
                    Log.d("Succeed", "Updated it!");
                }
            } catch (IOException e) {
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

    private static class FuzzySearchPatientTask extends AsyncTask<String, Void, ArrayList<Patient>>
    {
        @Override
        protected ArrayList<Patient> doInBackground(String... userNames) {
            setClient();

            String userName = userNames[0];

            // Build the search query
            String query = "{\n" +
                    "    \"query\": {\n" +
                    "        \"filtered\" : {\n" +
                    "            \"query\" : {\n" +
                    "                \"wildcard\" : {\n" +
                    "                    \"userName\" : \"*"+ userName + "*\"\n" +
                    "                }\n" +
                    "            },\n" +
                    "            \"filter\" : {\n" +
                    "                \"term\" : { \"isDoctor\" : \"false\" }\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";

            Log.d("Succeed", query);
            Search search = new Search.Builder(query)
                    // multiple index or types can be added.
                    .addIndex(INDEX_NAME)
                    .addType(USER_TYPE)
                    .build();

            // If searched, then return object, otherwise return null
            try {
                SearchResult searchResult = client.execute(search);
                if(searchResult.isSucceeded() && searchResult.getSourceAsStringList().size()>0){
                    Log.d("Succeed", String.valueOf(searchResult.getSourceAsStringList().size()));
                    Log.d("Succeed", searchResult.getSourceAsStringList().get(0));

                    // JsonParser is used to convert source string to JsonObject
                    ArrayList<Patient> patients = new ArrayList<>(searchResult.getSourceAsObjectList(Patient.class));
                    return patients;
                }
                else{
                    Log.d("Succeed", "Nothing Found!");
                }
            } catch (IOException e) {
                Log.d("Succeed", "Failed!");
                e.printStackTrace();
            }
            return new ArrayList<>();
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
                if(searchResult.isSucceeded() && searchResult.getSourceAsStringList().size()>0){
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
                Index userNameIndex = new Index.Builder(user).index(INDEX_NAME).type(USER_TYPE).id(user.getUserName()).build();
                try
                {
                    DocumentResult result = client.execute(userNameIndex);
                    if(result.isSucceeded()){
                        Log.d("Succeed", "Succeed!");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
            else
                return false;
        }
    }

    // Used to update or create a record
    private static class UpdateRecordTask extends AsyncTask<Record, Void, Void>
    {
        @Override
        protected Void doInBackground(Record... records) {
            setClient();

            Record record = records[0];
            String recordId = record.getRecordId();

            // Update the index with the unique userName
            Index userNameIndex = new Index.Builder(record).index(INDEX_NAME).type(RECORD_TYPE).id(recordId).build();

            try {
                // Execute the add action
                DocumentResult result = client.execute(userNameIndex);
                if(result.isSucceeded()) {
                    Log.d("Succeed", "Updated Record!");
                }
            } catch (IOException e) {
                Log.d("Succeed", "Failed!");
                e.printStackTrace();
                return null;
            }
            return null;
        }
    }

    // Used to search a record
    private static class SearchRecordTask extends AsyncTask<String, Void, Record>
    {
        @Override
        protected Record doInBackground(String... recordIds) {
            setClient();

            String recordId = recordIds[0];

            Get get = new Get.Builder(INDEX_NAME, recordId).type(RECORD_TYPE).build();

            // If searched, then return object, otherwise return null
            try {
                DocumentResult result = client.execute(get);
                Record record = result.getSourceAsObject(Record.class);
                return record;
            } catch (IOException e) {
                Log.d("Succeed", "Failed!");
                e.printStackTrace();
            }
            return null;
        }
    }

    public static HashMap<String, ArrayList<String>> searchRecordComment(String recordId){
        try {
            return new SearchRecordCommentTask().execute(recordId).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Used to search a specific record's comments
    private static class SearchRecordCommentTask extends AsyncTask<String, Void, HashMap<String, ArrayList<String>>>
    {
        @Override
        protected HashMap<String, ArrayList<String>> doInBackground(String... recordIds) {
            setClient();

            String recordId = recordIds[0];

            String query = "{\n" +
                    "    \"query\": {\n" +
                    "        \"ids\" : {\n" +
                    "            \"values\" : [\"" + recordId + "\"]\n" +
                    "                  }\n" +
                    "                },\n" +
                    "    \"_source\": [\"comments\"]\n" +
                    "}";

            Search search = new Search.Builder(query)
                    // multiple index or types can be added.
                    .addIndex(INDEX_NAME)
                    .addType(RECORD_TYPE)
                    .build();

            // If searched, then return object, otherwise return null
            try {
                SearchResult result = client.execute(search);
                JsonParser parser = new JsonParser();
                JsonObject jsonObject = parser.parse(result.getSourceAsString()).getAsJsonObject();

                Log.d("Succeed", jsonObject.get("comments").toString());

                Gson gson = new Gson();
                HashMap<String, ArrayList<String>> comments = gson.fromJson(jsonObject.get("comments").toString(), HashMap.class);

                Log.d("Succeed", comments.toString());
                return comments;
            } catch (IOException e) {
                Log.d("Succeed", "Failed!");
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class SearchRecordByGeolocationTask extends AsyncTask<Object[], Void, ArrayList<Record>>
    {
        @Override
        protected ArrayList<Record> doInBackground(Object[]... objects) {
            setClient();

            Object[] object = objects[0];

            String keyWord = (String) object[0];
            Location location = (Location) object[1];

            Double lat = location.getLatitude();
            Double lon = location.getLongitude();

            // Build the search query
            String query = "{\n" +
                    "    \"filtered\" : {\n" +
                    "        \"query\" : {\n" +
                    "            \"query_string\" : {\n" +
                    "            \"query\" : \"(title:" + keyWord + " OR description:" + keyWord + ")\" \n" +
                    "             }\n" +
                    "        },\n" +
                    "        \"filter\" : {\n" +
                    "            \"geo_distance\" : {\n" +
                    "                \"distance\" : \"2km\",\n" +
                    "                \"pin.location\" : {\n" +
                    "                    \"lat\" : " + lat + ",\n" +
                    "                    \"lon\" : " + lon + "\n" +
                    "                }\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";

            Search search = new Search.Builder(query)
                    // multiple index or types can be added.
                    .addIndex(INDEX_NAME)
                    .addType(RECORD_TYPE)
                    .build();
            ArrayList<Record> records = null;
            // If searched, then return object, otherwise return null
            try {
                SearchResult searchResult = client.execute(search);
                if(searchResult.isSucceeded() && searchResult.getSourceAsStringList().size()>0){
                    Log.d("Succeed", searchResult.getSourceAsStringList().get(0));
                    records = new ArrayList<>(searchResult.getSourceAsObjectList(Record.class));
                }
                else{
                    Log.d("Succeed", "Nothing Found!");
                }
            } catch (IOException e) {
                Log.d("Succeed", "Failed!");
                e.printStackTrace();
            }
            return records;
        }
    }

    // Used to search many records by one call
    private static class SearchRecordListTask extends AsyncTask<String, Void, ArrayList<Record>>
    {
        @Override
        protected ArrayList<Record> doInBackground(String... recordIds) {
            setClient();

            String query = "{\n" +
                    "    \"query\": {\n" +
                    "        \"ids\" : {\n" +
                    "            \"values\" : [%s]\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";

            StringBuilder ids = new StringBuilder();

            for(int i=0; i<recordIds.length; i++){
                ids.append("\"").append(recordIds[i]).append("\"");
                if(i+1!=recordIds.length){
                    ids.append(",");
                }
            }

            query = query.replace("%s", ids);

            Log.d("Succeed", query);

            Search search = new Search.Builder(query)
                    // multiple index or types can be added.
                    .addIndex(INDEX_NAME)
                    .addType(RECORD_TYPE)
                    .build();

            // If searched, then return object, otherwise return null
            try {
                SearchResult searchResult = client.execute(search);
                if(searchResult.isSucceeded() && searchResult.getSourceAsStringList().size()>0){
                    Log.d("Succeed", String.valueOf(searchResult.getSourceAsStringList().size()));
                    Log.d("Succeed", searchResult.getSourceAsStringList().get(0));

                    // JsonParser is used to convert source string to JsonObject
                    ArrayList<Record> records = new ArrayList<>(searchResult.getSourceAsObjectList(Record.class));
                    return records;
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

    // Used to delete many records by one call
    public static class DeleteRecordListTask extends AsyncTask<String, Void, Void>{
        @Override
        protected Void doInBackground(String... recordIds) {
            setClient();

            ArrayList<Delete> deletes = new ArrayList<>();

            for(String recordId:recordIds){
                Delete delete = new Delete.Builder(recordId)
                        .index(INDEX_NAME)
                        .type(RECORD_TYPE)
                        .build();
                deletes.add(delete);
            }

            Bulk bulk = new Bulk.Builder()
                    .defaultType(INDEX_NAME)
                    .defaultType(RECORD_TYPE)
                    .addAction(deletes)
                    .build();

            try {
                BulkResult bulkResult = client.execute(bulk);
                if(bulkResult.isSucceeded()){
                    Log.d("Succeed", "Deleted many records");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    // Used to delete a record
    public static class DeleteRecordTask extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... recordIds) {
            setClient();

            String recordId = recordIds[0];

            Delete delete = new Delete.Builder(recordId)
                    .index(INDEX_NAME)
                    .type(RECORD_TYPE)
                    .build();

            try {
                JestResult jestResult = client.execute(delete);
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

    public static Record searchRecord(String recordId){
        try {
            return new SearchRecordTask().execute(recordId).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void createRecord(Record record){
        new UpdateRecordTask().execute(record);
    }

    public static void updateRecord(Record record){
        new UpdateRecordTask().execute(record);
    }

    public static void deleteRecord(String recordId){
        new DeleteRecordTask().execute(recordId);
    }

    public static void deleteRecordList(ArrayList<String> recordIds){
        if(recordIds==null){
            return;
        }
        new DeleteRecordListTask().execute((String[]) recordIds.toArray(new String[recordIds.size()]));
    }

    public static ArrayList<Record> searchRecordList(ArrayList<String> recordIds){
        ArrayList<Record> records = null;
        if(recordIds==null)
            return new ArrayList<>();
        try {
            records = new SearchRecordListTask().execute((String[]) recordIds.toArray(new String[recordIds.size()])).get();
            if(records==null)
                records = new ArrayList<>();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return records;
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
