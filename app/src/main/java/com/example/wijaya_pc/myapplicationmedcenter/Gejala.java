package com.example.wijaya_pc.myapplicationmedcenter;

/**
 * Created by andre on 19-Jul-17.
 */

public class Gejala {
    public static final int RINGAN = 1;
    public static final int SEDANG = 2;
    public static final int BERAT = 3;
    public static final int PARAH = 4;

    private String gejalaId;
    private String isiGejala;
    private String namaPenyakit;
    private String idPenyakit;
    private String descriptionPenyakit;
    private String gejalaPenyakit;
    private String obatPenyakit;
    private int typePenyakit;

    public Gejala(){

    }

    public Gejala(String gejalaId, String isiGejala, String namaPenyakit, String idPenyakit, String descriptionPenyakit, String gejalaPenyakit, String obatPenyakit, int typePenyakit) {
        this.gejalaId = gejalaId;
        this.isiGejala = isiGejala;
        this.namaPenyakit = namaPenyakit;
        this.idPenyakit = idPenyakit;
        this.descriptionPenyakit = descriptionPenyakit;
        this.gejalaPenyakit = gejalaPenyakit;
        this.obatPenyakit = obatPenyakit;
        this.typePenyakit = typePenyakit;
    }

    /*public Gejala(String gejalaId, String isiGejala, String namaPenyakit) {
        this.gejalaId = gejalaId;
        this.isiGejala = isiGejala;
        this.namaPenyakit = namaPenyakit;
    }*/

    public String getNamaPenyakit() {
        return namaPenyakit;
    }

    public String getIdPenyakit() {
        return idPenyakit;
    }

    public String getDescriptionPenyakit() {
        return descriptionPenyakit;
    }

    public String getGejalaPenyakit() {
        return gejalaPenyakit;
    }

    public String getObatPenyakit() {
        return obatPenyakit;
    }

    public int getTypePenyakit() {
        return typePenyakit;
    }

    public String getGejalaId() {
        return gejalaId;
    }

    public String getIsiGejala() {
        return isiGejala;
    }

    @Override
    public String toString() {
        return this.isiGejala + " ["+this.namaPenyakit+"] ";
    }

    public String stringType(){
        if (this.typePenyakit == RINGAN)
            return "Ringan";
        else if (this.typePenyakit == SEDANG)
            return "Sedang";
        else if (this.typePenyakit == BERAT)
            return "Berat";
        else
            return "Parah";
    }
}
