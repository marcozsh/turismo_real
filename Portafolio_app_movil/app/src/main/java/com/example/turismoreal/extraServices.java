package com.example.turismoreal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.turismoreal.Services.ExtraServices;
import com.example.turismoreal.models.ExtraService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class extraServices extends AppCompatActivity {

    private LinearLayout principalLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_TurismoReal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_services);
        principalLayout = findViewById(R.id.principalLayout);
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(splashScreen.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ExtraServices extraServices = retrofit.create(ExtraServices.class);
            Call<List<ExtraService>> AllServices = extraServices.getExtraServices();
            AllServices.enqueue(new Callback<List<ExtraService>>() {

                @Override
                public void onResponse(Call<List<ExtraService>> call, Response<List<ExtraService>> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(com.example.turismoreal.extraServices.this, response.code(), Toast.LENGTH_LONG);
                        return;
                    }
                    List<ExtraService> allServices = response.body();
                    for (ExtraService services : allServices) {
                        //title
                        TableLayout titleTableLauout = new TableLayout(com.example.turismoreal.extraServices.this);
                        TableRow titleBox = new TableRow(com.example.turismoreal.extraServices.this);
                        TextView nombre = new TextView(com.example.turismoreal.extraServices.this);

                        nombre.setText(services.getName());
                        nombre.setTextColor(Color.parseColor("#0090FF"));

                        principalLayout.addView(titleTableLauout);
                        titleTableLauout.addView(titleBox);
                        titleBox.addView(nombre);

                        //atributes
                        TableLayout principalTable = new TableLayout(com.example.turismoreal.extraServices.this);
                        TableRow containerRow = new TableRow(com.example.turismoreal.extraServices.this);
                        TableRow containerRow2 = new TableRow(com.example.turismoreal.extraServices.this);
                        TableRow containerRow3 = new TableRow(com.example.turismoreal.extraServices.this);
                        containerRow.setGravity(Gravity.LEFT);
                        TableRow atributeRow = new TableRow(com.example.turismoreal.extraServices.this);
                        TableRow atributeRow2 = new TableRow(com.example.turismoreal.extraServices.this);
                        TableRow atributeRow3 = new TableRow(com.example.turismoreal.extraServices.this);

                        TableRow spaceRow = new TableRow(com.example.turismoreal.extraServices.this);
                        TableRow spaceRow2 = new TableRow(com.example.turismoreal.extraServices.this);
                        TableRow spaceRow3 = new TableRow(com.example.turismoreal.extraServices.this);

                        TableRow valueRow = new TableRow(com.example.turismoreal.extraServices.this);
                        TableRow valueRow2 = new TableRow(com.example.turismoreal.extraServices.this);
                        TableRow valueRow3 = new TableRow(com.example.turismoreal.extraServices.this);

                        TextView atribute1 = new TextView(com.example.turismoreal.extraServices.this);
                        TextView atribute2 = new TextView(com.example.turismoreal.extraServices.this);
                        TextView atribute3 = new TextView(com.example.turismoreal.extraServices.this);

                        TextView space = new TextView(com.example.turismoreal.extraServices.this);

                        TextView value1 = new TextView(com.example.turismoreal.extraServices.this);
                        TextView value2 = new TextView(com.example.turismoreal.extraServices.this);
                        TextView value3 = new TextView(com.example.turismoreal.extraServices.this);

                        atribute1.setText("Localización");
                        atribute2.setText("Precio");
                        atribute3.setText("Disponibilidad");

                        space.setText("                       ");

                        value1.setTextColor(Color.parseColor("#000000"));
                        value2.setTextColor(Color.parseColor("#000000"));
                        value3.setTextColor(Color.parseColor("#000000"));

                        String location = services.getLocation().isEmpty() ? "SIN REGISTRO" : services.getLocation();
                        value1.setText(location);
                        String price = "$" + services.getPrice();
                        value2.setText(price);
                        String isAvailable = services.isAvailable() ? "Disponible" : "No Disponible";
                        value3.setText(isAvailable);

                        principalLayout.addView(principalTable);

                        principalTable.addView(containerRow);
                        principalTable.addView(containerRow2);
                        principalTable.addView(containerRow3);

                        containerRow.addView(atributeRow);
                        containerRow2.addView(atributeRow2);
                        containerRow3.addView(atributeRow3);

                        containerRow.addView(spaceRow);
                        containerRow2.addView(spaceRow2);
                        containerRow3.addView(spaceRow3);

                        containerRow.addView(valueRow);
                        containerRow2.addView(valueRow2);
                        containerRow3.addView(valueRow3);

                        atributeRow.addView(atribute1);
                        atributeRow2.addView(atribute2);
                        atributeRow3.addView(atribute3);

                        valueRow.addView(value1);
                        valueRow2.addView(value2);
                        valueRow3.addView(value3);
                        spaceRow.addView(space);
                    }
                }

                @Override
                public void onFailure(Call<List<ExtraService>> call, Throwable t) {

                }
            });
        }catch (Exception e){

            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage());
        }


    }


    public void addService (View view){
        Intent i = new Intent(this, addExtraService.class);
        startActivity(i);
        finish();
    }

    public void goBack(View view){
        Intent i = new Intent(this, landingPage.class);
        startActivity(i);
        finish();
    }
}