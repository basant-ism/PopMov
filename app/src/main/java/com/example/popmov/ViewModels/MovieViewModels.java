package com.example.popmov.ViewModels;

import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.popmov.Models.Movie;
import com.example.popmov.Models.Trailer;
import com.example.popmov.Repositories.MovieRepository;
import com.example.popmov.Response.MovieResponse;
import com.example.popmov.Response.ReviewResponse;
import com.example.popmov.Response.TrailerResponse;

import java.util.List;


public class MovieViewModels extends ViewModel {
    private MovieRepository movieRepository;
    public MovieViewModels()
    {
        movieRepository=new MovieRepository();
    }
    public LiveData<MovieResponse> getMovies(int page)
    {
        return movieRepository.getMovies(page);
    }
    public LiveData<Movie>getFavoriteMovies(List<Integer>ids)
    {
        return movieRepository.getFavoriteMovies(ids);
    }
    public LiveData<TrailerResponse>getTrailers(Integer id)
    {
        return  movieRepository.getTrailers(id);
    }
    public LiveData<ReviewResponse>getReviews(Integer id,int page)
    {
        return  movieRepository.getReviews(id,page);
    }

}
