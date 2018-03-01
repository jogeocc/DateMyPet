package com.example.jgchan.datemypet.entidades;


import com.squareup.moshi.Json;

public class Success {

    @Json(name = "access_token")
    private String accessToken;
    @Json(name = "username")
    private String username;
    @Json(name = "id")
    private Integer id;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



}