package com.example.turismoreal.staff;

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

import com.example.turismoreal.LandingPage;
import com.example.turismoreal.R;
import com.example.turismoreal.Services.ReservationService;
import com.example.turismoreal.SplashScreen;
import com.example.turismoreal.administrator.DepartmentPage;
import com.example.turismoreal.administrator.ExtraServicePage;
import com.example.turismoreal.models.ExtraService;
import com.example.turismoreal.models.Reservation;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CheckInMenu extends AppCompatActivity {


    private LinearLayout reservationContainer;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_TurismoReal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        reservationContainer = findViewById(R.id.reservationContainer);
        reservationList();
    }



    public void reservationList(){
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setCancelable(false);
        final View loading = getLayoutInflater().inflate(R.layout.loading_gif, null);
        dialogBuilder.setView(loading);
        dialog = dialogBuilder.create();
        dialog.show();
        reservationContainer.removeAllViews();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SplashScreen.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ReservationService reservation = retrofit.create(ReservationService.class);
        Call<List<Reservation>> allReservations = reservation.getReservation();
        allReservations.enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                dialog.dismiss();
                List<Reservation> allReservations = response.body();
                for (Reservation r : allReservations){
                    TableLayout titleTableLauout = new TableLayout(CheckInMenu.this);
                    TableRow titleBox = new TableRow(CheckInMenu.this);
                    TextView nombre = new TextView(CheckInMenu.this);

                    nombre.setText("Reservación: " + r.getId());
                    nombre.setTextColor(Color.parseColor("#0090FF"));

                    reservationContainer.addView(titleTableLauout);
                    titleTableLauout.addView(titleBox);
                    titleBox.addView(nombre);
                    titleBox.setGravity(Gravity.CENTER);

                    //atributes
                    TableLayout principalTable = new TableLayout(CheckInMenu.this);
                    TableRow containerRow = new TableRow(CheckInMenu.this);
                    TableRow containerRow2 = new TableRow(CheckInMenu.this);
                    TableRow containerRow3 = new TableRow(CheckInMenu.this);
                    containerRow.setGravity(Gravity.CENTER);
                    containerRow2.setGravity(Gravity.CENTER);
                    containerRow3.setGravity(Gravity.CENTER);
                    TableRow atributeRow = new TableRow(CheckInMenu.this);
                    TableRow atributeRow2 = new TableRow(CheckInMenu.this);
                    TableRow atributeRow3 = new TableRow(CheckInMenu.this);

                    TableRow spaceRow = new TableRow(CheckInMenu.this);
                    TableRow spaceRow2 = new TableRow(CheckInMenu.this);
                    TableRow spaceRow3 = new TableRow(CheckInMenu.this);

                    TableRow valueRow = new TableRow(CheckInMenu.this);
                    TableRow valueRow2 = new TableRow(CheckInMenu.this);
                    TableRow valueRow3 = new TableRow(CheckInMenu.this);

                    TextView atribute1 = new TextView(CheckInMenu.this);
                    TextView atribute2 = new TextView(CheckInMenu.this);
                    TextView atribute3 = new TextView(CheckInMenu.this);

                    TextView space = new TextView(CheckInMenu.this);

                    TextView value1 = new TextView(CheckInMenu.this);
                    TextView value2 = new TextView(CheckInMenu.this);
                    TextView value3 = new TextView(CheckInMenu.this);

                    atribute1.setText("Nombre Cliente");
                    atribute2.setText("Email");
                    atribute3.setText("Estado");

                    space.setText("              ");

                    value1.setTextColor(Color.parseColor("#000000"));
                    value2.setTextColor(Color.parseColor("#000000"));

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
                    }else if (r.getStatus() == 4){
                        status = "Cancelado";
                        colorStatus = "#ff0000";
                    }
                    value3.setTextColor(Color.parseColor(colorStatus));

                    value1.setText(r.getFirstName() +" "+ r.getLastName());
                    value2.setText(r.getEmail());
                    value3.setText(status);

                    reservationContainer.addView(principalTable);

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
                    TextView containerSpace = new TextView(CheckInMenu.this);
                    containerSpace.setText(" ");
                    reservationContainer.addView(containerSpace);
                    principalTable.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SharedPreferences preferences = getSharedPreferences("reservation_details", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putInt("reservationId", r.getId());
                            editor.commit();
                            Intent i = new Intent(CheckInMenu.this, CheckIn.class);
                            startActivity(i);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Reservation>> call, Throwable t) {

            }
        });

    }

    public void goCheckInPage(View view){
        Intent i = new Intent(this, CheckIn.class);
        startActivity(i);
    }

    public void goBack(View view){
        Intent i = new Intent(this, LandingPage.class);
        startActivity(i);
        finish();
    }
}