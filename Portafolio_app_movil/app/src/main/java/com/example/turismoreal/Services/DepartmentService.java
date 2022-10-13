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

    @POST("department_inventory/")
    Call<ResponseBody> getDepartmentInventory(@Body RequestBody requestBody);

    @POST("department/available/")
    Call<ResponseBody> markDepartmentAvailable(@Body RequestBody requestBody);

    @POST("department_inventory/add/")
    Call<ResponseBody> addDepartmentInventory(@Body RequestBody requestBody);

    @POST("department/add/maintenance/")
    Call<ResponseBody> addMaintenance(@Body RequestBody requestBody);

    @POST("not_available_dates/")
    Call<ResponseBody>getNotAvailableDates(@Body RequestBody requestBody);

    @POST("department/")
    Call<ResponseBody> getDepartmentByCommune(@Body RequestBody requestBody);

    @POST("department/id/")
    Call<ResponseBody> getDepartmentById(@Body RequestBody requestBody);

    @POST("department/add/")
    Call<ResponseBody> addDepartment(@Body RequestBody requestBody);

}
