package com.example.turismoreal.Services;

import com.example.turismoreal.models.ExtraService;
import com.example.turismoreal.models.Reservation;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ReservationService {

    @GET("reservation/")
    Call<List<Reservation>> getReservation();

    @POST("reservation/id/")
    Call<ResponseBody> getReservation(@Body RequestBody requestBody);

    @POST("reservation/add_extra_service/")
    Call<ResponseBody> addExtraServiceToReservation(@Body RequestBody requestBody);

    @POST("reservation/extra_service/")
    Call<ResponseBody> getReservationExtraServiceId(@Body RequestBody requestBody);
}
