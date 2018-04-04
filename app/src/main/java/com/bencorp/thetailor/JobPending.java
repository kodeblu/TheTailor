package com.bencorp.thetailor;

import android.widget.Toast;

/**
 * Created by hp-pc on 3/19/2018.
 */

public class JobPending {
    private String name;
    private int price;
    private String measurements;
    private String date;
    private int id;
    private byte[] img;
    SqliteHandler myDb;
    public JobPending(String name,Integer price,String measurements,String date,int id, byte[] img){
        this.setName(name);
        this.setMeasurements(measurements);
        this.setPrice(price);
        this.setDate(date);
        this.setId(id);
        this.setImg(img);

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getMeasurements() {
        return measurements;
    }

    public void setMeasurements(String measurements) {
        this.measurements = measurements;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImg(byte[] img){
        this.img = img;
    }
    public byte[] getImg(){
        return img;
    }
}
