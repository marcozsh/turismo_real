package com.example.turismoreal.administrator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
    private Integer service_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_TurismoReal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_extra_service);

        swicthButton = findViewById(R.id.switchAvaible);
        serviceName = findViewById(R.id.serviceName);
        servicePrice = findViewById(R.id.servicePrice);
        serviceLocation = findViewById(R.id.serviceLocation);
        serviceTypeTour = findViewById(R.id.serviceTypeTours);
        serviceTypeTour.setChecked(true);
        serviceTypeTransport = findViewById(R.id.serviceTransportType);

        serviceName.requestFocus();
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
                String jsonData = "{\"service_type_id\":"+serviceType+",\"name\":\""+name+"\",\"price\": "+price+",\"location\": \""+location+"\",\"available\":"+avalible+"}";
                RequestBody requestBody = RequestBody.create(MediaType.parse("aplication/json"), jsonData);
                extraServices.addExtraService(requestBody).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        try {
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