package com.example.srecko.agrohelper;

import android.graphics.Point;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Srečko on 20. 04. 2016.
 */

public class Parcela {
    private String ime_parcele;
    private ArrayList<LatLng> parcela;
    private Tip_parcele tip;
    private ArrayList<Izdelek> izdelki;
    private ParcelInfo pi;



    public Parcela()
    {
        ime_parcele="Nova parcela";
       parcela= new ArrayList<>();
        izdelki=new ArrayList<>();
        tip=Tip_parcele.POLJE;
        pi= new ParcelInfo();

    }

    public void dodaj(LatLng p)
    {
        parcela.add(p);
    }

    public ArrayList<Izdelek> getIzdelki() {
        return izdelki;
    }

    public void setIzdelki(ArrayList<Izdelek> izdelki) {
        this.izdelki = izdelki;
    }
  /*  public void izbrisiParcelo()
    {
        parcela.clear();
        ime_parcele=null;

        tip=null;
    }*/

    public String getIme_parcele() {
        return ime_parcele;
    }

    public void setIme_parcele(String ime_parcele) {
        this.ime_parcele = ime_parcele;
    }

    public Tip_parcele getTip() {
        return tip;
    }

    public void setTip(Tip_parcele tip) {
        this.tip = tip;
    }

    public ArrayList<LatLng> getParcelaLatLng() {
        return parcela;
    }

    public void setParcela(ArrayList<LatLng> parcela) {
        this.parcela = parcela;
    }

    public Parcela(String ime_parcele, ArrayList<LatLng> parcela, Tip_parcele tip, ParcelInfo pi) {
        this.ime_parcele = ime_parcele;
        this.parcela = parcela;
        this.tip = tip;
        this.pi=pi;
    }
    public ParcelInfo getParcelInfo(){return pi;}
    public void addIzdelek(Izdelek i)
    {
        izdelki.add(i);
    }
    public int sizeIzdelki(){
        if(izdelki.size()!=0)
            return izdelki.size();
        else
            return 0;
    }
}
