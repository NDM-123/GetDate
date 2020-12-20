package com.example.date1b;

public class MarkerInfo {

    public String name;
    public String latitude;
    public String longitude;


    public MarkerInfo() {               //default constructor which invokes on object creation of respectisve class in MainActivity.java

    }

    public MarkerInfo(String name, String latitude, String longitude) {    //parameterized constructor which will store the retrieved data from firebase
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public String getString(String a) {
        return name;
    }
}
