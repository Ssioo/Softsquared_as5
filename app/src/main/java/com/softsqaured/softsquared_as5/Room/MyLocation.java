package com.softsqaured.softsquared_as5.Room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mylocation_table")
public class MyLocation {
    @PrimaryKey(autoGenerate = true)
    private int id = 0;

    private String location;
    private double Lat;
    private double Lng;
    private int X;
    private int Y;
    private int sky = 1;
    private int pty = 0;
    private double temp = 0.0;
    private double pop = 0.0;
    private double dust = 0.0;
    private int dustGrade = 1;
    private double fineDust = 0.0;
    private int findDustGrade = 1;
    private String measureDate = "";

    public void setSky(int sky) {
        this.sky = sky;
    }

    public int getSky() {
        return sky;
    }

    public int getPty() {
        return pty;
    }

    public void setPty(int pty) {
        this.pty = pty;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getPop() {
        return pop;
    }

    public void setPop(double pop) {
        this.pop = pop;
    }

    public int getDustGrade() {
        return dustGrade;
    }

    public void setDustGrade(int dustGrade) {
        this.dustGrade = dustGrade;
    }

    public double getFineDust() {
        return fineDust;
    }

    public void setFineDust(double fineDust) {
        this.fineDust = fineDust;
    }

    public int getFindDustGrade() {
        return findDustGrade;
    }

    public void setFindDustGrade(int findDustGrade) {
        this.findDustGrade = findDustGrade;
    }

    public String getMeasureDate() {
        return measureDate;
    }

    public void setMeasureDate(String measureDate) {
        this.measureDate = measureDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public double getLng() {
        return Lng;
    }

    public void setLng(double lng) {
        Lng = lng;
    }

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    public double getDust() {
        return dust;
    }

    public void setDust(double dust) {
        this.dust = dust;
    }

    public MyLocation() {
    }

    public MyLocation(String location, double lat, double lng, int x, int y) {
        this.location = location;
        Lat = lat;
        Lng = lng;
        X = x;
        Y = y;
    }
}
