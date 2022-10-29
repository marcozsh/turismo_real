package com.example.turismoreal.staff;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.turismoreal.R;
import com.example.turismoreal.Services.ProductService;
import com.example.turismoreal.SplashScreen;
import com.example.turismoreal.models.OneResponse;
import com.example.turismoreal.models.Product;
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

public class InventoryList extends AppCompatActivity {

    private Button markStatus;
    private CheckBox ok, wrong;
    private EditText productId;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_TurismoReal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_list);

        markStatus = findViewById(R.id.markStatus);
        ok = findViewById(R.id.ok);
        wrong = findViewById(R.id.wrong);
        productId = findViewById(R.id.productId);


        SharedPreferences preferences = getSharedPreferences("product_status", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        int product_id = preferences.getInt("product_id",0);
        System.out.println(product_id);
        if(product_id>0){
            dialogBuilder = new AlertDialog.Builder(InventoryList.this);
            dialogBuilder.setCancelable(false);
            final View loading = getLayoutInflater().inflate(R.layout.loading_gif, null);
            dialogBuilder.setView(loading);
            dialog = dialogBuilder.create();
            dialog.show();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(SplashScreen.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ProductService productService = retrofit.create(ProductService.class);
            String jsonData = "{\"product_id\":"+product_id+"}";
            RequestBody requestBody = RequestBody.create(MediaType.parse("aplicaton/json"), jsonData);
            productService.getProductsById(requestBody).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        dialog.dismiss();
                        Product[] product = new Gson().fromJson(response.body().string(), Product[].class);
                        for(Product p : product){
                            productId.setText(Integer.toString(p.getId()));
                            if (p.getStatus().equals("ok")){
                                ok.setChecked(true);
                            }else if(p.getStatus().equals("wrong")){
                                wrong.setChecked(true);
                            }
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




    public void okStatus(View view){
        if (ok.isChecked()){
            wrong.setChecked(false);
        }
    }

    public void wrongStatus(View view){
        if (wrong.isChecked()){
            ok.setChecked(false);
        }
    }

    public void setProductStatus(View view){
        if (ok.isChecked() || wrong.isChecked()){
            if (!productId.getText().toString().equals("")){
                Integer product = Integer.parseInt(productId.getText().toString());
                String status = ok.isChecked() ? "ok" : "wrong";
                dialogBuilder = new AlertDialog.Builder(InventoryList.this);
                dialogBuilder.setCancelable(false);
                final View loading = getLayoutInflater().inflate(R.layout.loading_gif, null);
                dialogBuilder.setView(loading);
                dialog = dialogBuilder.create();
                dialog.show();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(SplashScreen.URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                ProductService productService = retrofit.create(ProductService.class);
                String jsonData = "{\"product_id\":"+product+", \"status\":\""+status+"\"}";
                RequestBody requestBody = RequestBody.create(MediaType.parse("aplicaton/json"), jsonData);
                productService.updateProductStatus(requestBody).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            OneResponse oneResponse = new Gson().fromJson(response.body().string(), OneResponse.class);
                            if (oneResponse.getResponse()>0){
                                Toast.makeText(InventoryList.this, "Estado Actualizado", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(InventoryList.this, "Error al actualizar el estado, intente nuevamente", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {}
                });
            }
        } else{
            Toast.makeText(this, "Debe seleccionar al menos un estado", Toast.LENGTH_SHORT).show();
        }


    }

}