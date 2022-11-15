package com.example.turismoreal.administrator;

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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.turismoreal.LandingPage;
import com.example.turismoreal.R;
import com.example.turismoreal.Services.ReservationService;
import com.example.turismoreal.Services.UserServices;
import com.example.turismoreal.SplashScreen;
import com.example.turismoreal.models.Department;
import com.example.turismoreal.models.ExtraService;
import com.example.turismoreal.models.Reservation;
import com.example.turismoreal.models.User;
import com.example.turismoreal.staff.CheckOut;
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

public class Customers extends AppCompatActivity {


    private LinearLayout principalLayout, custumersContainer;
    private ScrollView customersScrollView;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_TurismoReal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);
        principalLayout = findViewById(R.id.custumersContainer);
        all_customers();
    }


    public void all_customers() {
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
            UserServices userServices = retrofit.create(UserServices.class);
            String jsonData = "{\"user_type\": 1}";
            RequestBody requestBody = RequestBody.create(MediaType.parse("aplicaton/json"), jsonData);
            userServices.getUsers(requestBody).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        dialog.dismiss();
                        User[] users = new Gson().fromJson(response.body().string(), User[].class);
                        if (users.length != 0) {
                            for (User user : users) {
                                //title
                                TableLayout titleTableLauout = new TableLayout(Customers.this);
                                TableRow titleBox = new TableRow(Customers.this);
                                TextView nombre = new TextView(Customers.this);
                                nombre.setText("Usuario");
                                nombre.setTextColor(Color.parseColor("#0090FF"));
                                principalLayout.addView(titleTableLauout);
                                titleTableLauout.addView(titleBox);
                                titleBox.addView(nombre);
                                titleBox.setGravity(Gravity.CENTER);

                                //atributes
                                TableLayout principalTable = new TableLayout(Customers.this);
                                TableRow containerRow = new TableRow(Customers.this);
                                TableRow containerRow2 = new TableRow(Customers.this);
                                TableRow containerRow3 = new TableRow(Customers.this);
                                containerRow.setGravity(Gravity.LEFT);
                                containerRow2.setGravity(Gravity.LEFT);
                                containerRow3.setGravity(Gravity.LEFT);
                                TableRow atributeRow = new TableRow(Customers.this);
                                TableRow atributeRow2 = new TableRow(Customers.this);
                                TableRow atributeRow3 = new TableRow(Customers.this);

                                TableRow spaceRow = new TableRow(Customers.this);
                                TableRow spaceRow2 = new TableRow(Customers.this);
                                TableRow spaceRow3 = new TableRow(Customers.this);

                                TableRow valueRow = new TableRow(Customers.this);
                                TableRow valueRow2 = new TableRow(Customers.this);
                                TableRow valueRow3 = new TableRow(Customers.this);

                                TextView atribute1 = new TextView(Customers.this);
                                TextView atribute2 = new TextView(Customers.this);
                                TextView atribute3 = new TextView(Customers.this);

                                TextView space = new TextView(Customers.this);

                                TextView value1 = new TextView(Customers.this);
                                TextView value2 = new TextView(Customers.this);
                                TextView value3 = new TextView(Customers.this);

                                atribute1.setText("Nombre");
                                atribute2.setText("Usuario");
                                atribute2.setText("Email");

                                space.setText("                 ");

                                value1.setTextColor(Color.parseColor("#000000"));
                                value2.setTextColor(Color.parseColor("#000000"));
                                value3.setTextColor(Color.parseColor("#000000"));

                                String name = user.getFullName();
                                value1.setText(name);

                                String username = user.getUsername();
                                value2.setText(username);
                                String email = user.getEmail();
                                value3.setText(email);

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
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void goBack(View view){
        Intent i = new Intent(this, LandingPage.class);
        startActivity(i);
        finish();
    }
}