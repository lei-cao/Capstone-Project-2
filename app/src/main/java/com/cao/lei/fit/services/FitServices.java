package com.cao.lei.fit.services;


import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;

public class FitServices {
    final private String baseUrl = "http://106.187.49.48/";

    public interface Interface {
        @GET("fit/trainingsets.json")
        Call<FitResponses.TrainingSetsResponse> trainingSets();
    }

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public Interface service = retrofit.create(Interface.class);
}
