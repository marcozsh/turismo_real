package com.example.turismoreal.Services;

import com.example.turismoreal.models.ExtraService;
import com.example.turismoreal.models.Product;
import com.example.turismoreal.models.ProductType;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ProductService {
    @GET("product_type/")
    Call<List<ProductType>> getProductType();

    @GET("product")
    Call<List<Product>> getProducts();

    @POST("product/")
    Call<ResponseBody> addProduct(@Body RequestBody requestBody);

    @POST("product_by_category/")
    Call<ResponseBody> getProductsByCategories(@Body RequestBody requestBody);
}
