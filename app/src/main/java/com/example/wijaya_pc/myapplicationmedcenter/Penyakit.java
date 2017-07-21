package com.example.wijaya_pc.myapplicationmedcenter;

/**
 * Created by Wijaya_PC on 01-Jul-17.
 */

import android.provider.BaseColumns;
import android.widget.Filter;
import android.widget.Filterable;

import java.io.Serializable;

public class Penyakit implements Serializable, BaseColumns {
    public static final int RINGAN = 1;
    public static final int SEDANG = 2;
    public static final int BERAT = 3;
    public static final int PARAH = 4;

    private String id;
    private String name;
    private String description;
    private String gejala;
    private String obat;
    private int type;

    public Penyakit(String id, String name, String description, String gejala, String obat, int type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.gejala = gejala;
        this.obat = obat;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGejala() {
        return gejala;
    }

    public void setGejala(String gejala) {
        this.gejala = gejala;
    }

    public String getObat() {
        return obat;
    }

    public void setObat(String obat) {
        this.obat = obat;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String stringType(){
        if (this.type == RINGAN)
            return "Ringan";
        else if (this.type == SEDANG)
            return "Sedang";
        else if (this.type == BERAT)
            return "Berat";
        else
            return "Parah";
    }

    @Override
    public String toString() {
        return this.name+" ["+this.stringType()+"] ";
    }
}
