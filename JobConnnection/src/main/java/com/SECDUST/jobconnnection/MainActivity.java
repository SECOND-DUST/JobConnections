package com.SECDUST.jobconnnection;

import android.app.Activity;
import android.content.Context;
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
    public Activity context2 = this;

    static String LMIforAllBaseURL = "http://api.lmiforall.org.uk/api/v1/";
    static String socSearchURL = "soc/search";
    EditText etQuery;
    TextView tvIsConnected;
    TextView tvResponse;
    public static Spinner sepresult;
    ExpandableListView Details;

    EditText txtSearch;


    public ExpandListAdapter ExpAdapter;
    public ArrayList<ExpandListGroup> ExpListItems;
    public ExpandableListView ExpandList;

    ArrayList<String> TitleArrayList;
    ArrayList<String> DescArrayList;
    ArrayList<String> QualArrayList;
    ArrayList<String> TaskArrayList;
    ArrayList<Integer> SocArrayList;

    GlobalVars variables = new GlobalVars();

    public static Context context;

    public void setSepresult(Spinner sepresult) {
        this.sepresult = sepresult;
    }

    public Spinner getSepresult() {
        return sepresult;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.context = getApplicationContext();

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
        setSepresult(sepresult);
        Details = (ExpandableListView) findViewById(R.id.expandableListView);
        GlobalVars.getDetails(Details);
        GlobalVars.getSepResult(sepresult);

        // call AsynTask to perform network operation on separate
        new HttpAsyncTask().execute(LMIforAllBaseURL + socSearchURL + "?q=chef");


        sepresult.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (GlobalVars.SavedDescArrayList != null) {
                    ExpListItems = SetStandardGroups();
                    ExpAdapter = new ExpandListAdapter(MainActivity.this, ExpListItems);
                    Details.setAdapter(ExpAdapter);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    public static ArrayList<ExpandListGroup> SetStandardGroups() {
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

        sepresult = GlobalVars.setSpinner(sepresult);
        ArrayList<String> DescArrayList = GlobalVars.SavedDescArrayList;
        ArrayList<String> QualArrayList = GlobalVars.SavedQualArrayList;
        ArrayList<Integer> SocArrayList = GlobalVars.SavedSocArrayList;
        ArrayList<String> TaskArrayList = GlobalVars.SavedTaskArrayList;
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

    public static Context getAppContext() {
        return MainActivity.context;
    }
}

