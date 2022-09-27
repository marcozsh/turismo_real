package com.example.turismoreal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

//sql imports
import java.io.IOException;

//login interface
import com.example.turismoreal.Services.LoginService;
import com.example.turismoreal.Services.SendLogin;
import com.example.turismoreal.models.Employee;
import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pl.droidsonroids.gif.GifTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class login extends AppCompatActivity {

    private EditText user;
    private EditText password;
    private Integer user_id;
    private GifTextView gib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.Theme_TurismoReal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        user = findViewById(R.id.username);
        password = findViewById(R.id.password);

        gib =  findViewById(R.id.loading);


        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

    }

    private void saveSession(Integer userId, String fullName, String userRol, String sessionId ){
        SharedPreferences preferences = getSharedPreferences("current_session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("userId", userId);
        editor.putString("fullName", fullName);
        editor.putString("userRol", userRol);
        editor.putString("sessionId", sessionId);
        editor.commit();
    }

    public void buttonConnectToOracleDB(View view){

        String username = user.getText().toString();
        String pass =password.getText().toString();
        if (!username.isEmpty() && !pass.isEmpty()){
            gib.setVisibility(view.VISIBLE);
            try {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(splashScreen.URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                LoginService loginService = retrofit.create(LoginService.class);
                String jsonData = "{\"login\":2,\"username\":\""+username+"\",\"password\":\""+pass+"\"}";
                RequestBody requestBody = RequestBody.create(MediaType.parse("aplication/json"), jsonData);
                loginService.login(requestBody).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try{
                            Gson g = new Gson();
                            SendLogin sendLogin = g.fromJson(response.body().string(), SendLogin.class);
                            user_id = sendLogin.getUserId();
                            if (user_id > 0){
                                String jsonData = "{\"user_id\":"+user_id+"}";
                                RequestBody requestBody = RequestBody.create(MediaType.parse("aplication/json"), jsonData);

                                loginService.loginEmployee(requestBody).enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        try {
                                            Gson g = new Gson();
                                            Employee employee = g.fromJson(response.body().string(), Employee.class);
                                            saveSession(user_id, employee.getFullName(), employee.getPosition(),employee.getSessionId());
                                            Toast.makeText(login.this, "Bienvenido!", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(login.this, landingPage.class);
                                            startActivity(i);
                                            finish();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        t.printStackTrace();
                                    }
                                });


                            }else{
                                Toast.makeText(login.this, "Usuario Incorrecto", Toast.LENGTH_LONG).show();
                                gib.setVisibility(view.INVISIBLE);
                            }
                        }catch(NullPointerException | IOException e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        t.printStackTrace();
                        System.out.println(t.getMessage());
                    }
                });
                //Toast.makeText(this, user_id.toString(), Toast.LENGTH_LONG).show();


            }
            catch (Exception e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
                System.out.println(e.toString());
            }
        }else{
            Toast.makeText(this, "Los campos son obligatorios", Toast.LENGTH_LONG).show();
        }
    }

}