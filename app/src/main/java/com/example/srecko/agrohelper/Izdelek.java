package com.example.srecko.agrohelper;

/**
 * Created by Srečko on 6. 06. 2016.
 */
public class Izdelek {
    private String naziv;
    private String datum;
    private String vrsta;
    private String presajanje;
    private String mesecSajenja;
    private String prezimna;
    private String rast;
    private int casKaljenja;
    private int casRasti;

    public Izdelek() {
        naziv = datum=vrsta=presajanje=mesecSajenja=prezimna=rast="";
        casKaljenja=casRasti=0;

       /* vrsta = new String[]{"Kapusnica", "Korenovka", "Solatnica", "Spinacnica", "Cebulnica", "Plodovka", "Strocnica", "Zito"};
        presajanje = new String[]{"DA", "NE"};
        prezimna = new String[]{"DA", "NE"};
        meseciSajenja = new String[]{"Januar", "Februar", "Marec", "April", "Maj", "Junij", "Julij", "Avgust", "September", "Oktober", "November", "December"};
        casKaljenja = 0;
        casKaljenja = 0;
        rast = new String[]{"Hitra rast", "Srednje hitra rast", "Pocasna rast"};*/
    }

    public Izdelek(String naziv, String datum, String vrsta, String presajanje, String mesecSajenja, String prezimna, String rast, int casKaljenja, int casRasti) {
        this.naziv = naziv;
        this.datum = datum;
        this.vrsta = vrsta;
        this.presajanje = presajanje;
        this.mesecSajenja = mesecSajenja;
        this.prezimna = prezimna;
        this.rast = rast;
        this.casKaljenja = casKaljenja;
        this.casRasti = casRasti;
    }

    public String getVrsta() {
        return vrsta;
    }

    public void setVrsta(String vrsta) {
        this.vrsta = vrsta;
    }

    public String getPresajanje() {
        return presajanje;
    }

    public void setPresajanje(String presajanje) {
        this.presajanje = presajanje;
    }

    public String getMesecSajenja() {
        return mesecSajenja;
    }

    public void setMesecSajenja(String mesecSajenja) {
        this.mesecSajenja = mesecSajenja;
    }

    public String getPrezimna() {
        return prezimna;
    }

    public void setPrezimna(String prezimna) {
        this.prezimna = prezimna;
    }

    public String getRast() {
        return rast;
    }

    public void setRast(String rast) {
        this.rast = rast;
    }

    public String getDatum() {
        return datum;
    }

    /*public Izdelek(String naziv, String datum, int vrsta, int presajanje, int meseciSajenja, int prezimna, int r, int casKaljenja, int casRasti) {
        this.naziv = naziv;
        this.datum = datum;
        this.vrsta = vrsta[];
        this.presajanje = presajanje;
        this.meseciSajenja = meseciSajenja;
        this.prezimna = this.prezimna;
        this.rast =;
        this.casKaljenja = casKaljenja;
        this.casRasti = casRasti;
    }*/


    public void setDatum(String datum) {
        this.datum = datum;
    }

    public int getCasRasti() {
        return casRasti;
    }

    public void setCasRasti(int casRasti) {
        this.casRasti = casRasti;
    }

    public int getCasKaljenja() {
        return casKaljenja;
    }

    public void setCasKaljenja(int casKaljenja) {
        this.casKaljenja = casKaljenja;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
}
