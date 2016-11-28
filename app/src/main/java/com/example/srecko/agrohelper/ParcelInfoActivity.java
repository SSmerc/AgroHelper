package com.example.srecko.agrohelper;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import io.github.codefalling.recyclerviewswipedismiss.SwipeDismissRecyclerViewTouchListener;

public class ParcelInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private Intent intent;
    private int pos;
    private EditText editIme;
    private AppAll myApp;
    private Spinner spinToWin;
    private ImageView imageTip;
    private FloatingActionButton  fabSave,fabAddIzdelek,fabMap;
    private String intTyp;
    private Parcela tmp;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
   // private TextView contentTxt,txtDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcel_info);
        myApp = (AppAll) getApplication();
        editIme = (EditText) findViewById(R.id.editIme);
        imageTip = (ImageView) findViewById(R.id.imageTip);
       // btnSave = (Button) findViewById(R.id.buttonSave);
       // btnShowMap = (Button) findViewById(R.id.buttonShowMap);
        spinToWin = (Spinner) findViewById(R.id.tipSpin);
        fabAddIzdelek = (FloatingActionButton) findViewById(R.id.fabAddIzdelek);
        fabSave = (FloatingActionButton) findViewById(R.id.fabSave);
        fabMap = (FloatingActionButton) findViewById(R.id.fabShowMap);
        String[] items = new String[]{"GOZD", "POLJE", "TRAVNIK"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinToWin.setAdapter(adapter);
        if (intent == null) {
            intent = getIntent();
            intTyp = intent.getStringExtra("Intent");
        }
        if (intTyp.equals("Info")) {
            //btn save
            fabSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myApp.getParcela(pos).setIme_parcele(editIme.getText().toString());
                    myApp.save();
                    finish();
                }
            });
            //btnShowMap
            fabMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent map = new Intent(ParcelInfoActivity.this, MapsActivity.class);
                    map.putExtra("Intent", "ShowDraw");
                    map.putExtra("Index", pos);
                    startActivity(map);
                }
            });
            //btnAddPlant
           // btnAddPlant = (Button)findViewById(R.id.buttonAddPlant);
            //contentTxt = (TextView)findViewById(R.id.txtContent);
           // txtDate = (TextView)findViewById(R.id.txtDate);
            fabAddIzdelek.setOnClickListener(this);
            spinToWin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            myApp.getParcela(pos).setTip(Tip_parcele.GOZD);
                            break;
                        case 1:
                            myApp.getParcela(pos).setTip(Tip_parcele.POLJE);
                            break;
                        case 2:
                            myApp.getParcela(pos).setTip(Tip_parcele.TRAVNIK);
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            mRecyclerView=(RecyclerView) findViewById(R.id.recviewIzdelki);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mAdapter = new MyAdapterIzdelki(myApp.getAll(),this,pos);
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
                            /*ArrayList<Izdelek> tmp= myApp.getParcela(mRecyclerView.getChildAdapterPosition(view)).getIzdelki();
                            myApp.removeParcela(mRecyclerView.getChildAdapterPosition(view));
                            mAdapter.notifyDataSetChanged();
                            myApp.save();
                            showDialog(String.format("Izbrisali ste izdelek "+tmp.getIme_parcele()));*/
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

                        }
                    }).create();
            mRecyclerView.setOnTouchListener(listener);
        }
        else
        {
            tmp= new Parcela();
            fabAddIzdelek.setEnabled(false);
            fabAddIzdelek.setVisibility(View.INVISIBLE);
            /*btnSave.setText("Shrani novo parcelo");
            btnSave.setEnabled(false);
            btnShowMap.setText("Vnesi koordinate");*/
            fabSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   myApp.save();
                    finish();
                }
            });
            fabMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tmp.setIme_parcele(editIme.getText().toString());
                    myApp.addParcela(tmp);
                    Intent addLL= new Intent(ParcelInfoActivity.this,MapsActivity.class);
                    addLL.putExtra("Intent","addLL");
                    startActivity(addLL);
                }
            });
            spinToWin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                           tmp.setTip(Tip_parcele.GOZD);
                            break;
                        case 1:
                           tmp.setTip(Tip_parcele.POLJE);
                            break;
                        case 2:
                            tmp.setTip(Tip_parcele.TRAVNIK);
                            break;
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }

    private void showDialog(String msg){
        AlertDialog alert = new AlertDialog.Builder(ParcelInfoActivity.this)
                .setTitle("Opozorilo")
                .setMessage(msg)
                .setCancelable(false)
                .create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.fabAddIzdelek){
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String name="";
            DateFormat df= new SimpleDateFormat("dd.MM.yyyy");
            String date=df.format(Calendar.getInstance().getTime());
            String scanContent = scanningResult.getContents();
            /*ServiceTask task = new ServiceTask();
            task.execute(scanContent,name);*/
            switch(scanContent) {
                case "9501101530003":name="Solata - Majska Kraljica";
                    break;
                case "9310779300005":name="Redkvica- Ledena sveča";
                    break;
                case "5052964056208":name="Korenje - rumeno";
                    break;
                case "784672659826":name="Rdeča pesa";
                    break;
                case "123456789012":name="Brokoli";
                    break;
                case "9771234567003":name="Radič";
                    break;
                case "1234567890128":name="Bučke";
                    break;
            }

            if(!name.equals("")) {
                Izdelek i = new Izdelek();
                i.setNaziv(name);
                i.setDatum(date);
                myApp.getParcela(pos).addIzdelek(i);
               // contentTxt.setText("Izdelek: " + name);
                //txtDate.setText("Datum:"+date);
                myApp.save();
            }
            else if(scanningResult==null)
            {
                finish();
            }

        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(intent==null) {
            intent = getIntent();
            intTyp=intent.getStringExtra("Intent");
        }
        else if (intent!=null&&intTyp.equals("Info")) {
            String ime= intent.getStringExtra("Parcela ime");
            String tip= intent.getStringExtra("Parcela tip");
            pos=intent.getIntExtra("Index",0);
            editIme.setText(ime);
            switch (tip)
            {
                case "GOZD":
                    imageTip.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.gozd,null));
                    spinToWin.setSelection(0);
                    break;
                case "POLJE":
                    imageTip.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.polje,null));
                    spinToWin.setSelection(1);
                    break;
                case "TRAVNIK":
                    imageTip.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.travnik,null));
                    spinToWin.setSelection(2);
                    break;
            }
        }
        else if(intTyp.equals("Add")) {
            if(myApp.getParcela(myApp.size()-1).getParcelaLatLng()!=null)
                fabSave.setEnabled(true);
        }
    }
}
