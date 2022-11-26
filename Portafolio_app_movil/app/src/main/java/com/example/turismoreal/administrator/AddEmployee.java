package com.example.turismoreal.administrator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.turismoreal.R;
import com.example.turismoreal.Services.UserServices;
import com.example.turismoreal.SplashScreen;
import com.example.turismoreal.models.OneResponse;
import com.example.turismoreal.models.User;
import com.google.gson.Gson;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddEmployee extends AppCompatActivity {

    EditText employeeRut, employeeName, employeeLastName, employeeEmail;
    RadioButton adminRole, funcRole;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_TurismoReal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        adminRole = findViewById(R.id.adminRole);
        funcRole = findViewById(R.id.funcRole);
        employeeRut = findViewById(R.id.employeeRut);
        employeeName = findViewById(R.id.employeeName);
        employeeLastName = findViewById(R.id.employeeLastName);
        employeeEmail = findViewById(R.id.employeeEmail);
    }



    public void addEmployee(View view){
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setCancelable(false);
        final View loading = getLayoutInflater().inflate(R.layout.loading_gif, null);
        dialogBuilder.setView(loading);
        dialog = dialogBuilder.create();

        if (employeeRut.getText().toString().isEmpty()){
            Toast.makeText(this, "El rut no puede estar vacío", Toast.LENGTH_SHORT).show();
        }else if (employeeName.getText().toString().isEmpty()){
            Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
        }else if (employeeLastName.getText().toString().isEmpty()){
            Toast.makeText(this, "El apellido no puede estar vacío", Toast.LENGTH_SHORT).show();
        }else if (employeeEmail.getText().toString().isEmpty()){
            Toast.makeText(this, "El email no puede estar vacío", Toast.LENGTH_SHORT).show();
        }else{
            int userRole = adminRole.isChecked() ? 1 : 2;
            dialog.show();
            try {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(SplashScreen.URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                UserServices userServices = retrofit.create(UserServices.class);
                String jsonData = "{\"rut\": \""+employeeRut.getText().toString()+"\", \"name\":\""+employeeName.getText().toString()+"\", \"last_name\":\""+employeeLastName.getText().toString()+"\", \"email\":\""+employeeEmail.getText().toString()+"\", \"user_role\":"+userRole+"}";
                RequestBody requestBody = RequestBody.create(MediaType.parse("aplication/json"), jsonData);
                userServices.addEmployee(requestBody).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            dialog.dismiss();
                            Gson g = new Gson();
                            OneResponse post_response = g.fromJson(response.body().string(), OneResponse.class);
                            Integer user_id = post_response.getResponse();
                            if (user_id > 0) {
                                Toast.makeText(AddEmployee.this, "Empleado Agregado correctamente", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(AddEmployee.this, Users.class);
                                startActivity(i);
                                finish();
                            }else{
                                Toast.makeText(AddEmployee.this, "Error al agregar el empleado", Toast.LENGTH_SHORT).show();
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

    public void adminRole(View view) {
        if (adminRole.isChecked()) {
            funcRole.setChecked(false);
        }
    }
    public void funcRole(View view) {
        if (funcRole.isChecked()) {
            adminRole.setChecked(false);
        }
    }


    public void goBack(View view){
        Intent i = new Intent(this, Users.class);
        startActivity(i);
        finish();
    }
}