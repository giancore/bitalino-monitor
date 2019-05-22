package com.example.bitalinomonitor.network;

import com.example.bitalinomonitor.interfaces.IPatientService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConfig {
    private final Retrofit retrofit;
    private static final String URL = "http://192.168.0.104/BitalinoMonitor.Api/";
    private IPatientService patientService;

    public RetrofitConfig() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateDeserializer())
        .registerTypeAdapter(Double.class,  new JsonSerializer<Double>() {

            @Override
            public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                if(src == src.longValue())
                    return new JsonPrimitive(src.longValue());
                return new JsonPrimitive(src);
            }
        }
        ).create();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.MINUTES)
                .readTimeout(15, TimeUnit.MINUTES)
                .writeTimeout(15, TimeUnit.MINUTES)
                .build();

        this.retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public IPatientService getPatientService()
    {
        return this.retrofit.create(IPatientService.class);
    }
}
