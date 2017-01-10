package com.example.srecko.agrohelper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Debug;
import weka.core.converters.ArffLoader;

import weka.core.Instances;

import io.github.codefalling.recyclerviewswipedismiss.SwipeDismissRecyclerViewTouchListener;

public class ParcelInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private Intent intent;
    private int pos;
    private EditText editIme,editStevilka;
    private AppAll myApp;
    private Spinner spinToWin;
    private ImageView imageTip;
    private FloatingActionButton  fabSave,fabAddIzdelek,fabMap;
    private String intTyp;
    private Parcela tmp;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String name,pres,mesec,prez,rast,vrsta,className,date;
    private int casR,casK;
   // private TextView contentTxt,txtDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcel_info);
        myApp = (AppAll) getApplication();
        editIme = (EditText) findViewById(R.id.editIme);
        editStevilka=(EditText) findViewById(R.id.editStevilka);
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
                    myApp.getParcela(pos).getParcelInfo().setStevilka(editStevilka.getText().toString());
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
                            imageTip.setImageResource(R.drawable.gozd);
                            break;
                        case 1:
                            myApp.getParcela(pos).setTip(Tip_parcele.POLJE);
                            imageTip.setImageResource(R.drawable.polje);
                            break;
                        case 2:
                            myApp.getParcela(pos).setTip(Tip_parcele.TRAVNIK);
                            imageTip.setImageResource(R.drawable.travnik);
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
                    tmp.getParcelInfo().setStevilka(editStevilka.getText().toString());
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
                            imageTip.setImageResource(R.drawable.gozd);
                            break;
                        case 1:
                           tmp.setTip(Tip_parcele.POLJE);
                            imageTip.setImageResource(R.drawable.polje);
                            break;
                        case 2:
                            tmp.setTip(Tip_parcele.TRAVNIK);
                            imageTip.setImageResource(R.drawable.travnik);
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
        try {
            ArffLoader source = new ArffLoader();
            InputStream s= getAssets().open("projekt_1.arff");
            source.setSource(s);
            Instances data = source.getDataSet();
            if (data.classIndex() == -1)
                data.setClassIndex(data.numAttributes() - 1);
            J48 tree = new J48();         // new instance of tree
            tree.buildClassifier(data);   // build classifier
            // 10-fold cross-validation
            Evaluation evaluation = new Evaluation(data);
            evaluation.crossValidateModel(tree, data, 10, new Debug.Random(1));
        } catch (Exception e)
        {
            Log.e("Error", e.toString());
        }
        if(v.getId()==R.id.fabAddIzdelek){
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            DateFormat df= new SimpleDateFormat("dd.MM.yyyy");
            date=df.format(Calendar.getInstance().getTime());
            String scanContent = scanningResult.getContents();
            /*ServiceTask task = new ServiceTask();
            task.execute(scanContent,name);*/
            switch(scanContent) {
                case "1":name="Solata - Majska Kraljica";
                    pres="DA";
                    vrsta="Solatnica";
                    mesec="Marec";
                    prez="NE";
                    casK=35;
                    casR=90;
                    break;
                case "2":name="Cebula";
                    pres="NE";
                    mesec="Maj";
                    vrsta="Cebulnica";
                    prez="NE";
                    casK=30;
                    casR=80;
                    break;
                case "3":name="Korenje - rumeno";
                    pres="NE";
                    mesec="April";
                    vrsta="Korenovka";
                    prez="NE";
                    casK=18;
                    casR=88;
                    break;
                case "4":name="Rdeča pesa";
                    pres="DA";
                    mesec="Junij";
                    vrsta="Korenovka";
                    prez="NE";
                    casK=20;
                    casR=100;
                    break;
                case "5":name="Brokoli";
                    pres="NE";
                    mesec="Avgust";
                    vrsta="Kapusnica";
                    prez="NE";
                    casK=10;
                    casR=120;
                    break;
                case "6":name="Radič";
                    pres="DA";
                    mesec="Marec";
                    vrsta="Solatnica";
                    prez="NE";
                    casK=24;
                    casR=50;
                    break;
                case "7":name="Bučke";
                    pres="NE";
                    mesec="Junij";
                    vrsta="Plodovka";
                    prez="DA";
                    casK=50;
                    casR=180;
                    break;
            }

            if(!name.equals("")) {

                try {
                    String temp="";

                    temp="@relation AgroHelper-weka.filters.unsupervised.attribute.Remove-R1 \n"+
                            "@attribute VRSTA {Kapusnica,Korenovka,Solatnica,Spinacnica,Cebulnica,Plodovka,Strocnica,Zito}\n"+
                            "@attribute 'POTREBNO PRESAJANJE' {DA,NE}\n"+
                            "@attribute 'CAS KALJENJA' numeric\n"+
                            "@attribute 'CAS RASTI' numeric\n"+
                            "@attribute 'PRIMERNI MESEC SAJENJA' {Januar,Februar,Marec,April,Maj,Junij,Julij,Avgust,September,Oktober,November,December}\n"+
                            "@attribute 'PREZIMNA VRSTA' {DA,NE}\n"+
                            "@attribute RAZRED {'Hitra rast','Srednje hitra rast','Pocasna rast'}\n"+

                            "@data\n" +
                            vrsta+", "+pres+", "+String.valueOf(casK)+", "+String.valueOf(casR)+", "+
                            mesec+", "+prez+", ?";
                    InputStream stream = new ByteArrayInputStream(temp.getBytes("UTF-8"));
                    ArffLoader loader=new ArffLoader();
                    loader.setSource(stream);
                    Instances testData=loader.getDataSet();
                    testData.setClassIndex(testData.numAttributes()-1);
                    ArffLoader source = new ArffLoader();
                    InputStream s= getAssets().open("projekt_1.arff");
                    source.setSource(s);
                    Instances data = source.getDataSet();
                    if (data.classIndex() == -1)
                        data.setClassIndex(data.numAttributes() - 1);
                    J48 tree = new J48();         // new instance of tree
                    tree.buildClassifier(data);   // build classifier
                    // 10-fold cross-validation
                    Evaluation evaluation = new Evaluation(data);
                    evaluation.crossValidateModel(tree, data, 10, new Debug.Random(1));

                    tree.buildClassifier(data);

                    for(int i=0;i<testData.numInstances();i++){
                        double index = tree.classifyInstance(testData.instance(i));
                        className = data.attribute(data.numAttributes()-1).value((int)index);
                        AlertDialog alert = new AlertDialog.Builder(ParcelInfoActivity.this)
                                .setTitle("Opozorilo")
                                .setMessage("Seme ima naslednjo rast: "+className +". Ali želite dodati seme?")
                                .setCancelable(false)
                                .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Izdelek izd = new Izdelek(name,date,vrsta,pres,mesec,prez,className,casK,casR);
                                        myApp.getParcela(pos).addIzdelek(izd);
                                        myApp.save();
                                    }
                                })
                                .setNegativeButton("Ne", null)
                                .show();

                    }

                } catch (Exception e)
                {
                    Log.e("Error", e.toString());
                }

               // contentTxt.setText("Izdelek: " + name);
                //txtDate.setText("Datum:"+date);

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
            String stevilka= intent.getStringExtra("Stevilka");
            pos=intent.getIntExtra("Index",0);
            editIme.setText(ime);
            editStevilka.setText(stevilka);
            switch (tip)
            {
                case "GOZD":
                    spinToWin.setSelection(0);
                    imageTip.setImageResource(R.drawable.gozd);
                    break;
                case "POLJE":
                    spinToWin.setSelection(1);
                    imageTip.setImageResource(R.drawable.polje);
                    break;
                case "TRAVNIK":
                    spinToWin.setSelection(2);
                    imageTip.setImageResource(R.drawable.travnik);
                    break;
            }
        }
        else if(intTyp.equals("Add")) {
            //if(myApp.size()!=0)
              //  if(myApp.getParcela(myApp.size()-1).getParcelaLatLng()!=null)
                    fabSave.setEnabled(true);
        }
    }
}
