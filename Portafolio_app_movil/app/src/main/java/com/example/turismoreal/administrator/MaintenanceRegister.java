package com.example.turismoreal.administrator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.turismoreal.R;
import com.example.turismoreal.Services.DepartmentService;
import com.example.turismoreal.models.OneResponse;
import com.example.turismoreal.SplashScreen;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MaintenanceRegister extends AppCompatActivity {

    TextView startDate, finishDate;
    DatePickerDialog.OnDateSetListener setListener, setListener2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_TurismoReal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_register);
        startDate = findViewById(R.id.startDate);
        finishDate = findViewById(R.id.finishDate);
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = year+"/"+month+"/"+dayOfMonth;
                startDate.setText(date);
            }
        };
        setListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = year+"/"+month+"/"+dayOfMonth;
                finishDate.setText(date);
            }
        };
    }
    public void startDate(View view){
        setDate(setListener);
    }
    public void finishDate(View view){
        setDate(setListener2);
    }
    public void setDate(DatePickerDialog.OnDateSetListener listener){
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(MaintenanceRegister.this, listener, year, month, day);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        datePickerDialog.setCancelable(false);
        datePickerDialog.show();
    }
    public void addMaintenance(View view) throws ParseException {

        SharedPreferences preferences = getSharedPreferences("department", Context.MODE_PRIVATE);
        Integer id = preferences.getInt("id", 0);

        String start =startDate.getText().toString();
        String finish =finishDate.getText().toString();

        Date dateStart = new SimpleDateFormat("dd/MM/yyyy").parse(start);
        Date dateFinish = new SimpleDateFormat("dd/MM/yyyy").parse(finish);

        if(dateStart.before(dateFinish)){
            Toast.makeText(this, "Fechas no validas", Toast.LENGTH_SHORT).show();
        }else{
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(SplashScreen.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            DepartmentService departmentService = retrofit.create(DepartmentService.class);
            String jsonData = "{\"start_date\":\""+start+"\",\"finish_date\":\""+finish+"\",\"department_id\":"+id+"}";
            RequestBody requestBody = RequestBody.create(MediaType.parse("aplicaton/json"), jsonData);
            departmentService.addMaintenance(requestBody).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (!response.isSuccessful()){
                        Toast.makeText(MaintenanceRegister.this, response.code(), Toast.LENGTH_SHORT).show();
                    }
                    try {
                        OneResponse post_response = new Gson().fromJson(response.body().string(), OneResponse.class);
                        Integer department_id = post_response.getResponse();
                        if (department_id == -1){
                            Toast.makeText(MaintenanceRegister.this, "Ya existe un mantenimiento registrado para esa fecha", Toast.LENGTH_SHORT).show();
                        }else if(department_id == -2){
                            Toast.makeText(MaintenanceRegister.this, "Existe una reserva para la fecha indicada", Toast.LENGTH_SHORT).show();
                        }else if(department_id > 0){
                            Toast.makeText(MaintenanceRegister.this, "Mantenimiento agregado correctamente", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(MaintenanceRegister.this, "Error al agregar el mantenimiento", Toast.LENGTH_SHORT).show();
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
}