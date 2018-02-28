package com.example.jgchan.datemypet.conexion;

import com.example.jgchan.datemypet.entidades.AccessToken;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by jgchan on 26/02/18.
 */

public interface apiService {


    @POST("registrar") //http://dominion.tk/api/
    @FormUrlEncoded
    /*'username',
      'nombre',
      'correo',
      "direccion",
       "telefono",
       "celular",
       'password'*/
    Call<AccessToken> registrar(@Field("username") String username ,
                               @Field("nombre") String nombre,
                               @Field("correo") String correo,
                               @Field("direccion") String direccion,
                               @Field("telefono") String telefono,
                               @Field("celular") String celular,
                               @Field("password") String email);


    @POST("ingresar")
    @FormUrlEncoded
    Call<AccessToken> ingresar(@Field("username") String username, @Field("password") String password);

    @POST("login")
    @FormUrlEncoded
    Call<AccessToken> login(@Field("username") String username, @Field("password") String password);

    @POST("logintemp")
    @FormUrlEncoded
    Call<AccessToken> ingresarfake(@Field("username") String username, @Field("password") String password);

    @POST("refresh")
    @FormUrlEncoded
    Call<AccessToken> refresh(@Field("refresh_token") String refreshToken);


}
