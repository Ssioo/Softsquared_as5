package com.softsqaured.softsquared_as5;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {
    public static final String URL_WEATHER_DUMMY = "http://newsky2.kma.go.kr/";

    /*
    ServiceKey=SERVICE_KEY
    &numOfRows=14
    &pageNo=1
    &base_date=20191026
    &base_time=1200
    &nx=59
    &ny=125
    &_type=json
    */

    @GET("service/SecndSrtpdFrcstInfoService2/ForecastSpaceData?_type=json&numOfRows=9")
    Call<ResponseBody> getWeather(@Query("ServiceKey") String ServiceKey,
                                  @Query("base_date") String base_date,
                                  @Query("base_time") String base_time,
                                  @Query("nx") int nx,
                                  @Query("ny") int ny);
}
