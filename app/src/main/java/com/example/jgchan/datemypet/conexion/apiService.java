package com.example.jgchan.datemypet.conexion;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by jgchan on 26/02/18.
 */

public interface apiService {


    @POST("registrar") //http://dominion.tk/api/
    @FormUrlEncoded
    Call<AccessToken> register(@Field("name") String name , @Field("email") String email);



}
