package com.example.turismoreal.administrator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.turismoreal.R;
import com.example.turismoreal.LandingPage;
import com.example.turismoreal.Services.ExtraServices;
import com.example.turismoreal.SplashScreen;
import com.example.turismoreal.models.ExtraService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExtraServicePage extends AppCompatActivity {

    private LinearLayout principalLayout;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_TurismoReal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_services);
        principalLayout = findViewById(R.id.principalLayout);
        listExtraService();
    }


    public void listExtraService(){
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
            ExtraServices extraServices = retrofit.create(ExtraServices.class);
            Call<List<ExtraService>> AllServices = extraServices.getExtraServices();
            AllServices.enqueue(new Callback<List<ExtraService>>() {
                @Override
                public void onResponse(Call<List<ExtraService>> call, Response<List<ExtraService>> response) {
                    dialog.dismiss();
                    List<ExtraService> allServices = response.body();
                    for (ExtraService services : allServices) {
                        if (services.getId() == 0){
                            continue;
                        }
                        //title
                        TableLayout titleTableLauout = new TableLayout(ExtraServicePage.this);
                        TableRow titleBox = new TableRow(ExtraServicePage.this);
                        TextView nombre = new TextView(ExtraServicePage.this);

                        nombre.setText(services.getName());
                        nombre.setTextColor(Color.parseColor("#0090FF"));

                        principalLayout.addView(titleTableLauout);
                        titleTableLauout.addView(titleBox);
                        titleBox.addView(nombre);

                        //atributes
                        TableLayout principalTable = new TableLayout(ExtraServicePage.this);
                        TableRow containerRow = new TableRow(ExtraServicePage.this);
                        TableRow containerRow2 = new TableRow(ExtraServicePage.this);
                        TableRow containerRow3 = new TableRow(ExtraServicePage.this);
                        containerRow.setGravity(Gravity.LEFT);
                        TableRow atributeRow = new TableRow(ExtraServicePage.this);
                        TableRow atributeRow2 = new TableRow(ExtraServicePage.this);
                        TableRow atributeRow3 = new TableRow(ExtraServicePage.this);

                        TableRow spaceRow = new TableRow(ExtraServicePage.this);
                        TableRow spaceRow2 = new TableRow(ExtraServicePage.this);
                        TableRow spaceRow3 = new TableRow(ExtraServicePage.this);

                        TableRow valueRow = new TableRow(ExtraServicePage.this);
                        TableRow valueRow2 = new TableRow(ExtraServicePage.this);
                        TableRow valueRow3 = new TableRow(ExtraServicePage.this);

                        TextView atribute1 = new TextView(ExtraServicePage.this);
                        TextView atribute2 = new TextView(ExtraServicePage.this);
                        TextView atribute3 = new TextView(ExtraServicePage.this);

                        TextView space = new TextView(ExtraServicePage.this);

                        TextView value1 = new TextView(ExtraServicePage.this);
                        TextView value2 = new TextView(ExtraServicePage.this);
                        TextView value3 = new TextView(ExtraServicePage.this);

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

                        principalTable.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SharedPreferences preferences = getSharedPreferences("extra_services", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putInt("service_id", services.getId());
                                editor.putString("service_name", services.getName());
                                editor.putInt("service_price", services.getPrice());
                                editor.putString("service_location", services.getLocation());
                                editor.putBoolean("service_disponibility", services.isAvailable());
                                editor.putInt("service_type", services.getServiceTypeId());
                                editor.commit();
                                editExtraService();
                            }
                        });
                    }
                }
                @Override
                public void onFailure(Call<List<ExtraService>> call, Throwable t) {
                    dialog.dismiss();
                    System.out.println(t.getMessage());
                }
            });


        }catch (Exception e){

            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage());
        }
    }

    public void editExtraService(){
        Intent i = new Intent(this, AddExtraService.class);
        startActivity(i);
    }


    public void addService (View view){
        SharedPreferences preferences = getSharedPreferences("extra_services", Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
        Intent i = new Intent(this, AddExtraService.class);
        startActivity(i);
    }


    public void goBack(View view){
        Intent i = new Intent(this, LandingPage.class);
        startActivity(i);
        finish();
    }

    public void departmentMenu(View view){
        Intent i = new Intent(this, DepartmentPage.class);
        startActivity(i);
        finish();
    }
}