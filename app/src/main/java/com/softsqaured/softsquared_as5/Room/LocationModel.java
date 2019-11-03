package com.softsqaured.softsquared_as5.Room;


import android.content.Context;

import com.softsqaured.softsquared_as5.Objects.GeneralLocation;

import java.util.ArrayList;

public class LocationModel {
    private LocationDao locationDao;

    public ArrayList<GeneralLocation> generalLocationList = new ArrayList<>();
    private void initLocation() {
        generalLocationList.add(new GeneralLocation("서울특별시 강남구", 37.517360, 127.047451));
        generalLocationList.add(new GeneralLocation("서울특별시 강동구", 37.530270, 127.123794));
        generalLocationList.add(new GeneralLocation("서울특별시 강북구", 37.639775, 127.025514));
        generalLocationList.add(new GeneralLocation("서울특별시 강서구", 37.550926, 126.849573));
        generalLocationList.add(new GeneralLocation("서울특별시 관악구", 37.478126, 126.951501));
        generalLocationList.add(new GeneralLocation("서울특별시 광진구", 37.538523, 127.082364));
        generalLocationList.add(new GeneralLocation("서울특별시 구로구", 37.495506, 126.887643));
        generalLocationList.add(new GeneralLocation("서울특별시 금천구", 37.456759, 126.895369));
        generalLocationList.add(new GeneralLocation("서울특별시 노원구", 37.654072, 127.056318));
        generalLocationList.add(new GeneralLocation("서울특별시 도봉구", 37.668906, 127.046996));
        generalLocationList.add(new GeneralLocation("서울특별시 동대문구", 37.574398, 127.039753));
        generalLocationList.add(new GeneralLocation("서울특별시 동작구", 37.512444, 126.939801));
        generalLocationList.add(new GeneralLocation("서울특별시 마포구", 37.566209, 126.901634));
        generalLocationList.add(new GeneralLocation("서울특별시 서대문구", 37.579172, 126.936789));
        generalLocationList.add(new GeneralLocation("서울특별시 서초구", 37.483576, 127.032655));
        generalLocationList.add(new GeneralLocation("서울특별시 성동구", 37.563087, 127.036665));
        generalLocationList.add(new GeneralLocation("서울특별시 성북구", 37.589359, 127.016742));
        generalLocationList.add(new GeneralLocation("서울특별시 송파구", 37.514456, 127.106075));
        generalLocationList.add(new GeneralLocation("서울특별시 양천구", 37.516958, 126.866564));
        generalLocationList.add(new GeneralLocation("서울특별시 영등포구", 37.526333, 126.896252));
        generalLocationList.add(new GeneralLocation("서울특별시 용산구", 37.532607, 126.990043));
        generalLocationList.add(new GeneralLocation("서울특별시 은평구", 37.602755, 126.929231));
        generalLocationList.add(new GeneralLocation("서울특별시 종로구", 37.572798, 126.979173));
        generalLocationList.add(new GeneralLocation("서울특별시 중구", 37.563759, 126.997543));
        generalLocationList.add(new GeneralLocation("서울특별시 중랑구", 37.606555, 127.092625));
    }

    public LocationModel(Context context) {
        locationDao = LocationDatabase.getDatabase(context).locationDao();
        initLocation();
    }

    public boolean addLocation(GeneralLocation generalLocation) {
        MyLocation myLocation = new MyLocation();
        myLocation.setLocation(generalLocation.getLocation());
        myLocation.setLat(generalLocation.getLat());
        myLocation.setLng(generalLocation.getLng());
        myLocation.setX(generalLocation.getX());
        myLocation.setY(generalLocation.getY());

        if (addLocation(myLocation))
            return true;
        return false;
    }

    public boolean addLocation(String location) {
        for (int i=0; i< generalLocationList.size(); i++) {
            if (location.equals(generalLocationList.get(i).getLocation())) {
                addLocation(generalLocationList.get(i));
                return true;
            }
        }
        return false;
    }

    public boolean addLocation(final MyLocation myLocation) {
        final ArrayList<MyLocation> myLocations = new ArrayList<>();
        Thread loadThread = new Thread(new Runnable() {
            @Override
            public void run() {
                myLocations.addAll(locationDao.getAllLocationsWithLocation(myLocation.getLocation()));
            }
        });
        loadThread.start();

        try {
            loadThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 이미 같은 지역 존재시 id 복제
        if (myLocations.size() != 0) {
            myLocation.setId(myLocations.get(0).getId());
        }

        // 추가
        Thread addThread = new Thread(new Runnable() {
            @Override
            public void run() {
                locationDao.insert(myLocation);
            }
        });
        addThread.start();
        return true;
    }

    public void deleteLocation(final String location) {
        Thread deleteThread = new Thread(new Runnable() {
            @Override
            public void run() {
                locationDao.deleteByLocation(location);
            }
        });
        deleteThread.start();
    }

    public void deleteLocation(final MyLocation myLocation) {
        Thread deleteThread = new Thread(new Runnable() {
            @Override
            public void run() {
                locationDao.delete(myLocation);
            }
        });
        deleteThread.start();
    }

    public ArrayList<MyLocation> getAllLocations() {
        final ArrayList<MyLocation> myLocations = new ArrayList<>();
        Thread loadThread = new Thread(new Runnable() {
            @Override
            public void run() {
                myLocations.addAll(locationDao.getAllLocations());
            }
        });
        loadThread.start();

        try {
            loadThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return myLocations;
    }
}
