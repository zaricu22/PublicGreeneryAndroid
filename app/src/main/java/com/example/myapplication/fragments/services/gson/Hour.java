package com.example.myapplication.fragments.services.gson;

import java.util.Date;

public class Hour {

    private Main main;
    private  Weather[] weather;
    private Date dt_txt;

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }

    public Date getDt_txt() {
        return dt_txt;
    }

    public void setDt_txt(Date dt_txt) {
        this.dt_txt = dt_txt;
    }
}
