package com.example.turismoreal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.turismoreal.Services.LoginService;
import com.example.turismoreal.administrator.Customers;
import com.example.turismoreal.administrator.DepartmentPage;
import com.example.turismoreal.administrator.ExtraServicePage;
import com.example.turismoreal.models.CustomError;
import com.example.turismoreal.staff.CheckInMenu;
import com.example.turismoreal.staff.CheckOutMenu;
import com.google.gson.Gson;

public class LandingPage extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextView logutMessage, userRol, fullName;
    private Button yes, no, btnReports, btnStaff, btnCustomers, btnCheckIn, btnCheckOut;
    private ImageView adminIcon, staffIcon;
    private LinearLayout administratorsMenus;
    private Integer user_id = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_TurismoReal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page);

        administratorsMenus = findViewById(R.id.administratorsMenus);
        adminIcon = findViewById(R.id.adminIcon);
        staffIcon = findViewById(R.id.staffIcon);
        btnReports = findViewById(R.id.btnReports);
        btnStaff = findViewById(R.id.btnStaff);
        btnCustomers = findViewById(R.id.btnCustomers);
        btnCheckIn = findViewById(R.id.btnCheckIn);
        btnCheckOut = findViewById(R.id.btnCheckOut);


        SharedPreferences preferences = getSharedPreferences("current_session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        String rol = preferences.getString("userRol", "NO SESSION");
        String userName = preferences.getString("fullName", "NO SESSION");
        Integer userId = preferences.getInt("userId", 0);

        userRol = findViewById(R.id.txtRol);
        fullName = findViewById(R.id.userName);
        userRol.setText(rol);
        fullName.setText(userName);
        user_id = userId;

        if (userRol.getText().toString().equals("Funcionario")){
            administratorsMenus.setVisibility(View.GONE);
            adminIcon.setVisibility(View.GONE);
            btnReports.setVisibility(View.GONE);
            btnStaff.setVisibility(View.GONE);
            btnCustomers.setVisibility(View.GONE);
            staffIcon.setVisibility(View.VISIBLE);
            btnCheckIn.setVisibility(View.VISIBLE);
            btnCheckOut.setVisibility(View.VISIBLE);
        }
    }

    public void popOutLogout(View view){
        dialogBuilder = new AlertDialog.Builder(this);
        final View logoutView = getLayoutInflater().inflate(R.layout.logout_popout, null);
        logutMessage = (TextView) logoutView.findViewById(R.id.logoutMessage);
        yes = (Button) logoutView.findViewById(R.id.btnYes);
        no = (Button) logoutView.findViewById(R.id.btnNo);

        dialogBuilder.setView(logoutView);
        dialog = dialogBuilder.create();
        dialog.show();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LandingPage.this, "Saliendo", Toast.LENGTH_SHORT).show();
                logout();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    public void logout(){
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(SplashScreen.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            String jsonData = "{\"user_id\":"+user_id+"}";
            RequestBody requestBody = RequestBody.create(MediaType.parse("aplication/json"), jsonData);
            LoginService loginService = retrofit.create(LoginService.class);

            loginService.logOutEmployee(requestBody).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Gson gson = new Gson();
                    try {
                        CustomError error = gson.fromJson(response.body().string(), CustomError.class);
                        String respuesta = error.getError();
                        if (respuesta.equals("NO ERROR")){
                            SharedPreferences preferences = getSharedPreferences("current_session", Context.MODE_PRIVATE);
                            preferences.edit().clear().apply();
                            Intent i = new Intent(LandingPage.this, Login.class);
                            startActivity(i);
                            finish();
                        }else{
                            Toast.makeText(LandingPage.this, "Error con el servidor", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }


    public void checkInMenu(View view){
        Intent i = new Intent(this, CheckInMenu.class);
        startActivity(i);
    }
    public void checkOutMenu(View view){
        Intent i = new Intent(this, CheckOutMenu.class);
        startActivity(i);
    }

    public void departmentMenu(View view){
        Intent i = new Intent(this, DepartmentPage.class);
        startActivity(i);
    }

    public void customersPage(View view){
        Intent i = new Intent(this, Customers.class);
        startActivity(i);
    }

    public void extraServicesMenu(View view){
        Intent i = new Intent(this, ExtraServicePage.class);
        startActivity(i);
    }
}