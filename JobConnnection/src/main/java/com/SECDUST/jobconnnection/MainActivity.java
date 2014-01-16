package com.SECDUST.jobconnnection;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting the text input + button ready for using.
        txtSearch = (EditText) findViewById(R.id.searchTxt);
        final Button btnSearch = (Button) findViewById(R.id.carrersearch);
        boolean enablebtn = !(txtSearch.getText().toString().equals(""));
        btnSearch.setEnabled(enablebtn);

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
                boolean enablebtn = ! txtSearch.getText().toString().equals("");
                btnSearch.setEnabled(enablebtn);
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

        // call AsynTask to perform network operation on separate thread
        new HttpAsyncTask().execute(LMIforAllBaseURL + socSearchURL + "?q=chef");

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
                ArrayList<String> DescArrayList = new ArrayList<String>();
                ArrayList<String> QualArrayList = new ArrayList<String>();
                ArrayList<String> TaskArrayList = new ArrayList<String>();
                ArrayList<Integer> SocArrayList = new ArrayList<Integer>();
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

            } catch (JSONException e) {
                e.printStackTrace();
            }
            tvResponse.setText(result);
        }
    }
}
