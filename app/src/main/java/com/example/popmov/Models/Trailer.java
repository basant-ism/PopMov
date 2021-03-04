package com.example.popmov.Models;

import com.google.gson.annotations.SerializedName;

public class Trailer {
    @SerializedName("key")
    private String key;
    @SerializedName("type")
    private String type;
    @SerializedName("site")
    private String site;
    @SerializedName("name")
    private String name;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
