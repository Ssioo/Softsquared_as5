package com.softsqaured.softsquared_as5.Objects;

public class GeneralLocation {
    private String location;
    private double Lat;
    private double Lng;
    private int X;
    private int Y;

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

    private static final double DEGRAD = Math.PI / 180.0;
    private static final double RADDEG = 180.0 / Math.PI;
    private static final double RE = 6371.00877; // 지구 반경(km)
    private static final double GRID = 5.0; // 격자 간격(km)
    private static final double SLAT1 = 30.0; // 투영 위도1(degree)
    private static final double SLAT2 = 60.0; // 투영 위도2(degree)
    private static final double OLON = 126.0; // 기준점 경도(degree)
    private static final double OLAT = 38.0; // 기준점 위도(degree)
    private static final double XO = 43; // 기준점 X좌표(GRID)
    private static final double YO = 136; // 기1준점 Y좌표(GRID)
    private static final double re = RE / GRID;
    private static final double slat1 = SLAT1 * DEGRAD;
    private static final double slat2 = SLAT2 * DEGRAD;
    private static final double olon = OLON * DEGRAD;
    private static final double olat = OLAT * DEGRAD;
    private double sn;
    private double sf;
    private double ro;

    public GeneralLocation(String location, double Lat, double Lng) {
        this.location = location;
        this.Lat = Lat;
        this.Lng = Lng;
        init();
        double ra = Math.tan(Math.PI * 0.25 + (Lat) * DEGRAD * 0.5);
        ra = re * sf / Math.pow(ra, sn);
        double theta = Lng * DEGRAD - olon;
        if (theta > Math.PI)
            theta -= 2.0 * Math.PI;
        if (theta < -Math.PI)
            theta += 2.0 * Math.PI;
        theta *= sn;

        this.X = (int) Math.floor(ra * Math.sin(theta) + XO + 0.5);
        this.Y = (int) Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);
    }

    public GeneralLocation(String location, int X, int Y) {
        this.location = location;
        this.X = X;
        this.Y = Y;
        init();
        double xn = this.Lat - XO;
        double yn = ro - this.Lng + YO;
        double ra = Math.sqrt(xn * xn + yn * yn);
        if (sn < 0.0)
            ra = -ra;
        double alat = Math.pow((re * sf / ra), (1.0 / sn));
        alat = 2.0 * Math.atan(alat) - Math.PI * 0.5;

        double theta = 0.0;
        if (Math.abs(xn) <= 0.0) {
            theta = 0.0;
        }
        else {
            if (Math.abs(yn) <= 0.0) {
                theta = Math.PI * 0.5;
                if (xn < 0.0) {
                    theta = -theta;
                }
            }
            else theta = Math.atan2(xn, yn);
        }
        double alon = theta / sn + olon;

        this.Lat = alat * RADDEG;
        this.Lng = alon * RADDEG;
    }

    private void init() {
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5));
        sf = Math.pow(Math.tan(Math.PI * 0.25 + slat1 * 0.5), sn) * Math.cos(slat1) / sn;
        ro = re * sf / Math.pow(Math.tan(Math.PI * 0.25 + olat * 0.5), sn);
    }
}
