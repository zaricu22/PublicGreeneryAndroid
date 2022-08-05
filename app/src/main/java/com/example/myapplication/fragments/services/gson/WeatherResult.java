package com.example.myapplication.fragments.services.gson;

// Hijerarhija objekata JSON dokumenta mora se ispostovati i napraviti sve kao zasebne klase ili teze praviti rucni Deserijalizator
// Ne mora @SerializedName("list") ako se polje zove isto kao u JSON dokumentu, znaci tip i naziv moraju poklapati kao u JSON
public class WeatherResult {

    private Hour[] list;

    public Hour[] getList() {
        return list;
    }

    public void setList(Hour[] list) {
        this.list = list;
    }
}
