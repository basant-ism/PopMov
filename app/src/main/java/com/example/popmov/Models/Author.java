package com.example.popmov.Models;

import com.google.gson.annotations.SerializedName;

public class Author
{
  @SerializedName("name")
    private String name;
    @SerializedName("username")
    private String username;
    @SerializedName("avatar_path")
    private String avatar_path;
    @SerializedName("rating")
    private Integer rating;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar_path() {
        return avatar_path;
    }

    public void setAvatar_path(String avatar_path) {
        this.avatar_path = avatar_path;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
