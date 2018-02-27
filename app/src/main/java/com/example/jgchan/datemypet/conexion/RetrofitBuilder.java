package com.example.jgchan.datemypet.conexion;

import com.example.jgchan.datemypet.BuildConfig;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * Created by jgchan on 26/02/18.
 */

public class RetrofitBuilder {

    private static final String BASE_URL = "http://date-my-pet-mx.tk/api/";


    private final static OkHttpClient cliente = buildClient();
    private  static Retrofit retrofit = buildRetrofit(cliente);


    private  static OkHttpClient buildClient(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {

                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();

                        Request.Builder builder = request.newBuilder()
                                .addHeader("Accept","aplication/json")
                                .addHeader("Connection","close");

                        return  chain.proceed(request);
                    }
                });


            if(BuildConfig.DEBUG){
                builder.addNetworkInterceptor(new StethoInterceptor());
            }

            return builder.build();


    }

    private static  Retrofit buildRetrofit(OkHttpClient cliente){
        return  new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(cliente)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

    }


    public static  <T> T createService(Class<T> service){
        return  retrofit.create(service);
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }
}
