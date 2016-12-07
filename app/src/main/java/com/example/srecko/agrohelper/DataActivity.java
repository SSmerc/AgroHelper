package com.example.srecko.agrohelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.app.AlertDialog;
import android.widget.ExpandableListView;
import android.widget.Toast;

import io.github.codefalling.recyclerviewswipedismiss.SwipeDismissRecyclerViewTouchListener;


public class DataActivity extends AppCompatActivity {
    private AppAll myApp;
   /* ExpandableListAdapter listAdapter;
    ExpandableListView expListView;*/
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myApp=(AppAll) getApplication();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent add= new Intent(DataActivity.this,ParcelInfoActivity.class);
                add.putExtra("Intent","Add");
                startActivity(add);
            }
        });
        /*expListView = (ExpandableListView) findViewById(R.id.expList);
        listAdapter = new ExpandableListAdapter(this,myApp.getAll(),this);
        expListView.setAdapter(listAdapter);
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();

        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });*/
        mRecyclerView=(RecyclerView) findViewById(R.id.recview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter(myApp.getAll(),this);
        mRecyclerView.setAdapter(mAdapter);
        SwipeDismissRecyclerViewTouchListener listener = new SwipeDismissRecyclerViewTouchListener.Builder(
                mRecyclerView,
                new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(int position) {
                        return true;
                    }

                    @Override
                    public void onDismiss(View view) {
                        // Do what you want when dismiss
                        Parcela tmp= myApp.getParcela(mRecyclerView.getChildAdapterPosition(view));
                        myApp.removeParcela(mRecyclerView.getChildAdapterPosition(view));
                        mAdapter.notifyDataSetChanged();
                        myApp.save();
                        showDialog(String.format("Izbrisali ste parcelo "+tmp.getIme_parcele()));
                    }
                })
                .setIsVertical(false)
                .setItemTouchCallback(
                        new SwipeDismissRecyclerViewTouchListener.OnItemTouchCallBack() {
                            @Override
                            public void onTouch(int index) {
                                // Do what you want when item be touched

                            }
                        })
                .setItemClickCallback(new SwipeDismissRecyclerViewTouchListener.OnItemClickCallBack() {
                    @Override
                    public void onClick(int position) {
                        // Do what you want when item be clicked
                            Intent dva = new Intent(DataActivity.this, ParcelInfoActivity.class);
                            dva.putExtra("Parcela ime",myApp.getParcela(position).getIme_parcele());
                            dva.putExtra("Parcela tip",myApp.getParcela(position).getTip().toString());
                            dva.putExtra("Index",position);
                            dva.putExtra("Intent","Info");
                            startActivity(dva);
                    }
                    }).create();
                    mRecyclerView.setOnTouchListener(listener);
    }

    private void showDialog(String msg){
        AlertDialog alert = new AlertDialog.Builder(DataActivity.this)
                .setTitle("Opozorilo")
                .setMessage(msg)
                .setCancelable(false)
                .create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
        //listAdapter.notifyDataSetChanged();
    }
  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_list, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_logout) {
            Intent i = new Intent(ActivityMainList.this, ActivitySignIn.class);
            i.putExtra(ActivitySignIn.LOGOUT, true);
            startActivity(i);
            ActivityMainList.this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
