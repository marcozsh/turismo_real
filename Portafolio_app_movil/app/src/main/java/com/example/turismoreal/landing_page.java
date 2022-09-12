package com.example.turismoreal;

import static com.example.turismoreal.login.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.turismoreal.Services.LoginService;
import com.example.turismoreal.models.CustomError;
import com.google.gson.Gson;

public class landing_page extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextView logutMessage;
    private Button yes, no;

    private TextView userRol;
    private TextView fullName;

    private Integer user_id = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_TurismoReal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page);

        SharedPreferences preferences = getSharedPreferences("current_session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        String rol = preferences.getString("userRol", "NO SESSION");
        String userName = preferences.getString("fullName", "NO SESSION");
        Integer userId = preferences.getInt("userId", 0);

        userRol = findViewById(R.id.txtRol);
        fullName = findViewById(R.id.userName);
        userRol.setText(rol);
        fullName.setText(userName);
        user_id = userId;
    }

    public void popOutLogout(View view){
        dialogBuilder = new AlertDialog.Builder(this);
        final View logoutView = getLayoutInflater().inflate(R.layout.logout_popout, null);
        logutMessage = (TextView) logoutView.findViewById(R.id.logoutMessage);
        yes = (Button) logoutView.findViewById(R.id.btnYes);
        no = (Button) logoutView.findViewById(R.id.btnNo);

        dialogBuilder.setView(logoutView);
        dialog = dialogBuilder.create();
        dialog.show();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(landing_page.this, "Saliendo", Toast.LENGTH_SHORT).show();
                logout();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    public void logout(){
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(splashScreen.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            String jsonData = "{\"user_id\":"+user_id+"}";
            RequestBody requestBody = RequestBody.create(MediaType.parse("aplication/json"), jsonData);
            LoginService loginService = retrofit.create(LoginService.class);

            loginService.logOutEmployee(requestBody).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Gson gson = new Gson();
                    try {
                        CustomError error = gson.fromJson(response.body().string(), CustomError.class);
                        String respuesta = error.getError();
                        if (respuesta.equals("NO ERROR")){
                            SharedPreferences preferences = getSharedPreferences("current_session", Context.MODE_PRIVATE);
                            preferences.edit().clear().apply();
                            Intent i = new Intent(landing_page.this, login.class);
                            startActivity(i);
                            finish();
                        }else{
                            Toast.makeText(landing_page.this, "Error con el servidor", Toast.LENGTH_SHORT).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }



    public void extraServicesMenu(View view){
        Intent i = new Intent(this, extra_services.class);
        startActivity(i);
        finish();
    }
}