package com.example.popmov.Networks;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit;
    public static Retrofit getRetrofit()
    {

        if(retrofit==null)
        {
            retrofit=new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org/3/movie/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        //https://api.themoviedb.org/3/movie/popular?api_key=4e44d9029b1270a757cddc766a1bcb63&language=en-US&page=2/
        //https://api.themoviedb.org/3/movie/343611?api_key=4e44d9029b1270a757cddc766a1bcb63
        //http://api.themoviedb.org/3/movie/{move key}/videos?api_key=###
        //https://www.youtube.com/watch?v=SUXWAEX2jlg
        //https://img.youtube.com/vi/2LqzF5WauAw/1.jpg
        //https://api.themoviedb.org/3/movie/343611/reviews?api_key=4e44d9029b1270a757cddc766a1bcb63&language=en-US&page=1
        return retrofit;
    }

}
