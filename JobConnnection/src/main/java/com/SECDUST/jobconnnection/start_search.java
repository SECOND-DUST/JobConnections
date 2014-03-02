package com.SECDUST.jobconnnection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Chad on 02/03/14.
 */
public class start_search extends Activity {
    static EditText txtSearch2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_start);
        MainActivity.context = getApplicationContext();

        //getting the text input + button ready for using.
        //Also setting the button to disabled by default.
        txtSearch2 = (EditText) findViewById(R.id.startsearchtxt);
        final Button btnSearch2 = (Button) findViewById(R.id.startsearchbtn);
        btnSearch2.setEnabled(!txtSearch2.getText().toString().trim().equals(""));

        //This will disable the search button so long as there's no text in the searchbar
        //don't need to disable btn by default
        txtSearch2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                btnSearch2.setEnabled(!txtSearch2.getText().toString().trim().equals(""));
            }
        });
        // call AsynTask to perform network operation on separate
        btnSearch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = txtSearch2.getText().toString().replace(" ", "+");
                String QueryURL = MainActivity.LMIforAllBaseURL + MainActivity.socSearchURL + "?q=" + query;
                new HttpAsyncTask().execute(QueryURL);
                startActivity(new Intent(start_search.this, MainActivity.class));
            }
        });

    }
}
