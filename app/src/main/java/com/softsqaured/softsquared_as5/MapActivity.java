package com.softsqaured.softsquared_as5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;


public class MapActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST_GPS = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    private RelativeLayout rlMap;
    private ImageView ivCurLoc;

    private Toolbar toolbar_map;
    private SearchView svSearchLocation;
    private MapView mapView;

    private LocationManager lm;
    private Location location;

    private static String[] sidoList = {"서울 강남구", "서울 강동구", "서울 강북구", "서울 강서구", "서울 관악구",
            "서울 광진구", "서울 구로구", "서울 금천구", "서울 노원구", "서울 도봉구",
            "서울 동대문구", "서울 동작구", "서울 마포구", "서울 서대문구", "서울 서초구",
            "서울 성동구", "서울 성북구", "서울 송파구", "서울 양천구", "서울 영등포구",
            "서울 용산구", "서울 은평구", "서울 종로구", "서울 중구", "서울 중랑구"};

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
        /* Location */
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        /* findViewByID */
        rlMap = findViewById(R.id.mapview_map);
        toolbar_map = findViewById(R.id.toolbar_map);
        ivCurLoc = findViewById(R.id.btn_curloc_map);

        /* ToolBar */
        setSupportActionBar(toolbar_map);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle("산책 지도");

        /* MapView Initializing */
        mapView = new MapView(MapActivity.this);
        rlMap.addView(mapView);
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(location.getLatitude(), location.getLongitude()), 5, true);
        mapView.setDefaultCurrentLocationMarker();
        mapView.setShowCurrentLocationMarker(true);
        mapView.setCurrentLocationRadius(20);
        mapView.setCurrentLocationRadiusStrokeColor(Color.BLUE);
        mapView.setCurrentLocationRadiusFillColor(Color.argb(66,0,0, 255));

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
                mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(location.getLatitude(), location.getLongitude()), true);
                mapView.setZoomLevel(2, true);
            }
        });
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
}
