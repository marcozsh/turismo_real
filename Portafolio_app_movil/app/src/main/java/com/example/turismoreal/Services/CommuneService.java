package com.example.turismoreal.Services;

import com.example.turismoreal.models.Commune;
import com.example.turismoreal.models.Department;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CommuneService {
    @GET("all_communes/")
    Call<List<Commune>> getCommunes();
}
