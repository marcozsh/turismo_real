package com.example.turismoreal.administrator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.turismoreal.R;
import com.example.turismoreal.Services.DepartmentService;
import com.example.turismoreal.Services.ProductService;
import com.example.turismoreal.models.OneResponse;
import com.example.turismoreal.models.Product;
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

public class AddDepartmentInventory extends AppCompatActivity {

    Spinner productCategorySpinner, productSpinner2;
    EditText qty;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_TurismoReal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_department_inventory);
        productCategorySpinner = findViewById(R.id.productCategorySpinner);
        productSpinner2 = findViewById(R.id.productSpinner2);
        qty = findViewById(R.id.qty);
        try {
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

            ProductService productService = retrofit.create(ProductService.class);
            Call<List<ProductType>> productsTypes = productService.getProductType();
            productsTypes.enqueue(new Callback<List<ProductType>>() {
                @Override
                public void onResponse(Call<List<ProductType>> call, Response<List<ProductType>> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(AddDepartmentInventory.this, response.code(), Toast.LENGTH_LONG);
                        return;
                    }
                    List<ProductType> productTypes = response.body();
                    ArrayList<String> options = new ArrayList<String>();
                    options.add("Seccionar una categoria");
                    for (ProductType i : productTypes) {
                        options.add(i.getDescription());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddDepartmentInventory.this, android.R.layout.simple_spinner_item, options);
                    productCategorySpinner.setAdapter(adapter);
                    dialog.dismiss();
                }
                @Override
                public void onFailure(Call<List<ProductType>> call, Throwable t) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        productCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!productCategorySpinner.getSelectedItem().toString().equals("Seccionar una categoria")){
                    dialogBuilder = new AlertDialog.Builder(AddDepartmentInventory.this);
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
                    String jsonData = "{\"department_type\":\""+productCategorySpinner.getSelectedItem().toString()+"\"}";
                    RequestBody requestBody = RequestBody.create(MediaType.parse("aplicaton/json"), jsonData);
                    productService.getProductsByCategories(requestBody).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            SharedPreferences preferences = getSharedPreferences("product", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            if (!response.isSuccessful()) {
                                Toast.makeText(AddDepartmentInventory.this, response.code(), Toast.LENGTH_LONG);
                                return;
                            }

                            try {
                                Product[] product = new Gson().fromJson(response.body().string(), Product[].class);
                                ArrayList<String> options = new ArrayList<String>();
                                for (Product p : product){
                                    options.add(p.getName());
                                    editor.putInt("product_id",p.getId());
                                }
                                editor.commit();
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddDepartmentInventory.this, android.R.layout.simple_spinner_item, options);
                                productSpinner2.setAdapter(adapter);
                                dialog.dismiss();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void addDepartmentInventory(View view){
        dialogBuilder = new AlertDialog.Builder(AddDepartmentInventory.this);
        dialogBuilder.setCancelable(false);
        final View loading = getLayoutInflater().inflate(R.layout.loading_gif, null);
        dialogBuilder.setView(loading);
        dialog = dialogBuilder.create();
        dialog.show();
        if (productCategorySpinner.getSelectedItem().toString().equals("Seccionar una categoria")){
            Toast.makeText(this, "Debe seleccionar una categoría", Toast.LENGTH_LONG).show();
        }else if (productSpinner2.getSelectedItem() == null){
            Toast.makeText(this, "Debe seleccionar un producto", Toast.LENGTH_LONG).show();
        }else if (qty.getText().toString().isEmpty() || Integer.parseInt(qty.getText().toString()) == 0){
            Toast.makeText(this, "Debe ingresar al menos un producto", Toast.LENGTH_LONG).show();
        }else{
            SharedPreferences preferencesDepartment = getSharedPreferences("department", Context.MODE_PRIVATE);
            SharedPreferences preferencesProduct = getSharedPreferences("product", Context.MODE_PRIVATE);
            int departmentId = preferencesDepartment.getInt("id", 0);
            int productId = preferencesProduct.getInt("product_id", 0);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(SplashScreen.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            DepartmentService departmentService = retrofit.create(DepartmentService.class);
            String jsonData = "{\"qty\":"+qty.getText().toString()+", \"department_id\": "+departmentId+", \"product_id\":"+productId+"}";
            RequestBody requestBody = RequestBody.create(MediaType.parse("aplicaton/json"), jsonData);
            departmentService.addDepartmentInventory(requestBody).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(AddDepartmentInventory.this, response.code(), Toast.LENGTH_LONG);
                        return;
                    }
                    try {
                        OneResponse inventoryResponse = new Gson().fromJson(response.body().string(), OneResponse.class);
                        int inventoryId = inventoryResponse.getResponse();
                        if(inventoryId != 0){
                            dialog.dismiss();
                            Toast.makeText(AddDepartmentInventory.this, "Inventario agreagdo correctamente", Toast.LENGTH_LONG);
                            preferencesProduct.edit().clear().apply();
                            Intent i = new Intent(AddDepartmentInventory.this, DepartmentDetail.class);
                            startActivity(i);
                            finish();
                        }else{
                            Toast.makeText(AddDepartmentInventory.this, "Error al agregar inventario", Toast.LENGTH_LONG);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }

    public void addProduct(View view){
        Intent i = new Intent(this, AddProduct.class);
        startActivity(i);
    }

    public void goBack(View view){
        Intent i = new Intent(this, DepartmentDetail.class);
        startActivity(i);
        finish();
    }
}