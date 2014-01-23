package com.SECDUST.jobconnnection;

import android.app.Activity;
import android.widget.ExpandableListView;
import android.widget.Spinner;

import java.util.ArrayList;


public class GlobalVars extends Activity {
    static ExpandableListView Details;
    static ArrayList<String> SavedTitleArrayList = new ArrayList<String>();
    static ArrayList<String> SavedDescArrayList = new ArrayList<String>();
    static ArrayList<String> SavedQualArrayList = new ArrayList<String>();
    static ArrayList<String> SavedTaskArrayList = new ArrayList<String>();
    static ArrayList<Integer> SavedSocArrayList = new ArrayList<Integer>();
    static Spinner sepresult;
    static ExpandListAdapter ExpAdapter;
    static ArrayList<ExpandListGroup> ExpListItems;
    static ExpandableListView ExpandList;

    public static void setArrays(ArrayList<String> DescArrayList, ArrayList<String> QualArrayList, ArrayList<String> TaskArrayList, ArrayList<Integer> SocArrayList, ArrayList<String> TitleArrayList) {
        SavedDescArrayList = DescArrayList;
        SavedQualArrayList = QualArrayList;
        SavedTaskArrayList = TaskArrayList;
        SavedSocArrayList = SocArrayList;
        SavedTitleArrayList = TitleArrayList;
    }

    public static void getSepResult(Spinner spin) {
        sepresult = spin;
    }

    public static Spinner setSpinner(Spinner Spin) {
        Spin = sepresult;
        return sepresult;
    }
    public static void getDetails(ExpandableListView ExpList){
        Details = ExpList;
    }
    public static ExpandableListView setExpandableListVieew(ExpandableListView ExpList){
        ExpList = Details;
        return ExpList;
    }

}
