package com.example.turismoreal.Services;
import com.example.turismoreal.models.ExtraService;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public interface ExtraServices {

    @GET("extra_services/")
    Call<List<ExtraService>> getExtraServices();

    @POST("extra_services/add/")
    Call<ResponseBody> addExtraService(@Body RequestBody requestBody);

    @POST ("extra_service/id/")
    Call<ResponseBody> getExtraServiceById(@Body RequestBody requestBody);
}
