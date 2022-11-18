package com.example.turismoreal.administrator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.turismoreal.R;
import com.example.turismoreal.models.OneResponse;
import com.example.turismoreal.SplashScreen;
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

public class AddExtraService extends AppCompatActivity {


    private Switch swicthButton;
    private EditText serviceName;
    private EditText servicePrice;
    private EditText serviceLocation;
    private RadioButton serviceTypeTour;
    private RadioButton serviceTypeTransport;
    private Button btnModificar, btnAgregar;
    private Integer service_id;
    private int s_id;
    private String s_name = "";
    private int s_price = 0;
    private String s_location = "";
    private Boolean s_status = false;
    private int s_service_type = 0;


    private LinearLayout principalLayout;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_TurismoReal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_extra_service);

        btnAgregar = findViewById(R.id.btnAgregar);
        btnModificar = findViewById(R.id.btnModificar);
        swicthButton = findViewById(R.id.switchAvaible);
        serviceName = findViewById(R.id.serviceName);
        servicePrice = findViewById(R.id.servicePrice);
        serviceLocation = findViewById(R.id.serviceLocation);
        serviceTypeTour = findViewById(R.id.serviceTypeTours);
        serviceTypeTransport = findViewById(R.id.serviceTransportType);

        btnAgregar.setVisibility(View.VISIBLE);
        serviceName.requestFocus();
        SharedPreferences preferences = getSharedPreferences("extra_services", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        s_id = preferences.getInt("service_id", 0);

        if (s_id > 0){
            btnAgregar.setVisibility(View.GONE);
            btnModificar.setVisibility(View.VISIBLE);
            s_name = preferences.getString("service_name", "");
            s_price = preferences.getInt("service_price", 0);
            s_location = preferences.getString("service_location", "");
            s_status = preferences.getBoolean("service_disponibility", false);
            s_service_type = preferences.getInt("service_type", 0);

            serviceName.setText(s_name);
            serviceName.setFocusable(false);
            servicePrice.setText(Integer.toString(s_price));
            serviceLocation.setText(s_location);
            if (s_service_type == 1) {
                serviceTypeTour.setChecked(true);
            } else {
                serviceTypeTransport.setChecked(true);
            }
            serviceTypeTour.setClickable(false);
            serviceTypeTransport.setClickable(false);
            swicthButton.setChecked(s_status);
        }

    }

    public void serviceType(View view) {
        if (serviceTypeTour.isChecked()) {
            serviceTypeTransport.setChecked(false);
        }
    }
    public void setServiceTypeTransport(View view) {
        if (serviceTypeTour.isChecked()) {
            serviceTypeTour.setChecked(false);
        }
    }

    public void addService(View view){
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setCancelable(false);
        final View loading = getLayoutInflater().inflate(R.layout.loading_gif, null);
        dialogBuilder.setView(loading);
        dialog = dialogBuilder.create();
        Integer avalible = swicthButton.isChecked() ? 1 : 0;
        Integer serviceType = serviceTypeTour.isChecked() ? 1 : 2;
        if (serviceName.getText().toString().isEmpty()){
            Toast.makeText(AddExtraService.this, "El nombre del servicio no puede ser vacío", Toast.LENGTH_SHORT).show();
        }else if (servicePrice.getText().toString().isEmpty()){
            Toast.makeText(AddExtraService.this, "Debe especificar un precio", Toast.LENGTH_SHORT).show();
        }else if (serviceLocation.getText().toString().isEmpty() && serviceType == 1){
            Toast.makeText(AddExtraService.this, "Debe especificar una localización para Tours", Toast.LENGTH_SHORT).show();
        }else {
            String name = serviceName.getText().toString();
            Integer price = Integer.parseInt(servicePrice.getText().toString());
            String location = serviceLocation.getText().toString();
            dialog.show();
            try {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(SplashScreen.URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                com.example.turismoreal.Services.ExtraServices extraServices = retrofit.create(com.example.turismoreal.Services.ExtraServices.class);
                String jsonData = "{\"service_type_id\":"+serviceType+",\"name\":\""+name+"\",\"price\": "+price+",\"location\": \""+location+"\",\"available\":"+avalible+"}";
                RequestBody requestBody = RequestBody.create(MediaType.parse("aplication/json"), jsonData);
                extraServices.addExtraService(requestBody).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        try {
                            dialog.dismiss();
                            Gson g = new Gson();
                            OneResponse post_response = g.fromJson(response.body().string(), OneResponse.class);
                            service_id = post_response.getResponse();
                            if (service_id != -1) {
                                Toast.makeText(AddExtraService.this, "Servicio Agregado correctamente", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(AddExtraService.this, ExtraServicePage.class);
                                startActivity(i);
                                finish();
                            }else{
                                Toast.makeText(AddExtraService.this, "Error al agregar el servicio extra", Toast.LENGTH_SHORT).show();
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

    public void updateService(View view){
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setCancelable(false);
        final View loading = getLayoutInflater().inflate(R.layout.loading_gif, null);
        dialogBuilder.setView(loading);
        dialog = dialogBuilder.create();
        dialog.show();
        Integer avalible = swicthButton.isChecked() ? 1 : 0;
        Integer serviceType = serviceTypeTour.isChecked() ? 1 : 2;
        if (serviceName.getText().toString().isEmpty()){
            Toast.makeText(AddExtraService.this, "El nombre del servicio no puede ser vacío", Toast.LENGTH_SHORT).show();
        }else if (servicePrice.getText().toString().isEmpty()){
            Toast.makeText(AddExtraService.this, "Debe especificar un precio", Toast.LENGTH_SHORT).show();
        }else if (serviceLocation.getText().toString().isEmpty() && serviceType == 1){
            Toast.makeText(AddExtraService.this, "Debe especificar una localización para Tours", Toast.LENGTH_SHORT).show();
        }else {
            String name = serviceName.getText().toString();
            Integer price = Integer.parseInt(servicePrice.getText().toString());
            String location = serviceLocation.getText().toString();
            try {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(SplashScreen.URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                com.example.turismoreal.Services.ExtraServices extraServices = retrofit.create(com.example.turismoreal.Services.ExtraServices.class);
                String jsonData = "{\"id\":"+s_id+",\"location\":\""+location+"\",\"status\": "+avalible+",\"price\":"+price+"}";
                RequestBody requestBody = RequestBody.create(MediaType.parse("aplication/json"), jsonData);
                extraServices.updateExtraService(requestBody).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            dialog.dismiss();
                            Gson g = new Gson();
                            OneResponse post_response = g.fromJson(response.body().string(), OneResponse.class);
                            service_id = post_response.getResponse();
                            if (service_id != -1) {
                                Toast.makeText(AddExtraService.this, "Servicio modificado correctamente", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(AddExtraService.this, ExtraServicePage.class);
                                startActivity(i);
                                finish();
                            }else{
                                Toast.makeText(AddExtraService.this, "Error al modificar el servicio extra", Toast.LENGTH_SHORT).show();
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

    public void switchButton(View view){
        if (swicthButton.isChecked()){
            swicthButton.setText("Disponible");
        }else{
            swicthButton.setText("No Disponible");
        }

    }

    public void goBack(View view){
        Intent i = new Intent(this, ExtraServicePage.class);
        startActivity(i);
        finish();
    }
}