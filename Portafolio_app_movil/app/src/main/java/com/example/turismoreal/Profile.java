package com.example.turismoreal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.turismoreal.Services.ProfileService;
import com.example.turismoreal.Services.UserServices;
import com.example.turismoreal.administrator.DepartmentPage;
import com.example.turismoreal.administrator.ExtraServicePage;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Profile extends AppCompatActivity {


    TextView userName2, rut, email, position;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_TurismoReal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userName2 = findViewById(R.id.userName2);
        rut = findViewById(R.id.rut);
        email = findViewById(R.id.email);
        position = findViewById(R.id.position);

        getProfile();
    }

    public void getProfile() {
        SharedPreferences preferences = getSharedPreferences("current_session", Context.MODE_PRIVATE);
        int userId = preferences.getInt("userId", 0);
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setCancelable(false);
        final View loading = getLayoutInflater().inflate(R.layout.loading_gif, null);
        dialogBuilder.setView(loading);
        dialog = dialogBuilder.create();
        dialog.show();
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(SplashScreen.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ProfileService profileService = retrofit.create(ProfileService.class);
            String jsonData = "{\"user_id\": " + userId + "}";
            RequestBody requestBody = RequestBody.create(MediaType.parse("aplicaton/json"), jsonData);
            profileService.getProfile(requestBody).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        dialog.dismiss();
                        com.example.turismoreal.models.Profile[] profile = new Gson().fromJson(response.body().string(), com.example.turismoreal.models.Profile[].class);
                        for (com.example.turismoreal.models.Profile p: profile) {
                            userName2.setText(p.getFullName());
                            rut.setText(p.getRut());
                            email.setText(p.getMail());
                            position.setText(p.getRol());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void goBack(View view){
        Intent i = new Intent(this, LandingPage.class);
        startActivity(i);
        finish();
    }

    public void extraServicesMenu(View view){
        Intent i = new Intent(this, ExtraServicePage.class);
        startActivity(i);
    }

    public void departmentMenu(View view){
        Intent i = new Intent(this, DepartmentPage.class);
        startActivity(i);
    }
}