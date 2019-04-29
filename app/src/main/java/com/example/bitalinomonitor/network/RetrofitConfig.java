package com.example.bitalinomonitor.network;

import com.example.bitalinomonitor.interfaces.IPatientService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConfig {
    private final Retrofit retrofit;
    private static final String URL = "http://192.168.0.104/BitalinoMonitor.Api/";
    private IPatientService patientService;

    public RetrofitConfig() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();

        this.retrofit = new Retrofit.Builder()
                .baseUrl(URL)
    			.addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public IPatientService getPatientService()
    {
        return this.retrofit.create(IPatientService.class);
    }
}
