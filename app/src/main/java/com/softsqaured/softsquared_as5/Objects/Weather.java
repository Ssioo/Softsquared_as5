package com.softsqaured.softsquared_as5.Objects;

public class Weather {
    private double T3H; // 3시간 기온
    private String R06; // 6시간 강수량
    private int SKY; // 하늘 상태 - 맑음 : 1, 구름 많음 : 3, 흐림 : 4
    private int PTY; // 강수 형태 - 없음 : 0, 비 : 1, 비/눈 : 2, 눈 : 3, 소나기 : 4
    private double POP; // 강수확률

    public Weather(double t3H, String r06, int SKY, int PTY) {
        T3H = t3H;
        R06 = r06;
        this.SKY = SKY;
        this.PTY = PTY;
    }

    public Weather() {
    }

    public double getPOP() {
        return POP;
    }

    public void setPOP(double POP) {
        this.POP = POP;
    }

    public double getT3H() {
        return T3H;
    }

    public void setT3H(double t3H) {
        T3H = t3H;
    }

    public String getR06() {
        return R06;
    }

    public void setR06(String r06) {
        R06 = r06;
    }

    public int getSKY() {
        return SKY;
    }

    public void setSKY(int SKY) {
        this.SKY = SKY;
    }

    public int getPTY() {
        return PTY;
    }

    public void setPTY(int PTY) {
        this.PTY = PTY;
    }
}
