package com.example.popmov.Models;

import java.util.List;

public class User {
    private String uname;
    private List<Integer>movies_id;

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public List<Integer> getMovies_id() {
        return movies_id;
    }

    public void setMovies_id(List<Integer> movies_id) {
        this.movies_id = movies_id;
    }
}
