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
import com.example.turismoreal.models.Reservation;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CheckOutMenu extends AppCompatActivity {


    private LinearLayout reservationContainer;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_TurismoReal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        reservationContainer = findViewById(R.id.custumersContainer);
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
                List<Reservation> allReservations = response.body();
                for (Reservation r : allReservations){
                    if (r.getStatus() == 2){
                        TableLayout titleTableLauout = new TableLayout(CheckOutMenu.this);
                        TableRow titleBox = new TableRow(CheckOutMenu.this);
                        TextView nombre = new TextView(CheckOutMenu.this);

                        nombre.setText("Reservación: " + r.getId());
                        nombre.setTextColor(Color.parseColor("#0090FF"));

                        reservationContainer.addView(titleTableLauout);
                        titleTableLauout.addView(titleBox);
                        titleBox.addView(nombre);
                        titleBox.setGravity(Gravity.CENTER);

                        //atributes
                        TableLayout principalTable = new TableLayout(CheckOutMenu.this);
                        TableRow containerRow = new TableRow(CheckOutMenu.this);
                        TableRow containerRow2 = new TableRow(CheckOutMenu.this);
                        TableRow containerRow3 = new TableRow(CheckOutMenu.this);
                        containerRow.setGravity(Gravity.CENTER);
                        containerRow2.setGravity(Gravity.CENTER);
                        containerRow3.setGravity(Gravity.CENTER);
                        TableRow atributeRow = new TableRow(CheckOutMenu.this);
                        TableRow atributeRow2 = new TableRow(CheckOutMenu.this);
                        TableRow atributeRow3 = new TableRow(CheckOutMenu.this);

                        TableRow spaceRow = new TableRow(CheckOutMenu.this);
                        TableRow spaceRow2 = new TableRow(CheckOutMenu.this);
                        TableRow spaceRow3 = new TableRow(CheckOutMenu.this);

                        TableRow valueRow = new TableRow(CheckOutMenu.this);
                        TableRow valueRow2 = new TableRow(CheckOutMenu.this);
                        TableRow valueRow3 = new TableRow(CheckOutMenu.this);

                        TextView atribute1 = new TextView(CheckOutMenu.this);
                        TextView atribute2 = new TextView(CheckOutMenu.this);
                        TextView atribute3 = new TextView(CheckOutMenu.this);

                        TextView space = new TextView(CheckOutMenu.this);

                        TextView value1 = new TextView(CheckOutMenu.this);
                        TextView value2 = new TextView(CheckOutMenu.this);
                        TextView value3 = new TextView(CheckOutMenu.this);

                        atribute1.setText("Nombre Cliente");
                        atribute2.setText("Email");
                        atribute3.setText("Estado");

                        space.setText("              ");

                        value1.setTextColor(Color.parseColor("#000000"));
                        value2.setTextColor(Color.parseColor("#000000"));

                        String status = "Activa";
                        String colorStatus = "#0400ff";
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
                        TextView containerSpace = new TextView(CheckOutMenu.this);
                        containerSpace.setText(" ");
                        reservationContainer.addView(containerSpace);
                        principalTable.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SharedPreferences preferences = getSharedPreferences("reservation_details", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putInt("reservationId", r.getId());
                                editor.putString("email", r.getEmail());
                                editor.commit();
                                Intent i = new Intent(CheckOutMenu.this, CheckOut.class);
                                startActivity(i);
                            }
                        });
                    }

                }
                dialog.dismiss();
            }
            @Override
            public void onFailure(Call<List<Reservation>> call, Throwable t) {
            }
        });
    }

    public void goBack(View view){
        Intent i = new Intent(this, LandingPage.class);
        startActivity(i);
        finish();
    }
}