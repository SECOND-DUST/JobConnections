package com.SECDUST.jobconnnection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Chad on 23/01/14.
 */
public class MyAdapter extends ArrayAdapter<String> {

    public MyAdapter(Context ctx, int txtViewResourceId, String[] objects) {
        super(ctx, txtViewResourceId, objects);
    }

    @Override
    public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
        return getCustomView(position, cnvtView, prnt);
    }

    @Override
    public View getView(int pos, View cnvtView, ViewGroup prnt) {
        return getCustomView(pos, cnvtView, prnt);
    }

    public View getCustomView(int position, View convertView,
                              ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) MainActivity.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mySpinner = inflater.inflate(R.layout.custom_spinner, parent,
                false);
        TextView main_text = null;
        if (mySpinner != null) {
            main_text = (TextView) mySpinner
                    .findViewById(R.id.text_main_seen);
            main_text.setText(GlobalVars.SavedTitleArrayList.get(position));
        }


        return mySpinner;
    }
}
