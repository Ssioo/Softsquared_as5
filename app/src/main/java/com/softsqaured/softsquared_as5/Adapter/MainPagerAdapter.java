package com.softsqaured.softsquared_as5.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.PagerAdapter;

import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.helper.log.Logger;
import com.softsqaured.softsquared_as5.Room.LocationModel;
import com.softsqaured.softsquared_as5.MiseApiService;
import com.softsqaured.softsquared_as5.Objects.MiseDust;
import com.softsqaured.softsquared_as5.R;
import com.softsqaured.softsquared_as5.Room.MyLocation;
import com.softsqaured.softsquared_as5.View.MainActivity;
import com.softsqaured.softsquared_as5.View.MapActivity;
import com.softsqaured.softsquared_as5.Objects.Weather;
import com.softsqaured.softsquared_as5.WeatherApiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainPagerAdapter extends PagerAdapter implements View.OnClickListener {

    private SimpleDateFormat sdfTvCurDate = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
    private SimpleDateFormat sdfBaseDate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
    private SimpleDateFormat sdfBaseTime = new SimpleDateFormat("H00", Locale.getDefault());
    private Context mContext;
    private LayoutInflater layoutInflater;
    private ArrayList<MyLocation> myLocations;
    private Retrofit retrofit_mise;
    private Retrofit retrofit_weather;
    private MiseApiService miseApiService_mise;
    private WeatherApiService weatherApiService_weather;

    private ImageView ivAddLocation;
    private ImageView ivMenu;
    private ImageView ivShare;
    private ImageView ivSky;
    private ImageView ivSmileEmoticon;
    private TextView tvCurLoc;
    private TextView tvCurTime;
    private TextView tvCurTemperature;
    private TextView tvCurPM;

    private LocationModel locationModel;

    public MainPagerAdapter(Context context, ArrayList<MyLocation> myLocations) {
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
        this.myLocations = myLocations;
        locationModel = new LocationModel(context);

        /* Retrofit2 */
        retrofit_mise = new Retrofit.Builder().baseUrl(MiseApiService.URL_MISE_DUMMY).build();
        retrofit_weather = new Retrofit.Builder().baseUrl(WeatherApiService.URL_WEATHER_DUMMY).build();
        miseApiService_mise = retrofit_mise.create(MiseApiService.class);
        weatherApiService_weather = retrofit_weather.create(WeatherApiService.class);
        Log.i("ViewPagerAdapter", "Constructed");
    }

    public void addLocations(MyLocation myLocation) {
        this.myLocations.add(myLocation);
        notifyDataSetChanged();
    }

    public void removeLocations(MyLocation myLocation) {
        this.myLocations.remove(myLocation);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return myLocations.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.fragment_main, container, false);

        /* findViewById */
        ivAddLocation = view.findViewById(R.id.btn_addlocation_main);
        ivMenu = view.findViewById(R.id.btn_menu);
        ivShare = view.findViewById(R.id.btn_share);
        tvCurLoc = view.findViewById(R.id.text_location);
        tvCurTime = view.findViewById(R.id.text_time_main);
        tvCurTemperature = view.findViewById(R.id.text_temperature_main);
        tvCurPM = view.findViewById(R.id.text_pm10_main);
        ivSky = view.findViewById(R.id.iv_sky_main);
        ivSmileEmoticon = view.findViewById(R.id.iv_smile_mise_main);

        Log.i("ViewPagerAdapter", "jSON Loading Start");

        /* JSON Loading */
        MiseDustLoading(myLocations.get(position).getLocation().substring(myLocations.get(position).getLocation().lastIndexOf(" ") + 1), position);
        Date today = new Date();
        String base_date = sdfBaseDate.format(today);
        int temp_base_time = ((Integer.valueOf(sdfBaseTime.format(today)) + 100) / 300) * 300 - 100;
        if (Integer.valueOf(sdfBaseTime.format(today)) < 200) {
            Calendar yesterday = Calendar.getInstance();
            yesterday.add(Calendar.DATE, -1);
            base_date = sdfBaseDate.format(yesterday.getTime());
        }
        if (temp_base_time < 0)
            temp_base_time += 2400;
        String base_time = String.valueOf(temp_base_time);
        if (base_time.length() != 4)
            base_time = "0" + base_time;
        Log.i("BASE_TIME_RAW", sdfBaseTime.format(today));
        Log.i("BASE_TIME", base_time);
        Log.i("BASE_DATE", base_date);
        WeatherLoading(base_date, base_time, myLocations.get(position).getX(), myLocations.get(position).getY(), position);

        /* Set Tag */
        ivShare.setTag(position);

        /* Set On Click Listener */
        ivAddLocation.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        ivMenu.setOnClickListener(this);

        /* Set Text */
        tvCurTime.setText(sdfTvCurDate.format(today));
        tvCurLoc.setText(myLocations.get(position).getLocation());

        /* Add to ViewPager */
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public FeedTemplate makeKaKaoLink(int position) {
        FeedTemplate params = FeedTemplate
                .newBuilder(ContentObject.newBuilder("지금 " + myLocations.get(position).getLocation() + " 미세먼지는?",
                        "https://img.sbs.co.kr/newimg/news/20190304/201288508_1280.jpg",
                        LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
                                .setMobileWebUrl("https://developers.kakao.com").build())
                        .setDescrption("무서운 미세먼지! 앱에서 미리보고 피하자!")
                        .build())
                .addButton(new ButtonObject("앱에서 보기", LinkObject.newBuilder()
                        .setWebUrl("'https://developers.kakao.com")
                        .setMobileWebUrl("'https://developers.kakao.com")
                        .setAndroidExecutionParams("key1=value1")
                        .setIosExecutionParams("key1=value1").build()))
                .build();

        return params;
    }

    public void sendKakaoLink(FeedTemplate params, final Context context) {
        Map<String, String> serverCallbackArgs = new HashMap<String, String>();
        serverCallbackArgs.put("user_id", "${current_user_id}");
        serverCallbackArgs.put("product_id", "${shared_product_id}");

        KakaoLinkService.getInstance().sendDefault(context, params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e(errorResult.toString());
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {
                Toast.makeText(context, "공유가 완료되었습니다", Toast.LENGTH_SHORT).show();
                // 템플릿 밸리데이션과 쿼터 체크가 성공적으로 끝남. 톡에서 정상적으로 보내졌는지 보장은 할 수 없다. 전송 성공 유무는 서버콜백 기능을 이용하여야 한다.
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_addlocation_main:
                Intent intent = new Intent(v.getContext(), MapActivity.class);
                v.getContext().startActivity(intent);
                break;

            case R.id.btn_share:
                sendKakaoLink(makeKaKaoLink((int) v.getTag()), v.getContext());
                break;

            case R.id.btn_menu:
                ((MainActivity) v.getContext()).dlMain.openDrawer(GravityCompat.START);
                break;
        }
    }

    private void WeatherLoading(String base_date, String base_time, int nx, int ny, final int position) {
        Call<ResponseBody> weathersBody = weatherApiService_weather.getWeather(mContext.getString(R.string.weather_app_key), base_date, base_time, nx, ny);
        weathersBody.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String json = response.body().string();
                    Log.i("RESPONSE", json);
                    JSONObject dataObject = new JSONObject(json);
                    JSONArray weatherlist = dataObject.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item");
                    Weather weather = new Weather();
                    for (int i = 0; i < weatherlist.length(); i++) {
                        JSONObject weatherObject = weatherlist.getJSONObject(i);
                        String category = weatherObject.getString("category");
                        if (category.equals("PTY")) {
                            weather.setPTY(weatherObject.getInt("fcstValue"));
                        } else if (category.equals("R06")) {
                            weather.setR06(weatherObject.getString("fcstValue"));
                        } else if (category.equals("SKY")) {
                            weather.setSKY(weatherObject.getInt("fcstValue"));
                        } else if (category.equals("T3H")) {
                            weather.setT3H(weatherObject.getDouble("fcstValue"));
                        } else if (category.equals("POP")) {
                            weather.setPOP(weatherObject.getDouble("fcstValue"));
                        }
                    }
                    tvCurTemperature.setText("온도: " + weather.getT3H() + " ℃");

                    myLocations.get(position).setSky(weather.getSKY());
                    myLocations.get(position).setPop(weather.getPOP());
                    myLocations.get(position).setPty(weather.getPTY());
                    myLocations.get(position).setTemp(weather.getT3H());

                    if (locationModel.addLocation(myLocations.get(position)))
                        Log.i("WeatherSave", "Success");
                    else
                        Log.i("WeatherSave", "Fail");
                    switch (weather.getSKY()) {
                        case 1:
                            ivSky.setImageResource(R.drawable.ic_sunny);
                            break;
                        case 3:
                            ivSky.setImageResource(R.drawable.ic_cloud_little);
                            break;
                        case 4:
                            ivSky.setImageResource(R.drawable.ic_cloud);
                            break;
                    }
                    switch (weather.getPTY()) {
                        case 0:
                            break;
                        case 1:
                            ivSky.setImageResource(R.drawable.ic_rainy);
                            break;
                        case 2:
                            ivSky.setImageResource(R.drawable.ic_rainy);
                            break;
                        case 3:
                            ivSky.setImageResource(R.drawable.ic_snowy);
                            break;
                        case 4:
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void MiseDustLoading(final String location, final int position) {
        Call<ResponseBody> miseDusts = miseApiService_mise.getDusts(location, "DAILY", 1, 1, mContext.getString(R.string.mise_app_key));
        miseDusts.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String json = response.body().string();
                    Log.i("RESPONSE", json);
                    JSONObject dataObject = new JSONObject(json);
                    JSONObject miseObject = dataObject.getJSONArray("list").getJSONObject(0);

                    // 가장 최근 정보 1개
                    MiseDust miseDust = new MiseDust();
                    miseDust.setDataTime(miseObject.getString("dataTime"));
                    miseDust.setPm10Grade1h(miseObject.getInt("pm10Grade1h"));
                    miseDust.setPm10Value(miseObject.getDouble("pm10Value"));
                    miseDust.setPm25Grade1h(miseObject.getInt("pm25Grade1h"));
                    miseDust.setPm25Value(miseObject.getDouble("pm25Value"));

                    /* Set Text */
                    tvCurPM.setText("PM10: " + miseDust.getPm10Value() + " ㎍/㎥\nPM25: " + miseDust.getPm25Value() + " ㎍/㎥");

                    myLocations.get(position).setDust(miseDust.getPm25Value());
                    myLocations.get(position).setFineDust(miseDust.getPm10Value());
                    myLocations.get(position).setDustGrade(miseDust.getPm25Grade1h());
                    myLocations.get(position).setFindDustGrade(miseDust.getPm10Grade1h());
                    myLocations.get(position).setMeasureDate(miseDust.getDataTime());

                    if (locationModel.addLocation(myLocations.get(position)))
                        Log.i("DustSave", "Success");
                    else
                        Log.i("DustSave", "Fail");
                    switch (miseDust.getPm25Grade1h() + miseDust.getPm10Grade1h()) {
                        case 2:
                            // 아주 좋음
                            ivSmileEmoticon.setImageResource(R.drawable.ic_emoticon_excellent);
                            break;
                        case 3:
                            ivSmileEmoticon.setImageResource(R.drawable.ic_emoticon_good);
                            // 좋음
                            break;
                        case 4:
                        case 5:
                            ivSmileEmoticon.setImageResource(R.drawable.ic_emoticon_soso);
                            // 보통
                            break;
                        case 6:
                        case 7:
                            ivSmileEmoticon.setImageResource(R.drawable.ic_emoticon_bad);
                            // 나쁨
                            break;
                        case 8:
                            ivSmileEmoticon.setImageResource(R.drawable.ic_emoticon_worst);
                            // 매우 나쁨
                            break;
                    }

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
