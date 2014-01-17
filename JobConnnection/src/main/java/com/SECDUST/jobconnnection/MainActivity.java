package com.SECDUST.jobconnnection;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends Activity {
    String LMIforAllBaseURL = "http://api.lmiforall.org.uk/api/v1/";
    String socSearchURL = "soc/search";
    EditText etQuery;
    TextView tvIsConnected;
    TextView tvResponse;
    Spinner sepresult;
    ExpandableListView Desc;
    ExpandableListView Soc;
    ExpandableListView Tasks;
    ExpandableListView Quali;

    EditText txtSearch;


    private ExpandListAdapter ExpAdapter;
    private ArrayList<ExpandListGroup> ExpListItems;
    private ExpandableListView ExpandList;

    ArrayList<String> TitleArrayList;
    ArrayList<String> DescArrayList;
    ArrayList<String> QualArrayList;
    ArrayList<String> TaskArrayList;
    ArrayList<Integer> SocArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting the text input + button ready for using.
        //Also setting the button to disabled by default.
        txtSearch = (EditText) findViewById(R.id.searchTxt);
        final Button btnSearch = (Button) findViewById(R.id.carrersearch);
        btnSearch.setEnabled(!txtSearch.getText().toString().trim().equals(""));

        //This will disable the search button so long as there's no text in the searchbar
        //don't need to disable btn by default
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                btnSearch.setEnabled(!txtSearch.getText().toString().trim().equals(""));
            }
        });

        // get reference to the views
        etQuery = (EditText) findViewById(R.id.editText);
        tvIsConnected = (TextView) findViewById(R.id.textView2);
        tvResponse = (TextView) findViewById(R.id.textView);

        // check if you are connected or not
        if (isConnected()) {
            tvIsConnected.setText("You are conncted");
        } else {
            tvIsConnected.setText("You are NOT conncted");
        }
        sepresult = (Spinner) findViewById(R.id.spinner);

        // call AsynTask to perform network operation on separate thread
        new HttpAsyncTask().execute(LMIforAllBaseURL + socSearchURL + "?q=chef");





        sepresult.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(DescArrayList != null){
                    ExpListItems = SetStandardGroups();
                    ExpAdapter = new ExpandListAdapter(MainActivity.this, ExpListItems);
                    Desc.setAdapter(ExpAdapter);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    public static String GET(String url) {
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            JSONArray c;
            try {
                JSONArray jsonArray = new JSONArray(result);
                sepresult = (Spinner) findViewById(R.id.spinner);
                String stringArray[];
                ArrayList<String> TitleArrayList = new ArrayList<String>();
                DescArrayList = new ArrayList<String>();
                QualArrayList = new ArrayList<String>();
                TaskArrayList = new ArrayList<String>();
                SocArrayList = new ArrayList<Integer>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json_data = jsonArray.getJSONObject(i);
                    TitleArrayList.add(json_data.getString("title"));
                    DescArrayList.add(json_data.getString("description"));
                    QualArrayList.add(json_data.getString("qualifications"));
                    TaskArrayList.add(json_data.getString("tasks"));
                    SocArrayList.add(json_data.getInt("soc"));
                }
                stringArray = TitleArrayList.toArray(new String[TitleArrayList.size()]);
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, stringArray);
                sepresult.setAdapter(spinnerArrayAdapter);
                ExpListItems = SetStandardGroups();
                ExpAdapter = new ExpandListAdapter(MainActivity.this, ExpListItems);
                Desc = (ExpandableListView) findViewById(R.id.expandableListView);
                Desc.setAdapter(ExpAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            tvResponse.setText(result);
        }
    }

    public ArrayList<ExpandListGroup> SetStandardGroups() {
        ArrayList<ExpandListGroup> list = new ArrayList<ExpandListGroup>();
        ArrayList<ExpandListChild> list2 = new ArrayList<ExpandListChild>();
        ArrayList<ExpandListChild> listD = new ArrayList<ExpandListChild>();
        ArrayList<ExpandListChild> listT = new ArrayList<ExpandListChild>();
        ArrayList<ExpandListChild> listS = new ArrayList<ExpandListChild>();
        ArrayList<ExpandListChild> listQ = new ArrayList<ExpandListChild>();

        ExpandListGroup Descript = new ExpandListGroup();
        Descript.setName("Description");
        ExpandListGroup Tasks = new ExpandListGroup();
        Tasks.setName("Task");
        ExpandListGroup Qualies = new ExpandListGroup();
        Qualies.setName("Qualifications");
        ExpandListGroup SocCode = new ExpandListGroup();
        SocCode.setName("Soc");
        ExpandListChild PlaceHolder = new ExpandListChild();
        PlaceHolder.setName("N/A");
        PlaceHolder.setTag(null);
        list2.add(PlaceHolder);

        sepresult = (Spinner) findViewById(R.id.spinner);
        int i = sepresult.getSelectedItemPosition();
        ExpandListChild Desctext = new ExpandListChild();
        Desctext.setName(DescArrayList.get(i));
        Desctext.setTag(null);
        listD.add(Desctext);

        ExpandListChild Tasktext = new ExpandListChild();
        Tasktext.setName(TaskArrayList.get(i));
        Tasktext.setTag(null);
        listT.add(Tasktext);

        ExpandListChild Qualtext = new ExpandListChild();
        Qualtext.setName(QualArrayList.get(i));
        Qualtext.setTag(null);
        listQ.add(Qualtext);

        ExpandListChild Soctext = new ExpandListChild();
        Soctext.setName(SocArrayList.get(i).toString());
        Soctext.setTag(null);
        listS.add(Soctext);

        Descript.setItems(listD);
        Tasks.setItems(listT);
        Qualies.setItems(listQ);
        SocCode.setItems(listS);
        list.add(Descript);
        list.add(Tasks);
        list.add(Qualies);
        list.add(SocCode);

        return list;
    }

}

