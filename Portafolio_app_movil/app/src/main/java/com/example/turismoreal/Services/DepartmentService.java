package com.example.turismoreal.Services;

import com.example.turismoreal.models.Department;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DepartmentService {

    @GET("department/")
    Call<List<Department>> getDepartment();

    @POST("department/add/")
    Call<ResponseBody> addDepartment(@Body RequestBody requestBody);

}
