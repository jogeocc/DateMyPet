package com.example.jgchan.datemypet.entidades;


import com.squareup.moshi.Json;

public class Success {

    @Json(name = "success")
    private String success;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    @Json(name = "access_token")
    private String accessToken;
    @Json(name = "username")
    private String username;
    @Json(name = "id")
    private Integer id;

    @Json(name ="name")
    String name;

    @Json(name ="email")
    String email;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}