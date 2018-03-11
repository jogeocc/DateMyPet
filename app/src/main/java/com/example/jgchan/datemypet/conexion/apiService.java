package com.example.jgchan.datemypet.conexion;

import com.example.jgchan.datemypet.entidades.AccessToken;
import com.example.jgchan.datemypet.entidades.Citas;
import com.example.jgchan.datemypet.entidades.Mascotas;
import com.example.jgchan.datemypet.entidades.ParseoToken;
import com.example.jgchan.datemypet.entidades.Success;
import com.example.jgchan.datemypet.entidades.Usuarios;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

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
    Call<ParseoToken> registrar(@Field("username") String username ,
                                @Field("nombre") String nombre,
                                @Field("correo") String correo,
                                @Field("direccion") String direccion,
                                @Field("telefono") String telefono,
                                @Field("celular") String celular,
                                @Field("password") String password);


    @POST("ingresar")
    @FormUrlEncoded
    Call<ParseoToken> ingresar(@Field("username") String username, @Field("password") String password);

    @POST("login")
    @FormUrlEncoded
    Call<ParseoToken> login(@Field("username") String username, @Field("password") String password);

    @POST("logintemp")
    @FormUrlEncoded
    Call<ParseoToken> ingresarfake(@Field("username") String username, @Field("password") String password);

    @POST("refresh")
    @FormUrlEncoded
    Call<AccessToken> refresh(@Field("refresh_token") String refreshToken);

    @GET("citas/{idUsuario}/listado")
    Call<Citas> miscitas(@Path("idUsuario") String idUsuario);

    @GET("usuario/{idUsuario}/ver")
    Call<Usuarios> usuario(@Path("idUsuario") String idUsuario);

    @GET("usuario/{idUsuario}/editar")
    Call<Usuarios> traerdatosUsuario(@Path("idUsuario") String idUsuario);

    @GET("usuario/{idUsuario}/eliminarcuenta")
    Call<Success> eliminarCuenta(@Path("idUsuario") String idUsuario);

    @PUT("usuario/{idUsuario}/actualizar") //http://dominion.tk/api/
    @FormUrlEncoded
    Call<Success> actualizar(   @Path("idUsuario") String idUsuario,
                                @Field("username") String username ,
                                @Field("nombre") String nombre,
                                @Field("correo") String correo,
                                @Field("direccion") String direccion,
                                @Field("telefono") String telefono,
                                @Field("celular") String celular);

    @GET("mismascotas/{idUsuario}/listado")
    Call<Mascotas> mismascotas(@Path("idUsuario") String idUsuario);
}
