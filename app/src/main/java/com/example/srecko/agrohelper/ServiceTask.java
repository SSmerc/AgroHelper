package com.example.srecko.agrohelper;

import android.os.AsyncTask;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by Srečko on 5. 06. 2016.
 */


public class ServiceTask extends AsyncTask<String, Void, String> {
    private static String METHOD_NAME = "";
    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String SOAP_ACTION = "http://tempuri.org/IService/";
    private static String URL="http://192.168.0.17:25877/Service1.svc";
    protected void onPreExecute() {

    }
    protected String doInBackground(String...strings) {

    try
    {
        METHOD_NAME="getIzdelek";//IME METODE
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        String s = strings[0];
        request.addProperty("ID",s);
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        androidHttpTransport.call(SOAP_ACTION+METHOD_NAME, envelope);//IME METODE
        SoapObject response=(SoapObject) envelope.bodyIn;
        String data=response.getProperty(0).toString();//NEKE VRSTE ODGOVOR
        return data;
    }
    catch (Exception e)
    {
     Log.d("ERROR",e.getMessage());
    }
     return null;
    }

    protected void onPostExecute(Void unused) {
    }
}
