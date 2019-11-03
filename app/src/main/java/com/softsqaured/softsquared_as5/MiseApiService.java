package com.softsqaured.softsquared_as5;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MiseApiService {
    public static final String URL_MISE_DUMMY = "http://openapi.airkorea.or.kr/";


    /*
    stationName=동작구
    &dataTerm=daily
    &pageNo=1
    &numOfRows=10
    &ServiceKey=SERVICE_KEY
    &ver=1.3
    &_returnType=json
    */

    @GET("openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?ver=1.3&_returnType=json")
    Call<ResponseBody>getDusts(@Query("stationName") String stationName,
                               @Query("dataTerm") String dataTerm,
                               @Query("pageNo") int pageNo,
                               @Query("numOfRows") int numOfRows,
                               @Query("ServiceKey") String ServiceKey);


}
