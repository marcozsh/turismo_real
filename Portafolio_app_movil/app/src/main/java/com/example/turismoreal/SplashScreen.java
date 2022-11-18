package com.example.turismoreal;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Handler;
import android.os.StrictMode;
import android.widget.Toast;


import com.example.turismoreal.Services.LoginService;
import com.example.turismoreal.models.SendLogin;
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

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    private String session_id;


    public static final String URL = "http://192.168.0.6:8000/api_mobile/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.Theme_TurismoReal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);



        new Handler().postDelayed(() -> {
            try{
                SharedPreferences preferences = getSharedPreferences("current_session", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                Integer id = preferences.getInt("userId", 0);
                if (id == 0){
                    Intent i = new Intent(this, Login.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    LoginService loginService = retrofit.create(LoginService.class);
                    String jsonData = "{\"login\":1,\"user_id\":"+id+"}";
                    RequestBody requestBody = RequestBody.create(MediaType.parse("aplication/json"), jsonData);
                    loginService.login(requestBody).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try{
                                Gson g = new Gson();
                                SendLogin sendLogin = g.fromJson(response.body().string(), SendLogin.class);
                                session_id = sendLogin.getSession();
                                if (session_id.equals("NO SESSION") || session_id.equals("UNREGISTER")){
                                    Intent i = new Intent(SplashScreen.this, Login.class);
                                    startActivity(i);
                                }else {
                                    Toast.makeText(SplashScreen.this, "Bienvenido!", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(SplashScreen.this, LandingPage.class);
                                    startActivity(i);
                                }
                                finish();

                            }
                            catch (NullPointerException | IOException e){
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(SplashScreen.this, "No se puede conectar con la base de datos", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(SplashScreen.this, Login.class);
                            startActivity(i);
                        }
                    });

                }
            }
            catch (Exception e){
                e.printStackTrace();
                System.out.println(e.getMessage());
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }, 2000);
    }

}