package com.example.turismoreal;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.turismoreal.Services.DepartmentService;
import com.example.turismoreal.models.Department;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class department_page extends AppCompatActivity {

    private EditText searchDepartment;
    private LinearLayout container;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_TurismoReal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_page);

        container = findViewById(R.id.contairner);
        searchDepartment = findViewById(R.id.searchDepartment);

        try {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(splashScreen.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            DepartmentService departmentService = retrofit.create(DepartmentService.class);
            Call<List<Department>> allDepartment = departmentService.getDepartment();
            allDepartment.enqueue(new Callback<List<Department>>() {
                @Override
                public void onResponse(Call<List<Department>> call, Response<List<Department>> response) {
                    if (!response.isSuccessful()){
                        Toast.makeText(department_page.this, response.code(), Toast.LENGTH_LONG);
                        return;
                    }
                    List<Department>departments = response.body();
                    for (Department department : departments){
                        LinearLayout departmentContainer = new LinearLayout(department_page.this);
                        TableLayout departmentTable = new TableLayout(department_page.this);

                        ImageView departmentPreview = new ImageView(department_page.this);

                        TableRow title = new TableRow(department_page.this);
                        TextView titleText = new TextView(department_page.this);

                        TableRow space = new TableRow(department_page.this);
                        TextView spaceRow = new TextView(department_page.this);

                        TableRow shortDescription = new TableRow(department_page.this);
                        TextView shortDescriptionText = new TextView(department_page.this);

                        TableRow price = new TableRow(department_page.this);
                        TextView priceText = new TextView(department_page.this);

                        TableRow status = new TableRow(department_page.this);
                        TextView statusText = new TextView(department_page.this);

                        if(department.getDepartmentImage() == null){
                            departmentPreview.setBackgroundResource(R.drawable.department_sample);
                        }else{
                            byte [] bytes = Base64.decode(department.getDepartmentImage(), Base64.NO_WRAP);
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
                        TextView spaceImg = new TextView(department_page.this);
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

                        statusText.setText(department.getStatus());
                        statusText.setTextColor(Color.parseColor("#858585"));
                        status.addView(statusText);
                        departmentTable.addView(status);

                        departmentContainer.addView(departmentTable);
                        TextView containerSpace = new TextView(department_page.this);
                        containerSpace.setText(" ");

                        container.addView(departmentContainer);
                        container.addView(containerSpace);
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


    public void goBack(View view){
        Intent i = new Intent(this, landing_page.class);
        startActivity(i);
        finish();
    }

    public void extraServicesMenu(View view){
        Intent i = new Intent(this, extra_services.class);
        startActivity(i);
        finish();
    }

    public void addDepartment(View view){
        Intent i = new Intent(this, add_department.class);
        startActivity(i);
        finish();
    }

    public void deleteSearch(View view){
        searchDepartment.setText("");
    }
}