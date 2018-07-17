package com.example.alex.popularmoviess1.retroFitUtils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alex on 25/02/18.
 */

class TmdbClient {

    private static final String BASE_URL = "http://api.themoviedb.org/3/";


    private final static String API_KEY_TAG ="api_key";

    private final static String SIZE_TAG="w185";
    private final static String BASE_POSTER_URL ="http://image.tmdb.org/t/p/";


    static Retrofit getClient() {

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


}
