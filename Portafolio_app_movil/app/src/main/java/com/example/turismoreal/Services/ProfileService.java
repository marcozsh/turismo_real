package com.example.turismoreal.Services;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ProfileService {
    @POST("profile/")
    Call<ResponseBody> getProfile(@Body RequestBody requestBody);
}
