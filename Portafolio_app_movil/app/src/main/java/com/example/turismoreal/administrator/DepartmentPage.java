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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.turismoreal.R;
import com.example.turismoreal.Services.DepartmentService;
import com.example.turismoreal.LandingPage;
import com.example.turismoreal.models.Department;
import com.example.turismoreal.models.DepartmentDisponibility;
import com.example.turismoreal.SplashScreen;
import com.google.gson.Gson;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DepartmentPage extends AppCompatActivity {
    private EditText searchDepartment;
    private LinearLayout container;
    private Integer flag = 1;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_TurismoReal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_page);

        container = findViewById(R.id.contairner);
        searchDepartment = findViewById(R.id.searchDepartment);
        searchDepartment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(searchDepartment.getText().toString().isEmpty()){
                    if(flag == 0){
                        departmentList();
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });


        departmentList();

    }

    public static String capitalize(String str) {
        if(str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public void searchDepartment(View view){
        if(!searchDepartment.getText().toString().isEmpty()){
            flag = 0;
            container.removeAllViews();
            getDepartmentByCommune(capitalize(searchDepartment.getText().toString()));
        }else{
            Toast.makeText(this, "Debe digitar la comuna a buscar", Toast.LENGTH_SHORT).show();
        }
    }

    public void getDepartmentByCommune(String commune){
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setCancelable(false);
        final View loading = getLayoutInflater().inflate(R.layout.loading_gif, null);
        dialogBuilder.setView(loading);
        dialog = dialogBuilder.create();
        dialog.show();
        try{
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(SplashScreen.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            DepartmentService departmentService = retrofit.create(DepartmentService.class);
            String jsonData = "{\"commune\":\""+commune+"\"}";
            RequestBody requestBody = RequestBody.create(MediaType.parse("aplicaton/json"), jsonData);
            departmentService.getDepartmentByCommune(requestBody).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (!response.isSuccessful()){
                        Toast.makeText(DepartmentPage.this, response.code(), Toast.LENGTH_LONG);
                        return;
                    }
                    try {
                        dialog.dismiss();
                        Department[] departments = new Gson().fromJson(response.body().string(), Department[].class);
                        for (Department department : departments){
                            LinearLayout departmentContainer = new LinearLayout(DepartmentPage.this);
                            TableLayout departmentTable = new TableLayout(DepartmentPage.this);

                            ImageView departmentPreview = new ImageView(DepartmentPage.this);

                            TableRow title = new TableRow(DepartmentPage.this);
                            TextView titleText = new TextView(DepartmentPage.this);

                            TableRow space = new TableRow(DepartmentPage.this);
                            TextView spaceRow = new TextView(DepartmentPage.this);

                            TableRow shortDescription = new TableRow(DepartmentPage.this);
                            TextView shortDescriptionText = new TextView(DepartmentPage.this);

                            TableRow price = new TableRow(DepartmentPage.this);
                            TextView priceText = new TextView(DepartmentPage.this);

                            if(department.getDepartmentImage() == null){
                                departmentPreview.setBackgroundResource(R.drawable.department_sample);
                            }else{
                                byte [] bytes = Base64.decode(department.getDepartmentImage(), Base64.NO_WRAP);
                                int x = 200;
                                int y = 200;

                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                departmentPreview.setImageBitmap(bitmap);
                            }

                            int width = 220;
                            int height = 200;
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
                            departmentPreview.setLayoutParams(params);

                            titleText.setText(department.getCommune());
                            titleText.setTextColor(Color.parseColor("#000000"));
                            title.addView(titleText);

                            departmentContainer.addView(departmentPreview);
                            TextView spaceImg = new TextView(DepartmentPage.this);
                            spaceImg.setText(" ");
                            departmentContainer.addView(spaceImg);
                            departmentTable.addView(title);

                            spaceRow.setText("               ");
                            space.addView(spaceRow);
                            departmentTable.addView(space);

                            shortDescriptionText.setText(department.getShortDescription());
                            shortDescriptionText.setTextColor(Color.parseColor("#000000"));
                            shortDescriptionText.setTextSize(10);
                            shortDescription.addView(shortDescriptionText);
                            departmentTable.addView(shortDescription);

                            priceText.setText("$ "+department.getPrice());
                            priceText.setTextColor(Color.parseColor("#000000"));
                            price.addView(priceText);
                            departmentTable.addView(price);


                            departmentContainer.addView(departmentTable);
                            TextView containerSpace = new TextView(DepartmentPage.this);
                            containerSpace.setText(" ");

                            container.addView(departmentContainer);
                            container.addView(containerSpace);
                            departmentContainer.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    goDepartmentDetail(department.getId());
                                }
                            });
                        }
                    } catch (Exception e) {
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

    }


    public void departmentList(){
        container.removeAllViews();
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setCancelable(false);
        final View loading = getLayoutInflater().inflate(R.layout.loading_gif, null);
        dialogBuilder.setView(loading);
        dialog = dialogBuilder.create();
        dialog.show();
        flag = 1;
        if (searchDepartment.getText().toString().isEmpty()){
            try {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(SplashScreen.URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                DepartmentService departmentService = retrofit.create(DepartmentService.class);
                Call<List<Department>> allDepartment = departmentService.getDepartment();
                allDepartment.enqueue(new Callback<List<Department>>() {
                    @Override
                    public void onResponse(Call<List<Department>> call, Response<List<Department>> response) {
                        if (!response.isSuccessful()){
                            Toast.makeText(DepartmentPage.this, response.code(), Toast.LENGTH_LONG);
                            return;
                        }
                        dialog.dismiss();
                        List<Department>departments = response.body();
                        for (Department department : departments){
                            LinearLayout departmentContainer = new LinearLayout(DepartmentPage.this);
                            TableLayout departmentTable = new TableLayout(DepartmentPage.this);

                            ImageView departmentPreview = new ImageView(DepartmentPage.this);

                            TableRow title = new TableRow(DepartmentPage.this);
                            TextView titleText = new TextView(DepartmentPage.this);

                            TableRow space = new TableRow(DepartmentPage.this);
                            TextView spaceRow = new TextView(DepartmentPage.this);

                            TableRow shortDescription = new TableRow(DepartmentPage.this);
                            TextView shortDescriptionText = new TextView(DepartmentPage.this);

                            TableRow price = new TableRow(DepartmentPage.this);
                            TextView priceText = new TextView(DepartmentPage.this);

                            if(department.getDepartmentImage() == null){
                                departmentPreview.setBackgroundResource(R.drawable.department_sample);
                            }else{
                                byte [] bytes = Base64.decode(department.getDepartmentImage(), Base64.NO_WRAP);
                                int x = 200;
                                int y = 200;

                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                departmentPreview.setImageBitmap(bitmap);
                            }

                            int width = 220;
                            int height = 200;
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
                            departmentPreview.setLayoutParams(params);

                            titleText.setText(department.getCommune());
                            titleText.setTextColor(Color.parseColor("#000000"));
                            title.addView(titleText);

                            departmentContainer.addView(departmentPreview);
                            TextView spaceImg = new TextView(DepartmentPage.this);
                            spaceImg.setText(" ");
                            departmentContainer.addView(spaceImg);
                            departmentTable.addView(title);

                            spaceRow.setText("               ");
                            space.addView(spaceRow);
                            departmentTable.addView(space);

                            shortDescriptionText.setText(department.getShortDescription());
                            shortDescriptionText.setTextColor(Color.parseColor("#000000"));
                            shortDescriptionText.setTextSize(10);
                            shortDescription.addView(shortDescriptionText);
                            departmentTable.addView(shortDescription);

                            priceText.setText("$ "+department.getPrice());
                            priceText.setTextColor(Color.parseColor("#000000"));
                            price.addView(priceText);
                            departmentTable.addView(price);


                            departmentContainer.addView(departmentTable);
                            TextView containerSpace = new TextView(DepartmentPage.this);
                            containerSpace.setText(" ");

                            container.addView(departmentContainer);
                            container.addView(containerSpace);
                            departmentContainer.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    goDepartmentDetail(department.getId());
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Department>> call, Throwable t) {

                    }
                });
            }catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(e.getMessage());
            }
        }

    }

    public void goDepartmentDetail(Integer id){
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
        DepartmentService departmentService = retrofit.create(DepartmentService.class);
        String jsonData = "{\"id\":\""+id+"\"}";
        RequestBody requestBody = RequestBody.create(MediaType.parse("aplicaton/json"), jsonData);
        departmentService.getDepartmentById(requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(DepartmentPage.this, response.code(), Toast.LENGTH_LONG);
                    return;
                }
                try {
                    Department[] departments = new Gson().fromJson(response.body().string(), Department[].class);
                    for (Department department : departments){
                        SharedPreferences preferences = getSharedPreferences("department", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("id",department.getId());
                        editor.putString("commune", department.getCommune());
                        editor.putInt("qty_rooms", department.getQtyRoom());
                        editor.putString("department_type", department.getDepartmentType());
                        editor.putString("department_image", department.getDepartmentImage());
                        editor.putString("address", department.getAddress());
                        editor.putInt("price", department.getPrice());
                        editor.putInt("is_new", department.getIsNew());

                        String json = "{\"department_id\":"+department.getId()+"}";
                        RequestBody requestBody2 = RequestBody.create(MediaType.parse("aplication/json"), json);
                        departmentService.getNotAvailableDates(requestBody2).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                               if (!response.isSuccessful()){
                                   Toast.makeText(DepartmentPage.this, response.code(), Toast.LENGTH_LONG);
                                   return;
                               }
                               try{
                                   DepartmentDisponibility[] disponibility = new Gson().fromJson(response.body().string(), DepartmentDisponibility[].class);
                                   for (DepartmentDisponibility dates : disponibility){
                                       editor.putString("check_in",dates.getCheckIn());
                                       editor.putString("check_out",dates.getCheckOut());
                                       editor.putString("maintenance_start",dates.getMaintenanceStart());
                                       editor.putString("maintenance_finish",dates.getMaintenanceFinish());
                                       editor.commit();
                                       Intent i = new Intent(DepartmentPage.this , DepartmentDetail.class);
                                       startActivity(i);
                                       dialog.dismiss();
                                   }
                               }catch (Exception e){
                                   e.printStackTrace();
                               }
                            }
                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {}
                        });

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });





    }

    public void goBack(View view){
        Intent i = new Intent(this, LandingPage.class);
        startActivity(i);
        finish();
    }

    public void extraServicesMenu(View view){
        Intent i = new Intent(this, ExtraServicePage.class);
        startActivity(i);
        finish();
    }

    public void addDepartment(View view){
        Intent i = new Intent(this, AddDepartment.class);
        startActivity(i);
    }

    public void deleteSearch(View view){
        searchDepartment.setText("");
    }
}