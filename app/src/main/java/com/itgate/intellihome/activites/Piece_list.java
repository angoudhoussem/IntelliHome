package com.itgate.intellihome.activites;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.itgate.intellihome.R;
import com.itgate.intellihome.expandblelist.ExpandableListAdapter;
import com.itgate.intellihome.expandblelist.item;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Piece_list extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<item>> listDataChild;

    private int lastExpandedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_piece_list);

        // preparing list data
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<String, List<item>>();

        // Adding child data
        listDataHeader.add("Livingroom");
        listDataHeader.add("Kitchen");
        listDataHeader.add("Bathroom");
        listDataHeader.add("Bedroom");

        // Adding child data
        List<item> Livingroom = new ArrayList<item>();
        Livingroom.add(new item("Air Conditioner",R.drawable.ac));
        Livingroom.add(new item("Light bulb",R.drawable.lightbulb));
        Livingroom.add(new item("TV", R.drawable.tv));

        List<item> Kitchen = new ArrayList<item>();
        Kitchen.add(new item("Light bulb", R.drawable.lightbulb));

        List<item> Bedroom = new ArrayList<item>();
        Bedroom.add(new item("Light bulb", R.drawable.lightbulb));
        Bedroom.add(new item("Personal Computer", R.drawable.pcomputer));
        Bedroom.add(new item("CD/DVD Player", R.drawable.cddvdplayer));

        List<item> Bathroom = new ArrayList<item>();
        Bathroom.add(new item("Light bulb", R.drawable.lightbulb));

        listDataChild.put(listDataHeader.get(0), Livingroom); // Header, Child data
        listDataChild.put(listDataHeader.get(1), Kitchen);
        listDataChild.put(listDataHeader.get(2), Bathroom);
        listDataChild.put(listDataHeader.get(3), Bedroom);

        expListView = (ExpandableListView) findViewById(R.id.list);
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
        // expListView.expandGroup(0);
        // expListView.expandGroup(1);
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                item s = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                Toast.makeText(getApplicationContext(), s.Name, Toast.LENGTH_LONG).show();
                return true;
            }
        });

        /** begin of making an Device_item close when opening another */

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });

        /** end of making an Device_item close when opening another */

    }
}
