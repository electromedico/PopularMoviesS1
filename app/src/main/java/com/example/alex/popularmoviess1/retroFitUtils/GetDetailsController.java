package com.example.alex.popularmoviess1.retroFitUtils;

import com.example.alex.popularmoviess1.BuildConfig;
import com.example.alex.popularmoviess1.utilInterfaces.OnTaskCompleted;
import com.example.alex.popularmoviess1.model.MovieDetails;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by alex on 25/02/18.
 */

public class GetDetailsController implements Callback<MovieDetails> {

    private final static String API_KEY = BuildConfig.THE_MOVIE_DB_API_TOKEN;
    private final OnTaskCompleted listener;
    public GetDetailsController(OnTaskCompleted listener) {
        this.listener = listener;
    }

    public void start(int id){
        Retrofit client = TmdbClient.getClient();
        TmdbApi tmdbApi = client.create(TmdbApi.class);
        Call<MovieDetails>  call = tmdbApi.loadMovie(id,API_KEY);
        call.enqueue(this);

    }

    @Override
    public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
        if (response.isSuccessful()){
            MovieDetails movieDetails = response.body();
            listener.onTaskCompletedDetails(movieDetails,this.getClass());
        }
    }

    @Override
    public void onFailure(Call<MovieDetails> call, Throwable t) {
        t.printStackTrace();
    }
}
