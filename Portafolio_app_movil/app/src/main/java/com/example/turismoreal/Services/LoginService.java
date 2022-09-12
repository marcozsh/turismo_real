package com.example.turismoreal.Services;


import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;


import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public interface LoginService {

    @POST("employee_login/")
    Call<ResponseBody> login(@Body RequestBody requestBody);

    @POST("employee_logout/")
    Call<ResponseBody> logOutEmployee(@Body RequestBody requestBody);

    @POST("employee/")
    Call<ResponseBody> loginEmployee(@Body RequestBody requestBody);
}

