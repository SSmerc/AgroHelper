package com.example.srecko.agrohelper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;



public class DataActivity extends AppCompatActivity {
    private AppAll myApp;
    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myApp = (AppAll) getApplication();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent add = new Intent(DataActivity.this, ParcelInfoActivity.class);
                add.putExtra("Intent", "Add");
                startActivity(add);
            }
        });

        expListView = (ExpandableListView) findViewById(R.id.expList);
        listAdapter = new ExpandableListAdapter(this, myApp.getAll(), this);
        expListView.setAdapter(listAdapter);
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return false;
            }
        });
        expListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                int itemType = ExpandableListView.getPackedPositionType(l);
                if (itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    final int groupPosition = i;
                    //do your per-group callback here
                    AlertDialog alert = new AlertDialog.Builder(DataActivity.this)
                            .setTitle("Opozorilo")
                            .setMessage("Ali zelite izbrisati parcelo: "+myApp.getParcela(groupPosition).getIme_parcele())
                            .setCancelable(false)
                            .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    listAdapter.delete(groupPosition);
                                    listAdapter.notifyDataSetChanged();
                                    myApp.removeParcela(groupPosition);
                                    myApp.save();
                                }
                            })
                            .setNegativeButton("Ne", null)
                            .show();
                    return true; //true if we consumed the click, false if not

                } else {
                    // null item; we don't consume the click
                    return false;
                }
            }
        });
        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub

                return true;
            }
        });
    }
     private void showDialog(String msg){

    }

    @Override
    protected void onResume() {
        super.onResume();
        listAdapter.notifyDataSetChanged();
    }
}
