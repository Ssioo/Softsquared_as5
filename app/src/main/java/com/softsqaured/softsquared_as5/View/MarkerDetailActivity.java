package com.softsqaured.softsquared_as5.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.softsqaured.softsquared_as5.Objects.GeneralLocation;
import com.softsqaured.softsquared_as5.Room.LocationModel;
import com.softsqaured.softsquared_as5.R;

import java.util.ArrayList;

public class MarkerDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ToggleButton tbtnSubscribe;
    private TextView tvLocation;
    private TextView tvDate;
    private TextView tvDataTime;
    private TextView tvDust;
    private TextView tvFineDust;
    private TextView tvDustGrade;
    private TextView tvFineDustGrade;
    private TextView tvTemp;
    private ImageView ivSkyPty;
    private TextView tvPop;
    private ImageView ivSmileDust;
    private ImageView ivSmileFineDust;

    private LocationModel locationModel;

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
        setContentView(R.layout.activity_marker_detail);

        /* findViewbyID */
        toolbar = findViewById(R.id.toolbar_markerdetail);
        tbtnSubscribe = findViewById(R.id.btn_subscribe_detail);
        tvLocation = findViewById(R.id.text_location_detail);
        tvDate = findViewById(R.id.text_cur_date_detail);
        tvDataTime = findViewById(R.id.text_measure_time_detail);
        tvDust = findViewById(R.id.text_mise_detail);
        tvFineDust = findViewById(R.id.text_chomise_detail);
        tvDustGrade = findViewById(R.id.text_mise_status_detail);
        tvFineDustGrade = findViewById(R.id.text_chomise_status_detail);
        tvTemp = findViewById(R.id.text_cur_temp_detail);
        ivSkyPty = findViewById(R.id.iv_sky_detail);
        tvPop = findViewById(R.id.text_cur_pop_detail);
        ivSmileDust = findViewById(R.id.iv_emoticon_mise_detail);
        ivSmileFineDust = findViewById(R.id.iv_emoticon_chomise_detail);

        /* General Location Set */
        initLocation();

        /* DB */
        locationModel = new LocationModel(this);

        /* getIntent */
        final Intent intent = getIntent();



        /* Toolbar */
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("자세히 보기");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_close);

        /* Set View */
        tvLocation.setText(intent.getStringExtra("location"));
        tvDust.setText(String.valueOf(intent.getDoubleExtra("dust", 0.0)) + " ㎍/㎥");
        tvFineDust.setText(String.valueOf(intent.getDoubleExtra("finedust", 0.0)) + " ㎍/㎥");
        tvTemp.setText(String.valueOf(intent.getDoubleExtra("temp", 0.0)) + " ℃");
        tvPop.setText(intent.getDoubleExtra("pop", 0.0) + " %");
        tvDataTime.setText(intent.getStringExtra("datatime"));
        if (intent.getIntExtra("Tag", 0) == 0) {
            tbtnSubscribe.setChecked(false);
        } else {
            tbtnSubscribe.setChecked(true);
        }



        tbtnSubscribe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // DB 저장
                    locationModel.addLocation(intent.getStringExtra("location"));
                } else {
                    // DB에서 삭제
                    locationModel.deleteLocation(intent.getStringExtra("location"));
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
