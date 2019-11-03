package com.softsqaured.softsquared_as5.Objects;

import com.google.gson.annotations.SerializedName;

public class MiseDust {
    private String dataTime;
    private int pm10Grade1h;
    private double pm10Value;
    private int pm25Grade1h;
    private double pm25Value;

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public int getPm10Grade1h() {
        return pm10Grade1h;
    }

    public void setPm10Grade1h(int pm10Grade1h) {
        this.pm10Grade1h = pm10Grade1h;
    }

    public double getPm10Value() {
        return pm10Value;
    }

    public void setPm10Value(double pm10Value) {
        this.pm10Value = pm10Value;
    }

    public int getPm25Grade1h() {
        return pm25Grade1h;
    }

    public void setPm25Grade1h(int pm25Grade1h) {
        this.pm25Grade1h = pm25Grade1h;
    }

    public double getPm25Value() {
        return pm25Value;
    }

    public void setPm25Value(double pm25Value) {
        this.pm25Value = pm25Value;
    }
}
