package com.example.turismoreal.administrator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.turismoreal.R;
import com.example.turismoreal.Services.ProductService;
import com.example.turismoreal.models.OneResponse;
import com.example.turismoreal.models.ProductType;
import com.example.turismoreal.SplashScreen;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddProduct extends AppCompatActivity {

    Spinner productTypeSpinner;
    EditText productName, productBrand, productPrice;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_TurismoReal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        productTypeSpinner = findViewById(R.id.productTypeSpinner);
        productName = findViewById(R.id.productName);
        productBrand = findViewById(R.id.productBrand);
        productPrice = findViewById(R.id.productPrice);

        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(SplashScreen.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ProductService productService = retrofit.create(ProductService.class);
            Call<List<ProductType>> allCommunes = productService.getProductType();
            allCommunes.enqueue(new Callback<List<ProductType>>() {
                @Override
                public void onResponse(Call<List<ProductType>> call, Response<List<ProductType>> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(AddProduct.this, response.code(), Toast.LENGTH_LONG);
                        return;
                    }
                    List<ProductType> productTypes = response.body();
                    ArrayList<String> options = new ArrayList<String>();
                    options.add("Seccionar un tipo de producto");
                    for (ProductType i : productTypes) {
                        options.add(i.getDescription());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddProduct.this, android.R.layout.simple_spinner_item, options);
                    productTypeSpinner.setAdapter(adapter);
                }
                @Override
                public void onFailure(Call<List<ProductType>> call, Throwable t) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void addProduct(View view){
        dialogBuilder = new AlertDialog.Builder(AddProduct.this);
        dialogBuilder.setCancelable(false);
        final View loading = getLayoutInflater().inflate(R.layout.loading_gif, null);
        dialogBuilder.setView(loading);
        dialog = dialogBuilder.create();
        dialog.show();
        if (productName.getText().toString().isEmpty()){
            Toast.makeText(this, "Debe ingresar el nombre del producto",Toast.LENGTH_SHORT).show();
        }else if (productBrand.getText().toString().isEmpty()){
            Toast.makeText(this, "Debe ingresar la marca del producto",Toast.LENGTH_SHORT).show();
        }else if(productTypeSpinner.equals("Seccionar un tipo de producto")){
            Toast.makeText(this, "Debe seleccionar un tipo de producto",Toast.LENGTH_SHORT).show();
        }else if (productPrice.getText().toString().isEmpty()){
            Toast.makeText(this, "Debe ingresar el precio del producto",Toast.LENGTH_SHORT).show();
        } else{
            try {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(SplashScreen.URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                ProductService productService = retrofit.create(ProductService.class);
                String jsonData = "{\"name\":\""+productName.getText().toString()+"\", \"brand\":\""+productBrand.getText().toString()+"\",\"product_type\":\""+productTypeSpinner.getSelectedItem().toString()+"\", \"price\":"+Integer.parseInt(productPrice.getText().toString())+"}";
                RequestBody requestBody = RequestBody.create(MediaType.parse("aplicaton/json"), jsonData);
                productService.addProduct(requestBody).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            dialog.dismiss();
                            Gson g = new Gson();
                            OneResponse postResponse = g.fromJson(response.body().string(), OneResponse.class);
                            int productId = postResponse.getResponse();
                            if (productId != 0) {
                                Toast.makeText(AddProduct.this, "Producto Agregado correctamente", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(AddProduct.this, AddDepartmentInventory.class);
                                startActivity(i);
                                finish();
                            }else{
                                Toast.makeText(AddProduct.this, "Error al agregar el producto", Toast.LENGTH_SHORT).show();
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
    }
    public void goBack(View view){
        Intent i = new Intent(this, AddDepartmentInventory.class);
        startActivity(i);
        finish();
    }
}