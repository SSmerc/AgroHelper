package com.example.srecko.agrohelper;

import android.provider.ContactsContract;

import com.google.android.gms.drive.realtime.internal.event.ParcelableEventList;

import java.util.ArrayList;

/**
 * Created by Srečko on 20. 04. 2016.
 */
public class Oseba {
    private String ime, priimek, naslov;
    //private ContactsContract.CommonDataKinds.Email mail;
    private ArrayList<Parcela> parcele;

    public Oseba(String ime, String priimek, String naslov) {
        this.ime = ime;
        this.priimek = priimek;
        this.naslov = naslov;
        //this.mail = mail;
        this.parcele = new ArrayList<>();
    }

    private  void izbrisiVseParcele()
    {
        parcele.clear();
    }

    private void dodajParcelo(Parcela p)
    {
        parcele.add(p);
    }
    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPriimek() {
        return priimek;
    }

    public void setPriimek(String priimek) {
        this.priimek = priimek;
    }

    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }

  /*  public ContactsContract.CommonDataKinds.Email getMail() {
        return mail;
    }

    public void setMail(ContactsContract.CommonDataKinds.Email mail) {
        this.mail = mail;
    }*/
}
