package com.example.turismoreal.Services;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserServices {
    @POST("users/")
    Call<ResponseBody> getUsers(@Body RequestBody requestBody);

    @POST("employee/add/")
    Call<ResponseBody> addEmployee(@Body RequestBody requestBody);

    @POST("employee/update_password/")
    Call<ResponseBody> updatePassword(@Body RequestBody requestBody);
}
