package com.example.myapplication.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Job {
    // Sam generise ID pocevsi od 1
    @PrimaryKey
    @NonNull
    private Integer id;

    @NonNull
    private String opis;

    private String materijal;

    @NonNull
    private String status;

    @NonNull
    private Date pocetak;

    @NonNull
    private Date rok;

    private double lat;
    private double lng;

    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    @NonNull
    public String getOpis() {
        return opis;
    }

    public void setOpis(@NonNull String opis) {
        this.opis = opis;
    }

    @NonNull
    public String getMaterijal() {
        return materijal;
    }

    public void setMaterijal(@NonNull String materijal) {
        this.materijal = materijal;
    }

    @NonNull
    public String getStatus() {
        return status;
    }

    public void setStatus(@NonNull String status) {
        this.status = status;
    }

    @NonNull
    public Date getRok() {
        return rok;
    }

    public void setRok(@NonNull Date rok) {
        this.rok = rok;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @NonNull
    public Date getPocetak() {
        return pocetak;
    }

    public void setPocetak(@NonNull Date pocetak) {
        this.pocetak = pocetak;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", opis='" + opis + '\'' +
                ", materijal='" + materijal + '\'' +
                ", status='" + status + '\'' +
                ", pocetak=" + pocetak +
                ", rok=" + rok +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
