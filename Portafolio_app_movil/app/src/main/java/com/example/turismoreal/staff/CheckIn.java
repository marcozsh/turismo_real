package com.example.turismoreal.staff;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.turismoreal.LandingPage;
import com.example.turismoreal.R;
import com.example.turismoreal.Services.ReservationService;
import com.example.turismoreal.SplashScreen;
import com.example.turismoreal.administrator.DepartmentPage;
import com.example.turismoreal.models.Department;
import com.example.turismoreal.models.ExtraService;
import com.example.turismoreal.models.OneResponse;
import com.example.turismoreal.models.Reservation;
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

public class CheckIn extends AppCompatActivity {

    private TextView reservationQtyRooms, qtyCustumers,
            reservationCheckIn, reservationCheckOut, reservationTotal,
            reservationTitle, reservationStatus, textPayment
            ,rDepartmentPrice, extraServicesTotal, reservationPaid,idReservation, btnReservation;
    private ImageView departmentImageReservation;
    private EditText reservationId;
    private Button btnRegisterCheckIn, btnAddExtraService;
    private LinearLayout principalLayout;
    private ScrollView containerView;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private Integer totalAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_TurismoReal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in2);

        //avoid keyboard open
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        reservationId = findViewById(R.id.reservationId);
        containerView = findViewById(R.id.containerView);
        reservationQtyRooms = findViewById(R.id.reservationQtyRooms);
        qtyCustumers = findViewById(R.id.qtyCustumers);
        reservationCheckIn = findViewById(R.id.reservationCheckIn);
        reservationCheckOut = findViewById(R.id.reservationCheckOut);
        reservationTotal = findViewById(R.id.reservationTotal);
        reservationTitle = findViewById(R.id.reservationTitle);
        reservationStatus = findViewById(R.id.reservationStatus);
        rDepartmentPrice = findViewById(R.id.rDepartmentPrice);
        reservationPaid = findViewById(R.id.reservationPaid);
        btnReservation = findViewById(R.id.btnReservation);
        idReservation = findViewById(R.id.idReservation);
        extraServicesTotal = findViewById(R.id.extraServicesTotal);
        textPayment = findViewById(R.id.textPayment);
        departmentImageReservation = findViewById(R.id.departmentImageReservation);
        principalLayout = findViewById(R.id.principalLayout);
        btnRegisterCheckIn = findViewById(R.id.btnRegisterCheckIn);
        btnAddExtraService = findViewById(R.id.btnAddExtraService);
        containerView.setVisibility(View.GONE);

        SharedPreferences preferences = getSharedPreferences("reservation_details", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Integer id = preferences.getInt("reservationId", 0);
        if (id > 0 ){
            reservationId.setText(Integer.toString(id));
            btnReservation.performClick();
            preferences.edit().clear().apply();
        }

    }

    public void checkIn(View view){
        btnRegisterCheckIn.setVisibility(View.GONE);
        try{
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(SplashScreen.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ReservationService reservationService = retrofit.create(ReservationService.class);
            String jsonData = "{\"reservation_id\":"+Integer.parseInt(idReservation.getText().toString())+", \"action\": 1}";
            RequestBody requestBody = RequestBody.create(MediaType.parse("aplicaton/json"), jsonData);
            reservationService.markReservation(requestBody).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        OneResponse result = new Gson().fromJson(response.body().string(), OneResponse.class);
                        if (result.getResponse() > 0 ){
                            Toast.makeText(CheckIn.this, "Check In realizado correctamente", Toast.LENGTH_SHORT).show();
                            btnReservation.performClick();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {}
            });

        }catch (Exception e){
           e.printStackTrace();
        }
    }

    public void searchReservation(View view){
        if(!reservationId.getText().toString().isEmpty()){
            principalLayout.removeAllViews();
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
                ReservationService reservationService = retrofit.create(ReservationService.class);
                String jsonData = "{\"reservation_id\":" + reservationId.getText().toString() + "}";
                RequestBody requestBody = RequestBody.create(MediaType.parse("aplicaton/json"), jsonData);
                reservationService.getReservation(requestBody).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            dialog.dismiss();
                            Reservation[] reservation = new Gson().fromJson(response.body().string(), Reservation[].class);
                            if (reservation.length != 0){
                                for (Reservation r : reservation){
                                    idReservation.setText(Integer.toString(r.getId()));
                                    Department[] department = r.getDepartment().toArray(new Department[0]);
                                    for (Department d : department){
                                        byte [] bytes = Base64.decode(d.getDepartmentImage(), Base64.NO_WRAP);
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0,bytes.length);
                                        departmentImageReservation.setImageBitmap(bitmap);
                                        reservationQtyRooms.setText(Integer.toString(d.getQtyRoom()));
                                        rDepartmentPrice.setText("$ " + d.getPrice());
                                    }
                                    reservationTitle.setText("Reserva "+ DepartmentPage.capitalize(r.getFirstName()) + " " + DepartmentPage.capitalize(r.getLastName()));
                                    qtyCustumers.setText(Integer.toString(r.getQtyCustomers()));
                                    reservationCheckIn.setText(r.getCheckIn());
                                    reservationCheckOut.setText(r.getCheckOut());
                                    String status = "";
                                    String colorStatus = "#575757";
                                    if (r.getStatus() == 1){
                                        status = "Reservado";
                                        colorStatus = "#00ff44";
                                    }else if (r.getStatus() == 2){
                                        status = "Activa";
                                        colorStatus = "#0400ff";
                                    }else if (r.getStatus() == 3){
                                        status = "Terminada";
                                        btnAddExtraService.setVisibility(View.GONE);
                                    }else if (r.getStatus() == 4){
                                        status = "Cancelado";
                                        colorStatus = "#ff0000";
                                        btnAddExtraService.setVisibility(View.GONE);
                                    }
                                    reservationStatus.setTextColor(Color.parseColor(colorStatus));
                                    reservationStatus.setText(status);

                                    ExtraService[] extraServices = r.getServiceExtra().toArray(new ExtraService[0]);
                                    Integer extraServiceTotal = 0;
                                    for (ExtraService services: extraServices){
                                        //title
                                        TableLayout titleTableLauout = new TableLayout(CheckIn.this);
                                        TableRow titleBox = new TableRow(CheckIn.this);
                                        TextView nombre = new TextView(CheckIn.this);
                                        nombre.setText(services.getName());
                                        nombre.setTextColor(Color.parseColor("#0090FF"));
                                        principalLayout.addView(titleTableLauout);
                                        titleTableLauout.addView(titleBox);
                                        titleBox.addView(nombre);
                                        titleBox.setGravity(Gravity.CENTER);

                                        //atributes
                                        TableLayout principalTable = new TableLayout(CheckIn.this);
                                        TableRow containerRow = new TableRow(CheckIn.this);
                                        TableRow containerRow2 = new TableRow(CheckIn.this);
                                        containerRow.setGravity(Gravity.CENTER);
                                        containerRow2.setGravity(Gravity.CENTER);
                                        TableRow atributeRow = new TableRow(CheckIn.this);
                                        TableRow atributeRow2 = new TableRow(CheckIn.this);

                                        TableRow spaceRow = new TableRow(CheckIn.this);
                                        TableRow spaceRow2 = new TableRow(CheckIn.this);

                                        TableRow valueRow = new TableRow(CheckIn.this);
                                        TableRow valueRow2 = new TableRow(CheckIn.this);

                                        TextView atribute1 = new TextView(CheckIn.this);
                                        TextView atribute2 = new TextView(CheckIn.this);

                                        TextView space = new TextView(CheckIn.this);

                                        TextView value1 = new TextView(CheckIn.this);
                                        TextView value2 = new TextView(CheckIn.this);

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
                                    }
                                    if (r.getStatus() != 1){
                                        btnRegisterCheckIn.setVisibility(View.GONE);
                                        textPayment.setText("Total Pagado");
                                    }
                                    extraServicesTotal.setText("$ " + extraServiceTotal);
                                    reservationPaid.setText("$ " + r.getReservationAmount());
                                    reservationTotal.setText("$ " + (r.getTotalAmount() -r.getReservationAmount()));
                                }
                            }else{
                                containerView.setVisibility(View.GONE);
                                Toast.makeText(CheckIn.this, "No se encontraron reservas", Toast.LENGTH_SHORT).show();
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
                e.printStackTrace();
            }
            containerView.setVisibility(View.VISIBLE);
        }else{
            Toast.makeText(this, "Debe ingresar el folio de la reservación", Toast.LENGTH_SHORT).show();
        }

    }

    public void checkInMenu(View view){
        Intent i = new Intent(this, CheckInMenu.class);
        startActivity(i);
        finish();
    }

    public void goBack(View view){
        Intent i = new Intent(this, LandingPage.class);
        startActivity(i);
        finish();
    }
}