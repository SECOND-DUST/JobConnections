package com.SECDUST.jobconnnection;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

public class HttpAsyncTask extends AsyncTask<String, Void, String> {

    ArrayList<String> TitleArrayList = new ArrayList<String>();
    ArrayList<String> DescArrayList = new ArrayList<String>();
    ArrayList<String> QualArrayList = new ArrayList<String>();
    ArrayList<String> TaskArrayList = new ArrayList<String>();
    ArrayList<Integer> SocArrayList = new ArrayList<Integer>();
    ExpandableListView Details;

    public String GET(String url) {
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

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) MainActivity.getAppContext().getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    @Override
    protected String doInBackground(String... urls) {

        return GET(urls[0]);
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(MainActivity.getAppContext(), "Received!", Toast.LENGTH_LONG).show();
        try {
            JSONArray jsonArray = new JSONArray(result);
            //test2.update();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // tvResponse.setText(result);
        Toast.makeText(MainActivity.getAppContext(), "Received!", Toast.LENGTH_LONG).show();
        JSONArray c;
        try {
            JSONArray jsonArray = new JSONArray(result);
            Spinner sepresult = null;
            sepresult = GlobalVars.setSpinner(sepresult);
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
            ArrayAdapter<String> spinnerArrayAdapter = new MyAdapter(MainActivity.getAppContext(), R.layout.custom_spinner, stringArray);
            sepresult.setAdapter(spinnerArrayAdapter);
            GlobalVars.setArrays(DescArrayList, QualArrayList, TaskArrayList, SocArrayList,TitleArrayList);
            //setArrays();
            GlobalVars.ExpListItems = MainActivity.SetStandardGroups();
            GlobalVars.ExpAdapter = new ExpandListAdapter(MainActivity.getAppContext(), GlobalVars.ExpListItems);
            Details = GlobalVars.setExpandableListVieew(Details);
            GlobalVars.Details.setAdapter(GlobalVars.ExpAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
