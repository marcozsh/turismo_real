package com.example.turismoreal.Services;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ReservationService {
    @POST("reservation/id/")
    Call<ResponseBody> getReservation(@Body RequestBody requestBody);
}
