package com.example.turismoreal.staff;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.turismoreal.LandingPage;
import com.example.turismoreal.R;
import com.example.turismoreal.Services.DepartmentService;
import com.example.turismoreal.Services.ExtraServices;
import com.example.turismoreal.Services.ReservationService;
import com.example.turismoreal.SplashScreen;
import com.example.turismoreal.administrator.AddDepartmentInventory;
import com.example.turismoreal.models.Department;
import com.example.turismoreal.models.ExtraService;
import com.example.turismoreal.models.OneResponse;
import com.example.turismoreal.models.ProductType;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.regex.Pattern;

public class ReservationExtraServices extends AppCompatActivity {

    private Spinner extraServiceSpinner;
    private TextView totalServices;

    private Integer extraServiceTotal = 0;
    private ArrayList<Integer> idLists = new ArrayList<Integer>() ;
    private ArrayList<Integer> idListsNews = new ArrayList<Integer>() ;
    private LinearLayout principalLayout;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_TurismoReal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_extra_services);
        extraServiceSpinner = findViewById(R.id.extraServiceSpinner);
        principalLayout = findViewById(R.id.principalLayout);
        totalServices = findViewById(R.id.totalServices);
        listAllServices();
        sumTotal(0);
        extraServiceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String service = extraServiceSpinner.getSelectedItem().toString();
                if (!service.equals("Seccionar un servicio extra")){
                    SharedPreferences preferences = getSharedPreferences("reservation_details", Context.MODE_PRIVATE);
                    Integer reservationId = preferences.getInt("reservationId", 0);


                    dialogBuilder = new AlertDialog.Builder(ReservationExtraServices.this);
                    dialogBuilder.setCancelable(false);
                    final View loading = getLayoutInflater().inflate(R.layout.loading_gif, null);
                    dialogBuilder.setView(loading);
                    dialog = dialogBuilder.create();
                    dialog.show();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(SplashScreen.URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    ReservationService reservationService = retrofit.create(ReservationService.class);
                    String jsonData = "{\"reservation_id\":" + reservationId + "}";
                    RequestBody requestBody = RequestBody.create(MediaType.parse("aplicaton/json"), jsonData);
                    reservationService.getReservationExtraServiceId(requestBody).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                int serviceId = -1;
                                ExtraService[] extraService = new Gson().fromJson(response.body().string(), ExtraService[].class);
                                dialog.dismiss();
                                for(ExtraService ex : extraService){
                                    System.out.println(ex.getId());
                                    idLists.add(ex.getId());
                                }
                                serviceId = Integer.parseInt(service.replaceAll("[^0-9]",""));
                                if (!idLists.contains(serviceId)){
                                    listService(serviceId);
                                }else{
                                    Toast.makeText(ReservationExtraServices.this, "El servicio indicado ya está seleccionado", Toast.LENGTH_SHORT).show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {}

                    });

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }
    public void addServicesToReservation (View view){
        SharedPreferences preferences = getSharedPreferences("reservation_details", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Integer reservationId = preferences.getInt("reservationId", 0);

        if (idListsNews.size() == 0){
            Toast.makeText(ReservationExtraServices.this, "Debe seleccionar al menos un servicio extra", Toast.LENGTH_SHORT).show();
        }else{
            if(reservationId != 0){
                dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setCancelable(false);
                final View loading = getLayoutInflater().inflate(R.layout.loading_gif, null);
                dialogBuilder.setView(loading);
                dialog = dialogBuilder.create();
                dialog.show();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(SplashScreen.URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                ReservationService reservationService = retrofit.create(ReservationService.class);
                String jsonData = "{\"reservation_id\":" +reservationId + ", \"services\":"+idListsNews+"}";
                RequestBody requestBody = RequestBody.create(MediaType.parse("aplicaton/json"), jsonData);

                reservationService.addExtraServiceToReservation(requestBody).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        OneResponse post_response = null;
                        try {
                            dialog.dismiss();
                            post_response = new Gson().fromJson(response.body().string(), OneResponse.class);
                            int reservationId = post_response.getResponse();
                            if (reservationId != 0){
                                Toast.makeText(ReservationExtraServices.this, "Servicios Extras agregados correctamente", Toast.LENGTH_SHORT).show();
                                extraServiceSpinner.setSelection(0);
                                principalLayout.removeAllViews();
                                sumTotal(0);
                            }else{
                                Toast.makeText(ReservationExtraServices.this, "Hubo un error al momento de agregar los servicios extras", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }else{
                Toast.makeText(ReservationExtraServices.this, "Error al agregar el servicio extra", Toast.LENGTH_SHORT).show();
            }
        }



    }
    public void listService(Integer serviceId){
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setCancelable(false);
        final View loading = getLayoutInflater().inflate(R.layout.loading_gif, null);
        dialogBuilder.setView(loading);
        dialog = dialogBuilder.create();
        dialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SplashScreen.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ExtraServices extraService = retrofit.create(ExtraServices.class);
        String jsonData = "{\"service_id\":\""+serviceId+"\"}";
        RequestBody requestBody = RequestBody.create(MediaType.parse("aplicaton/json"), jsonData);
        extraService.getExtraServiceById(requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    dialog.dismiss();
                    ExtraService[] extraServices = new Gson().fromJson(response.body().string(), ExtraService[].class);
                    for (ExtraService services: extraServices){
                        idListsNews.add(services.getId());
                        //title
                        TableLayout titleTableLauout = new TableLayout(ReservationExtraServices.this);
                        TableRow titleBox = new TableRow(ReservationExtraServices.this);
                        TextView nombre = new TextView(ReservationExtraServices.this);
                        nombre.setText(services.getName());
                        nombre.setTextColor(Color.parseColor("#0090FF"));
                        principalLayout.addView(titleTableLauout);
                        titleTableLauout.addView(titleBox);
                        titleBox.addView(nombre);
                        titleBox.setGravity(Gravity.CENTER);

                        //atributes
                        TableLayout principalTable = new TableLayout(ReservationExtraServices.this);
                        TableRow containerRow = new TableRow(ReservationExtraServices.this);
                        TableRow containerRow2 = new TableRow(ReservationExtraServices.this);
                        containerRow.setGravity(Gravity.CENTER);
                        containerRow2.setGravity(Gravity.CENTER);
                        TableRow atributeRow = new TableRow(ReservationExtraServices.this);
                        TableRow atributeRow2 = new TableRow(ReservationExtraServices.this);

                        TableRow spaceRow = new TableRow(ReservationExtraServices.this);
                        TableRow spaceRow2 = new TableRow(ReservationExtraServices.this);

                        TableRow valueRow = new TableRow(ReservationExtraServices.this);
                        TableRow valueRow2 = new TableRow(ReservationExtraServices.this);

                        TextView atribute1 = new TextView(ReservationExtraServices .this);
                        TextView atribute2 = new TextView(ReservationExtraServices.this);

                        TextView space = new TextView(ReservationExtraServices.this);

                        TextView value1 = new TextView(ReservationExtraServices.this);
                        TextView value2 = new TextView(ReservationExtraServices.this);

                        atribute1.setText("Localización");
                        atribute2.setText("Precio");

                        space.setText("                       ");

                        value1.setTextColor(Color.parseColor("#000000"));
                        value2.setTextColor(Color.parseColor("#000000"));

                        String location = services.getLocation().isEmpty() ? "SIN REGISTRO" : services.getLocation();
                        value1.setText(location);
                        String price = "$" + services.getPrice();
                        value2.setText(price);

                        principalLayout.addView(principalTable);

                        principalTable.addView(containerRow);
                        principalTable.addView(containerRow2);

                        containerRow.addView(atributeRow);
                        containerRow2.addView(atributeRow2);

                        containerRow.addView(spaceRow);
                        containerRow2.addView(spaceRow2);

                        containerRow.addView(valueRow);
                        containerRow2.addView(valueRow2);

                        atributeRow.addView(atribute1);
                        atributeRow2.addView(atribute2);

                        valueRow.addView(value1);
                        valueRow2.addView(value2);
                        spaceRow.addView(space);
                        extraServiceTotal += services.getPrice();
                        idLists.add(services.getId());
                    }
                    sumTotal(extraServiceTotal);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    public void sumTotal(Integer total){
        totalServices.setText("$ "+ total);
    }


    public void listAllServices(){
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setCancelable(false);
        final View loading = getLayoutInflater().inflate(R.layout.loading_gif, null);
        dialogBuilder.setView(loading);
        dialog = dialogBuilder.create();
        dialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SplashScreen.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        com.example.turismoreal.Services.ExtraServices extraServices = retrofit.create(com.example.turismoreal.Services.ExtraServices.class);
        Call<List<ExtraService>> AllServices = extraServices.getExtraServices();
        AllServices.enqueue(new Callback<List<ExtraService>>() {
            @Override
            public void onResponse(Call<List<ExtraService>> call, Response<List<ExtraService>> response) {
                dialog.dismiss();
                List<com.example.turismoreal.models.ExtraService> allServices = response.body();
                ArrayList<String> options = new ArrayList<String>();
                options.add("Seccionar un servicio extra");
                for (ExtraService i : allServices) {
                    if (i.getId() == 0){
                        continue;
                    }
                    options.add(i.getId()+" "+i.getName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ReservationExtraServices.this, android.R.layout.simple_spinner_item, options);
                extraServiceSpinner.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<ExtraService>> call, Throwable t) {

            }
        });
    }

    public void goBack(View view){
        Intent i = new Intent(this, CheckIn.class);
        startActivity(i);
        finish();
    }
}