package com.example.turismoreal.staff;

import static com.example.turismoreal.Login.MD5;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.turismoreal.LandingPage;
import com.example.turismoreal.R;
import com.example.turismoreal.Services.UserServices;
import com.example.turismoreal.SplashScreen;
import com.example.turismoreal.administrator.AddEmployee;
import com.example.turismoreal.administrator.Users;
import com.example.turismoreal.models.OneResponse;
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

public class ChangePassword extends AppCompatActivity {


    EditText  newPassword, newPassword2;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        newPassword = findViewById(R.id.newPassword);
        newPassword2 = findViewById(R.id.newPassword2);

    }

    public void updatePassword(View view){

        SharedPreferences preferences = getSharedPreferences("current_session", Context.MODE_PRIVATE);
        Integer userId = preferences.getInt("userId", 0);
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setCancelable(false);
        final View loading = getLayoutInflater().inflate(R.layout.loading_gif, null);
        dialogBuilder.setView(loading);
        dialog = dialogBuilder.create();
        if (newPassword.getText().toString().isEmpty()){
            Toast.makeText(this, "Ingresa la contraseña", Toast.LENGTH_SHORT).show();
        } else if (!newPassword.getText().toString().equals(newPassword2.getText().toString())){
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        }else{
            dialog.show();
            try {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(SplashScreen.URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                UserServices userServices = retrofit.create(UserServices.class);
                String jsonData = "{\"user_id\":"+userId+", \"new_password\": \""+MD5(newPassword2.getText().toString())+"\"}";
                RequestBody requestBody = RequestBody.create(MediaType.parse("aplication/json"), jsonData);
                userServices.updatePassword(requestBody).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            dialog.dismiss();
                            Gson g = new Gson();
                            OneResponse post_response = g.fromJson(response.body().string(), OneResponse.class);
                            Integer user_id = post_response.getResponse();
                            if (user_id > 0) {
                                Toast.makeText(ChangePassword.this, "Contraseña modificada correctamente", Toast.LENGTH_SHORT).show();
                                newPassword.setText("");
                                newPassword2.setText("");
                            }else{
                                Toast.makeText(ChangePassword.this, "Error al modificar la contraseña", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(e.getMessage());
            }
        }

    }

    public void goBack(View view){
        Intent i = new Intent(this, LandingPage.class);
        startActivity(i);
        finish();
    }
}