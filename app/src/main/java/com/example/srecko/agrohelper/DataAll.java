package com.example.srecko.agrohelper;

import android.graphics.Point;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Created by Srečko on 20. 04. 2016.
 */
public class DataAll implements Serializable{

    private ArrayList<Parcela> parcele;
    public DataAll() {
        this.parcele = new ArrayList<>();
    }
    public  void izbrisiVseParcele()
    {
        parcele.clear();
    }
    public void dodajParcelo(Parcela p)
    {
        parcele.add(p);
    }
    public Parcela vrniParcelo(int index)
    {
        return parcele.get(index);
    }
    public void izbrisiParcelo(int i){parcele.remove(i);}
    public int steviloParcel()
    {
        return parcele.size();
    }
    public ParcelInfo vrniParcelInfo(int index)
    {
        return parcele.get(index).getParcelInfo();
    }

    public void setData()
    {
        ArrayList<LatLng> par1 = new ArrayList<>();
        par1.add(new LatLng(46.154421f,15.331252f));
        par1.add(new LatLng(46.154550f,15.332529f));
        par1.add(new LatLng(46.154193f,15.332556f));
        par1.add(new LatLng(46.154135f,15.331747f));
        par1.add(new LatLng(46.154219f,15.331713f));
        par1.add(new LatLng(46.154172f,15.331317f));
        ArrayList<LatLng> par2 = new ArrayList<>();
        par2.add(new LatLng(46.154889f,15.328036f));
        par2.add(new LatLng(46.154746f,15.328421f));
        par2.add(new LatLng(46.154211f,15.327862f));
        par2.add(new LatLng(46.154343f,15.327460f));
        ArrayList<LatLng> par3 = new ArrayList<>();
        par3.add(new LatLng(46.153874f,15.330418f));
        par3.add(new LatLng(46.153777f,15.329938f));
        par3.add(new LatLng(46.153596f,15.329619f));
        par3.add(new LatLng(46.153839f,15.329052f));
        par3.add(new LatLng(46.154277f,15.329675f));
        par3.add(new LatLng(46.154150f,15.329766f));
        par3.add(new LatLng(46.154023f,15.329969f));
        par3.add(new LatLng(46.154013f,15.330372f));
        ArrayList<LatLng> par4 = new ArrayList<>();
        par4.add(new LatLng(46.154496f,15.333103f));
        par4.add(new LatLng(46.155375f,15.333474f));
        par4.add(new LatLng(46.155531f,15.332843f));
        par4.add(new LatLng(46.155253f,15.332665f));
        par4.add(new LatLng(46.155188f,15.333181f));
        ArrayList<LatLng> par5 = new ArrayList<>();
        par5.add(new LatLng(46.154011f,15.332605f));
        par5.add(new LatLng(46.154552f,15.332539f));
        par5.add(new LatLng(46.154456f,15.333131f));
        par5.add(new LatLng(46.154409f,15.333625f));
        par5.add(new LatLng(46.154355f,15.333652f));
        par5.add(new LatLng(46.153956f,15.333423f));
        par5.add(new LatLng(46.153972f,15.333071f));
        parcele=new ArrayList<>();
        ParcelInfo p1=new ParcelInfo(200,"1435");
        ParcelInfo p2=new ParcelInfo(150,"1436");
        ParcelInfo p3=new ParcelInfo(2000,"1437");
        ParcelInfo p4=new ParcelInfo(1354.4,"1438");
        ParcelInfo p5=new ParcelInfo(4651.7,"1439");
        Parcela o1 = new Parcela("Njiva za podom",par1,Tip_parcele.POLJE,p1);
        Parcela o2 = new Parcela("Njiva nad sosedom",par2,Tip_parcele.POLJE,p2);
        Parcela o3 = new Parcela("Lipekov",par3,Tip_parcele.TRAVNIK,p3);
        Parcela o4 = new Parcela("Grički gozd",par4,Tip_parcele.GOZD,p4);
        Parcela o5 = new Parcela("Punkl",par5,Tip_parcele.TRAVNIK,p5);
        dodajParcelo(o1);
        dodajParcelo(o2);
        dodajParcelo(o3);
        dodajParcelo(o4);
        dodajParcelo(o5);
    }
   /* private ArrayList<Oseba> osebe;

    private DataAll()
    {
        osebe= new ArrayList<>();
    }
    private void dodajOsebo(Oseba o)
    {
        osebe.add(o);
    }


    public Oseba vrniOsebo(int index)
    {
        return osebe.get(index);
    }

    public int steviloOseb()
    {
        return osebe.size();
    }*/
}
