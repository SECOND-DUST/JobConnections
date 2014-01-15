package com.SECDUST.jobconnnection;

import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by Chad on 14/01/14.
 */
public class PythonCodeToBeConverted extends ActionBarActivity {
    public JSONObject Results = new JSONObject();
    final String LMIForAllBaseURL = "http://api.lmiforall.org.uk/api/v1/";

    final String socSearchURL = "soc/search";
    final String wfPredictURL = "wf/predict";
    InputStream is = null;

    String json = "";
    String Query = "";
    JSONParser test = new JSONParser();

    public void test() {
        setContentView(R.layout.fragment_test);

        final Button Search = (Button) findViewById(R.id.carrersearch);
        final EditText searchquery = (EditText) findViewById(R.id.editText);
        final TextView ResultDisplay = (TextView) findViewById(R.id.textView);
        Search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (null != searchquery.getText().toString()){
                String query = searchquery.getText().toString();
                Results = searchquery(query);
                //ResultDisplay.setText();
                   JSONArray ResultArray = null;
                    try {
                        Results.toJSONArray(ResultArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i =0;i<ResultArray.length();i++) {
                    try {
                        ResultDisplay.append(ResultArray.getString(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                }
            }
        });

    }
    public JSONObject searchquery(String Query){
        Results = test.getJSONFromUrl(LMIForAllBaseURL+socSearchURL+"?q="+Query);
        return Results;
    }
    /*
import requests

        LMIforAllBaseURL = "http://api.lmiforall.org.uk/api/v1/"

        socSearchURL = "soc/search"
        wfPredictURL = "wf/predict"

        # remember this is 2.7 not 3 so we need to use raw_input for getting input from keyboard
        jobRole = raw_input('enter job to search for:')
        socSearch = LMIforAllBaseURL + socSearchURL # builds our url to use for using the soc api
        socSearchParam = {'q':jobRole}              # creates a dictionary of parameters to use in our search

        # remember that the soc search api has the one parameter
        # make the call to the lmiforall api, this version of requests.get takes the base url we pass in as the
        # first parameter, and then builds the rest of the url from the dictionary we pass in to make up the
        # final url with parameters.
        # so we pass it the url: http://api.lmiforall.org.uk/api/v1/soc/search
        # which it turns into http://api.lmiforall.org.uk/api/v1/soc/search?q=chef (or whatever career was entered)

        r = requests.get(socSearch,params=socSearchParam)

        jsonObj = r.json()

        # list our results from the search

        print('I found the following matches for your search:')

        # basically looping through the results to the screen
        count = 1
        for entry in jsonObj:
        print str(count) + '. ' + entry['title']
        count += 1

        # asks the user to select a job to find out more info for
        # raw_input gives us a string. you could add validation here to make sure only
        # numbers have been entered before converting the string into an integer.

        lookAt = raw_input('Enter which of the found items you would like to look at further: ')
        ilookAt = int(lookAt)  # convert the number entered (which is a string) into an integer

        # prints some basic job details
        # why have i used ilookAt-1 and not ilookAt?
        # if you are unsure change ilookAt-1 to ilookAt and run the program. What happens?

        print 'Role Details for being a '+ jsonObj[ilookAt-1]['title']
        print 'Description======================================'
        print jsonObj[ilookAt-1]['description']
        print 'Qualifications==================================='
        print jsonObj[ilookAt-1]['qualifications']
        print 'Tasks============================================'
        print jsonObj[ilookAt-1]['tasks']
        print '================================================='

        # lets get the work force predictions for the selected job roles

        socCode = jsonObj[ilookAt-1]['soc']

        wfPredict = LMIforAllBaseURL + wfPredictURL
        wfPredictParam = {'soc':socCode,'minYear':'2014','maxYear':'2020'} # build a dictionary of the parameters
        # we will use for our request to the
        # work force predictions api

        r = requests.get(wfPredict,params=wfPredictParam)

        jsonObj = r.json()

        print jsonObj # prints the json data unformatted
 */
}