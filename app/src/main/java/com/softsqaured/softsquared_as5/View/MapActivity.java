package com.softsqaured.softsquared_as5.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.softsqaured.softsquared_as5.Room.LocationModel;
import com.softsqaured.softsquared_as5.R;
import com.softsqaured.softsquared_as5.Objects.GeneralLocation;
import com.softsqaured.softsquared_as5.Room.MyLocation;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;


public class MapActivity extends AppCompatActivity implements MapView.POIItemEventListener, MapView.MapViewEventListener{

    private static final int MY_PERMISSION_REQUEST_GPS = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    private ImageView ivCurLoc;

    private Toolbar toolbar_map;
    private SearchView svSearchLocation;
    private MapView mvMap;

    private LocationManager lm;
    private Location location;

    private LocationModel locationModel;

    private ArrayList<MyLocation> myLocations = new ArrayList<>();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        /* Permission Check */
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(MapActivity.this, "위치 접근 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(MapActivity.this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(MapActivity.this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        }
        /* Current Location */
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        /* findViewByID */
        mvMap = findViewById(R.id.mapview_map);
        toolbar_map = findViewById(R.id.toolbar_map);
        ivCurLoc = findViewById(R.id.btn_curloc_map);

        /* MyLocation Database */
        locationModel = new LocationModel(MapActivity.this);


        /* ToolBar */
        setSupportActionBar(toolbar_map);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle("미세먼지 지도");

        /* GeneralLocation Initializing */
        initLocation();


        /* MapView Initializing */
        mvMap.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(location.getLatitude(), location.getLongitude()), 5, true);
        mvMap.setDefaultCurrentLocationMarker();
        mvMap.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        mvMap.setShowCurrentLocationMarker(true);
        mvMap.setPOIItemEventListener(this);
        mvMap.setCalloutBalloonAdapter(new CustomCallOurBallonAdapter());

        /* Set on Click Listener */
        ivCurLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        Toast.makeText(MapActivity.this, "위치 접근 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(MapActivity.this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
                    } else {
                        ActivityCompat.requestPermissions(MapActivity.this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
                    }
                }
                location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                mvMap.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                Log.i("MAP", String.valueOf(mvMap.isShowingCurrentLocationMarker()));
                mvMap.setDefaultCurrentLocationMarker();
                mvMap.setCurrentLocationRadius(100);
                mvMap.setCurrentLocationRadiusStrokeColor(Color.argb(33, 66, 66, 255));
                mvMap.setCurrentLocationRadiusFillColor(Color.argb(99,66,66, 255));
                mvMap.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(location.getLatitude(), location.getLongitude()), true);
                mvMap.setZoomLevel(2, true);
            }
        });
    }

    private void mapMarkerInit() {
        for (int i=0; i< generalLocationList.size(); i++) {
            boolean isMyLocaiton = false;
            for (int j=0; j< myLocations.size(); j++) {
                if (generalLocationList.get(i).getLocation().equals(myLocations.get(j).getLocation())) {
                    isMyLocaiton = true;
                }
            }
            if (!isMyLocaiton) {
                MapPOIItem mapPOIItem = new MapPOIItem();
                mapPOIItem.setItemName("");
                mapPOIItem.setMapPoint(MapPoint.mapPointWithGeoCoord(generalLocationList.get(i).getLat(), generalLocationList.get(i).getLng()));
                mapPOIItem.setMarkerType(MapPOIItem.MarkerType.RedPin);
                mapPOIItem.setShowAnimationType(MapPOIItem.ShowAnimationType.SpringFromGround);
                mapPOIItem.setUserObject(generalLocationList.get(i));
                mapPOIItem.setTag(0);
                mapPOIItem.setShowDisclosureButtonOnCalloutBalloon(false);
                mvMap.addPOIItem(mapPOIItem);
            }
        }
        for (int i=0; i<myLocations.size(); i++) {
            MapPOIItem mapPOIItem = new MapPOIItem();
            mapPOIItem.setItemName("");
            mapPOIItem.setMapPoint(MapPoint.mapPointWithGeoCoord(myLocations.get(i).getLat(), myLocations.get(i).getLng()));
            mapPOIItem.setMarkerType(MapPOIItem.MarkerType.BluePin);
            mapPOIItem.setShowAnimationType(MapPOIItem.ShowAnimationType.SpringFromGround);
            mapPOIItem.setUserObject(myLocations.get(i));
            mapPOIItem.setTag(1);
            mapPOIItem.setShowDisclosureButtonOnCalloutBalloon(false);
            mvMap.addPOIItem(mapPOIItem);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        /* MyLocation Initializing */
        myLocations = locationModel.getAllLocations();
        mapMarkerInit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE && grantResults.length == REQUIRED_PERMISSIONS.length) {
            boolean check_result = true;

            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map , menu) ;
        svSearchLocation = (SearchView) menu.findItem(R.id.menu_search_map).getActionView();
        svSearchLocation.setMaxWidth(Integer.MAX_VALUE);
        svSearchLocation.setQueryHint("위치 찾기");
        svSearchLocation.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_close_map:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
        // 마커 선택시
        //Toast.makeText(this, "onCalloutBalloonOfPOIItemTouched2", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MarkerDetailActivity.class);
        if (mapPOIItem.getTag() == 1) {
            intent.putExtra("location", ((MyLocation) mapPOIItem.getUserObject()).getLocation());
            intent.putExtra("dust", ((MyLocation) mapPOIItem.getUserObject()).getDust());
            intent.putExtra("dustgrade", ((MyLocation) mapPOIItem.getUserObject()).getDustGrade());
            intent.putExtra("finedust", ((MyLocation) mapPOIItem.getUserObject()).getFineDust());
            intent.putExtra("finedustgrade", ((MyLocation) mapPOIItem.getUserObject()).getFindDustGrade());
            intent.putExtra("datatime", ((MyLocation) mapPOIItem.getUserObject()).getMeasureDate());
            intent.putExtra("temp", ((MyLocation) mapPOIItem.getUserObject()).getTemp());
            intent.putExtra("pop", ((MyLocation) mapPOIItem.getUserObject()).getPop());
            intent.putExtra("pty", ((MyLocation) mapPOIItem.getUserObject()).getPty());
            intent.putExtra("sky", ((MyLocation) mapPOIItem.getUserObject()).getSky());
            intent.putExtra("Tag", mapPOIItem.getTag());
        } else {
            intent.putExtra("location", ((GeneralLocation) mapPOIItem.getUserObject()).getLocation());
            intent.putExtra("Tag", mapPOIItem.getTag());
        }
        startActivity(intent);
    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    private class CustomCallOurBallonAdapter implements CalloutBalloonAdapter {

        private final View mCalloutBallloon;

        public CustomCallOurBallonAdapter() {
            mCalloutBallloon = getLayoutInflater().inflate(R.layout.custom_layout_balloon, null);
        }

        @Override
        public View getCalloutBalloon(MapPOIItem mapPOIItem) {
            if (mapPOIItem.getTag() == 0) {
                ((TextView) mCalloutBallloon.findViewById(R.id.text_location_balloon)).setText(((GeneralLocation) mapPOIItem.getUserObject()).getLocation());
                ((TextView) mCalloutBallloon.findViewById(R.id.text_dust_balloon)).setText("-.-/-.-");
                ((TextView) mCalloutBallloon.findViewById(R.id.text_temp_balloon)).setText("-.- ℃");
            } else {
                ((TextView) mCalloutBallloon.findViewById(R.id.text_location_balloon)).setText(((MyLocation) mapPOIItem.getUserObject()).getLocation());
                ((TextView) mCalloutBallloon.findViewById(R.id.text_dust_balloon)).setText(((MyLocation) mapPOIItem.getUserObject()).getDust() + "/" + ((MyLocation) mapPOIItem.getUserObject()).getFineDust());
                ((TextView) mCalloutBallloon.findViewById(R.id.text_temp_balloon)).setText(((MyLocation) mapPOIItem.getUserObject()).getTemp() + " ℃");
            }
            return mCalloutBallloon;
        }

        @Override
        public View getPressedCalloutBalloon(MapPOIItem mapPOIItem) {
            return mCalloutBallloon;
        }
    }
}
