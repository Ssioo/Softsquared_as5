package com.softsqaured.softsquared_as5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivAddLocation;
    private TextView tvCurLoc;
    private TextView tvCurTime;
    private TextView tvCurTemperature;
    private TextView tvCurPM;

    private ArrayList<String> locationList = new ArrayList<>();

    private Retrofit retrofit;
    private ApiService apiService;
    private ArrayList<MiseDust> miseDustsArrayList = new ArrayList<>();

    private static String[] sidoList = {"서울 강남구", "서울 강동구", "서울 강북구", "서울 강서구", "서울 관악구",
            "서울 광진구", "서울 구로구", "서울 금천구", "서울 노원구", "서울 도봉구",
            "서울 동대문구", "서울 동작구", "서울 마포구", "서울 서대문구", "서울 서초구",
            "서울 성동구", "서울 성북구", "서울 송파구", "서울 양천구", "서울 영등포구",
            "서울 용산구", "서울 은평구", "서울 종로구", "서울 중구", "서울 중랑구"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Retrofit2 */
        retrofit = new Retrofit.Builder().baseUrl(ApiService.URL_DUMMY).build();
        apiService = retrofit.create(ApiService.class);

        /* findViewById */
        ivAddLocation = findViewById(R.id.btn_addlocation_main);
        tvCurLoc = findViewById(R.id.text_location);
        tvCurTime = findViewById(R.id.text_time_main);
        tvCurTemperature = findViewById(R.id.text_temperature_main);
        tvCurPM = findViewById(R.id.text_pm10_main);

        /* Set On Click Listener */
        ivAddLocation.setOnClickListener(this);

        /* Location DataSet for Searching Mise Data */
        locationList.add("동작구");

        /* JSON Loading */
        MiseDustLoading(locationList.get(0));


        /* Set Text */
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd mm:SS", Locale.getDefault());
        tvCurTime.setText(simpleDateFormat.format(new Date()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_addlocation_main:
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void MiseDustLoading(String location) {
        miseDustsArrayList.clear();
        Call<ResponseBody> miseDusts = apiService.getDusts(location, "DAILY", 1, 3,
                getResources().getString(R.string.mise_app_key));
        miseDusts.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String json = response.body().string();
                    Log.i("RESPONSE", json);

                    JSONObject dataObject = new JSONObject(json);
                    JSONArray miseList = dataObject.getJSONArray("list");
                    Log.i("list", miseList.toString());
                    for (int i=0; i< miseList.length(); i++) {
                        JSONObject miseObject = miseList.getJSONObject(i);

                        MiseDust miseDust = new MiseDust();
                        miseDust.setDataTime(miseObject.getString("dataTime"));
                        miseDust.setPm10Grade1h(miseObject.getInt("pm10Grade1h"));
                        miseDust.setPm10Value(miseObject.getDouble("pm10Value"));
                        miseDust.setPm25Grade1h(miseObject.getInt("pm25Grade1h"));
                        miseDust.setPm25Value(miseObject.getDouble("pm25Value"));

                        tvCurPM.setText(String.valueOf(miseDustsArrayList.get(0).getPm10Value()) + " ㎍/㎥");
                    }
                    Log.i("size", String.valueOf(miseDustsArrayList.size()));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("FAILURE", "FAILED");
                t.printStackTrace();
            }
        });
    }
}
