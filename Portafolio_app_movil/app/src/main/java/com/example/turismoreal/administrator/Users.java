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
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.turismoreal.LandingPage;
import com.example.turismoreal.R;
import com.example.turismoreal.Services.UserServices;
import com.example.turismoreal.SplashScreen;
import com.example.turismoreal.models.User;
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

public class Users extends AppCompatActivity {


    private TextView userGoBack;

    private LinearLayout principalLayout;

    private int userType;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_TurismoReal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);
        principalLayout = findViewById(R.id.custumersContainer);
        userGoBack = findViewById(R.id.userGoBack);

        SharedPreferences preferences = getSharedPreferences("user_type", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        userType =preferences.getInt("user_type", 0) ;
        if (userType == 0){
            Toast.makeText(this, "Error al ingresar en el menú",Toast.LENGTH_SHORT).show();
            finish();
        }
        if (userType == 1){
            userGoBack.setText("Clientes");
        }else if(userType == 2){
            userGoBack.setText("Funcionarios");
        }
        all_customers(userType);


    }



    public void all_customers(int userType) {
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
            String jsonData = "{\"user_type\": "+userType+"}";
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
                                TableLayout titleTableLauout = new TableLayout(Users.this);
                                TableRow titleBox = new TableRow(Users.this);
                                TextView nombre = new TextView(Users.this);
                                nombre.setText(userType == 1 ? "Cliente" : "Funcionario");
                                nombre.setTextColor(Color.parseColor("#0090FF"));
                                principalLayout.addView(titleTableLauout);
                                titleTableLauout.addView(titleBox);
                                titleBox.addView(nombre);

                                //atributes
                                TableLayout principalTable = new TableLayout(Users.this);
                                TableRow containerRow = new TableRow(Users.this);
                                TableRow containerRow2 = new TableRow(Users.this);
                                TableRow containerRow3 = new TableRow(Users.this);

                                TableRow atributeRow = new TableRow(Users.this);
                                TableRow atributeRow2 = new TableRow(Users.this);
                                TableRow atributeRow3 = new TableRow(Users.this);

                                TableRow spaceRow = new TableRow(Users.this);
                                TableRow spaceRow2 = new TableRow(Users.this);
                                TableRow spaceRow3 = new TableRow(Users.this);

                                TableRow valueRow = new TableRow(Users.this);
                                TableRow valueRow2 = new TableRow(Users.this);
                                TableRow valueRow3 = new TableRow(Users.this);

                                TextView atribute1 = new TextView(Users.this);
                                TextView atribute2 = new TextView(Users.this);
                                TextView atribute3 = new TextView(Users.this);

                                TextView space = new TextView(Users.this);

                                TextView value1 = new TextView(Users.this);
                                TextView value2 = new TextView(Users.this);
                                TextView value3 = new TextView(Users.this);

                                atribute1.setText("Nombre");
                                atribute2.setText("Usuario");
                                atribute3.setText("Email");

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
                                        System.out.println(user.getUsername());
                                    }
                                });

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

    public void addEmployee(View view){
        Intent i = new Intent(this, AddEmployee.class);
        startActivity(i);
    }


    public void goBack(View view){
        Intent i = new Intent(this, LandingPage.class);
        startActivity(i);
        finish();
    }
}