package com.example.popmov.Networks;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient2 {
    private static Retrofit retrofit;
    public static Retrofit getRetrofit()
    {

        if(retrofit==null)
        {
            retrofit=new Retrofit.Builder()
                    .baseUrl("https://www.themealdb.com/api/json/v1/1/lookup.php/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
