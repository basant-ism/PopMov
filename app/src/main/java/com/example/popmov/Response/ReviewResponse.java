package com.example.popmov.Response;

import com.example.popmov.Models.Review;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewResponse
{
    @SerializedName("results")
    private List<Review>reviews;

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
