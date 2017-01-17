package com.example.srecko.agrohelper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
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

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

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
    private String className,date;
    private String [] tmpIzd;
    private  String tmpType;
    private static String data,scanContent;
   // private TextView contentTxt,txtDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcel_info);
        myApp = (AppAll) getApplication();
        editIme = (EditText) findViewById(R.id.editIme);
        editStevilka=(EditText) findViewById(R.id.editStevilka);
        imageTip = (ImageView) findViewById(R.id.imageTip);
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
            pos=intent.getIntExtra("Index",0);
            tmpType=intent.getStringExtra("Parcela tip");
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
            final SwipeDismissRecyclerViewTouchListener listener = new SwipeDismissRecyclerViewTouchListener.Builder(
                    mRecyclerView,
                    new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {
                        @Override
                        public boolean canDismiss(int position) {
                            return true;
                        }

                        @Override
                        public void onDismiss(View view) {
                          final int i= mRecyclerView.getChildLayoutPosition(view);
                            AlertDialog alert = new AlertDialog.Builder(ParcelInfoActivity.this)
                                    .setTitle("Opozorilo")
                                    .setMessage("Ali želite izbrisati izdelek: "+myApp.getParcela(pos).getIzdelek(i))
                                    .setCancelable(false)
                                    .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            myApp.getParcela(pos).deleteIzdelek(i);
                                            myApp.save();
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    })
                                    .setNegativeButton("Ne", null)
                                    .show();
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
                            if (tmpType.equals("POLJE")) {
                                Izdelek tt = myApp.getParcela(pos).getIzdelki().get(position);
                                AlertDialog alert = new AlertDialog.Builder(ParcelInfoActivity.this)
                                        .setTitle("Podatki o semenu")
                                        .setMessage("Naziv:" + tt.getNaziv() + "\nVrsta: " + tt.getVrsta() + "\nPotrebno presajanje: " + tt.getPresajanje() + "\nMesec sajenja: " + tt.getMesecSajenja() + "\nPrezimna vrsta: " + tt.getPrezimna() + "\nCas kaljenja: " + String.valueOf(tt.getCasKaljenja()) + "\nCas rasti: " + String.valueOf(tt.getCasRasti()) + "\nRast: " + tt.getRast() + ".")
                                        .setCancelable(false)
                                        .setPositiveButton("V redu", null)
                                        .show();

                            }
                        }
                    }).create();
            mRecyclerView.setOnTouchListener(listener);
        }
        else
        {
            tmp= new Parcela();
            fabAddIzdelek.setEnabled(false);
            fabAddIzdelek.setVisibility(View.INVISIBLE);
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
                    if(!editIme.getText().toString().equals("")&&!editStevilka.getText().toString().equals("")) {
                        tmp.setIme_parcele(editIme.getText().toString());
                        tmp.getParcelInfo().setStevilka(editStevilka.getText().toString());
                        myApp.addParcela(tmp);
                        Intent addLL = new Intent(ParcelInfoActivity.this, MapsActivity.class);
                        addLL.putExtra("Intent", "addLL");
                        startActivity(addLL);
                    }
                    else
                    {
                        AlertDialog alert = new AlertDialog.Builder(ParcelInfoActivity.this)
                                .setTitle("Opozorilo")
                                .setMessage("Niste vnesli imena in stevilke!")
                                .setCancelable(false)
                                .setPositiveButton("V redu",null)
                                .show();
                    }
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

    @Override
    public void onClick(View v) {
        if(tmpType.equals("POLJE")) {
            try {
                ArffLoader source = new ArffLoader();
                InputStream s = getAssets().open("projekt_1.arff");
                source.setSource(s);
                Instances data = source.getDataSet();
                if (data.classIndex() == -1)
                    data.setClassIndex(data.numAttributes() - 1);
                J48 tree = new J48();         // new instance of tree
                tree.buildClassifier(data);   // build classifier
                // 10-fold cross-validation
                Evaluation evaluation = new Evaluation(data);
                evaluation.crossValidateModel(tree, data, 10, new Debug.Random(1));
            } catch (Exception e) {
                Log.e("Error", e.toString());
            }
            if (v.getId() == R.id.fabAddIzdelek) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(this);
                scanIntegrator.initiateScan();
            }
        }
        else if(tmpType.equals("GOZD"))
        {
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            date = df.format(Calendar.getInstance().getTime());
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            AlertDialog alert = new AlertDialog.Builder(ParcelInfoActivity.this)
                    .setTitle("")
                    .setMessage("Vnesite m3: ")
                    .setView(input)
                    .setCancelable(false)
                    .setPositiveButton("V redu",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Izdelek izd = new Izdelek(input.getText().toString()+" m3",date);
                            myApp.getParcela(pos).addIzdelek(izd);
                            myApp.save();
                            mAdapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton("Prekliči",null)
                    .show();
        }
        else if(tmpType.equals("TRAVNIK"))
        {
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            date = df.format(Calendar.getInstance().getTime());
            final EditText input = new EditText(this);
            AlertDialog alert = new AlertDialog.Builder(ParcelInfoActivity.this)
                    .setTitle("")
                    .setMessage("Vnesite ime košnje: ")
                    .setView(input)
                    .setCancelable(false)
                    .setPositiveButton("V redu",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Izdelek izd = new Izdelek(input.getText().toString(),date);
                            myApp.getParcela(pos).addIzdelek(izd);
                            myApp.save();
                            mAdapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton("Prekliči",null)
                    .show();
        }
    }
    public class ServiceTask extends AsyncTask<Void, Void, Void> {
        private  String METHOD_NAME = "";
        private static final String NAMESPACE = "http://tempuri.org/";
        private static final String SOAP_ACTION = "http://tempuri.org/IService1/";
        private  String URL="http://192.168.0.21:99/WebService";
        protected void onPreExecute() {

        }
        protected Void doInBackground(Void... unused) {

            try
            {
                METHOD_NAME="getIzdelek";
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                request.addProperty("ID",scanContent);
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

                androidHttpTransport.call(SOAP_ACTION+"getIzdelek", envelope);
                SoapObject response=(SoapObject) envelope.bodyIn;
                data=response.getProperty(0).toString();

            }
            catch (Exception e)
            {
                Log.d("ERROR",e.getMessage());

            }
            return null;
        }

        protected void onPostExecute(Void unused) {
            tmpIzd=data.split(";");
            if(!data.equals("")) {

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
                            tmpIzd[2]+", "+tmpIzd[3]+", "+tmpIzd[6]+", "+tmpIzd[7]+", "+
                            tmpIzd[4]+", "+tmpIzd[5]+", ?";
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
                                .setMessage("Dodali boste naslednje seme: \n Naziv:"+tmpIzd[1]+"\nVrsta: "+tmpIzd[2]+"\nPotrebno presajanje: "+tmpIzd[3]+"\nMesec sajenja: "+tmpIzd[4]+"\nPrezimna vrsta: "+tmpIzd[5]+"\nCas kaljenja: "+tmpIzd[6]+"\nCas rasti: "+tmpIzd[7]+"\nRast: "+className +".\nAli želite dodati seme?")
                                .setCancelable(false)
                                .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Izdelek izd = new Izdelek(tmpIzd[1],date,tmpIzd[2],tmpIzd[3],tmpIzd[4],tmpIzd[5],className, Integer.parseInt(tmpIzd[6]),Integer.parseInt(tmpIzd[7]));
                                        myApp.getParcela(pos).addIzdelek(izd);
                                        myApp.save();
                                        mAdapter.notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("Ne", null)
                                .show();

                    }

                } catch (Exception e)
                {
                    Log.e("Error", e.toString());
                }
            }
            else
            {
                finish();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            date = df.format(Calendar.getInstance().getTime());
            scanContent = scanningResult.getContents();
            ServiceTask task = new ServiceTask();
            task.execute();
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
            mAdapter.notifyDataSetChanged();
        }
        else if(intTyp.equals("Add")) {
                    fabSave.setEnabled(true);
        }
    }
}
