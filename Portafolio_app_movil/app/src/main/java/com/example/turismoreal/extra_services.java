package com.example.turismoreal;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.turismoreal.Services.ExtraServices;
import com.example.turismoreal.models.ExtraService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class extra_services extends AppCompatActivity {

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
                        Toast.makeText(extra_services.this, response.code(), Toast.LENGTH_LONG);
                        return;
                    }
                    List<ExtraService> allServices = response.body();
                    for (ExtraService services : allServices) {
                        //title
                        TableLayout titleTableLauout = new TableLayout(extra_services.this);
                        TableRow titleBox = new TableRow(extra_services.this);
                        TextView nombre = new TextView(extra_services.this);

                        nombre.setText(services.getName());
                        nombre.setTextColor(Color.parseColor("#0090FF"));

                        principalLayout.addView(titleTableLauout);
                        titleTableLauout.addView(titleBox);
                        titleBox.addView(nombre);

                        //atributes
                        TableLayout principalTable = new TableLayout(extra_services.this);
                        TableRow containerRow = new TableRow(extra_services.this);
                        TableRow containerRow2 = new TableRow(extra_services.this);
                        TableRow containerRow3 = new TableRow(extra_services.this);
                        containerRow.setGravity(Gravity.LEFT);
                        TableRow atributeRow = new TableRow(extra_services.this);
                        TableRow atributeRow2 = new TableRow(extra_services.this);
                        TableRow atributeRow3 = new TableRow(extra_services.this);

                        TableRow spaceRow = new TableRow(extra_services.this);
                        TableRow spaceRow2 = new TableRow(extra_services.this);
                        TableRow spaceRow3 = new TableRow(extra_services.this);

                        TableRow valueRow = new TableRow(extra_services.this);
                        TableRow valueRow2 = new TableRow(extra_services.this);
                        TableRow valueRow3 = new TableRow(extra_services.this);

                        TextView atribute1 = new TextView(extra_services.this);
                        TextView atribute2 = new TextView(extra_services.this);
                        TextView atribute3 = new TextView(extra_services.this);

                        TextView space = new TextView(extra_services.this);

                        TextView value1 = new TextView(extra_services.this);
                        TextView value2 = new TextView(extra_services.this);
                        TextView value3 = new TextView(extra_services.this);

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
        Intent i = new Intent(this, landing_page.class);
        startActivity(i);
        finish();
    }
}