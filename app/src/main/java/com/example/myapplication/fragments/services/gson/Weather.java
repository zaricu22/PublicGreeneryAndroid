package com.example.myapplication.fragments.services.gson;

import android.graphics.Bitmap;

public class Weather {

    private String main;
    private String icon;
    private Bitmap weatherIcon; // za naknadnu upotrebu

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Bitmap getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(Bitmap weatherIcon) {
        this.weatherIcon = weatherIcon;
    }
}
