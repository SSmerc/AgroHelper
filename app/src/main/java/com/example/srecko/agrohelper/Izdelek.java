package com.example.srecko.agrohelper;

/**
 * Created by Srečko on 6. 06. 2016.
 */
public class Izdelek {
    private String naziv;
    private String datum;

    public Izdelek()
    {
        naziv="";
        datum="";
    }
    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
}
