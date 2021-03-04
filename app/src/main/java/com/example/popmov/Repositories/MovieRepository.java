package com.example.popmov.Repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.popmov.Models.Movie;
import com.example.popmov.Models.Review;
import com.example.popmov.Networks.ApiClient;
import com.example.popmov.Networks.ApiService;
import com.example.popmov.Response.MovieResponse;
import com.example.popmov.Response.ReviewResponse;
import com.example.popmov.Response.TrailerResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MovieRepository {
    private ApiService apiService;
    public MovieRepository()
    {
        apiService= ApiClient.getRetrofit().create(ApiService.class);
    }
    public LiveData<MovieResponse> getMovies(int page)
    {
        MutableLiveData<MovieResponse> data=new MutableLiveData<>();
        apiService.getAllMovies("4e44d9029b1270a757cddc766a1bcb63","en-US",page).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });
       return data;
    }
public LiveData<Movie> getFavoriteMovies(List<Integer>ids)
    {

        MutableLiveData<Movie> data=new MutableLiveData<>();
        for(Integer id:ids)
        {


            apiService.getFavoriteMovie(id+"","4e44d9029b1270a757cddc766a1bcb63").enqueue(new Callback<Movie>() {
                @Override
                public void onResponse(Call<Movie> call, Response<Movie> response) {
                    data.setValue(response.body());
                }

                @Override
                public void onFailure(Call<Movie> call, Throwable t) {

                }
            });
        }
        return data;
    }
    public LiveData<TrailerResponse>getTrailers(Integer id)
    {
        MutableLiveData<TrailerResponse>data=new MutableLiveData<>();
        apiService.getTrailers(id+"/videos","4e44d9029b1270a757cddc766a1bcb63").enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                data.setValue(response.body());

            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {


            }
        });
        return data;
    }

    public LiveData<ReviewResponse> getReviews(Integer id,int page) {
        MutableLiveData<ReviewResponse>data=new MutableLiveData<>();
        apiService.getReviews(id+"/reviews","4e44d9029b1270a757cddc766a1bcb63","en-US",page).enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                data.setValue(response.body());


            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {

            }
        });
        return  data;
    }
}
