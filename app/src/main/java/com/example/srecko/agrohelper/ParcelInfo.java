package com.example.srecko.agrohelper;

/**
 * Created by Heavy on 7. 12. 2016.
 */

public class ParcelInfo {
    private double povrsina;
    private String stevilka;

    public double getPovrsina() {
        return povrsina;
    }

    public void setPovrsina(double povrsina) {
        this.povrsina = povrsina;
    }

    public String getStevilka() {
        return stevilka;
    }

    public void setStevilka(String stevilka) {
        this.stevilka = stevilka;
    }

    public ParcelInfo() {
        povrsina = 0;
        stevilka = "";
    }
    public ParcelInfo(double povrsina, String stevilka) {
        this.povrsina = povrsina;
        this.stevilka = stevilka;
    }
}
