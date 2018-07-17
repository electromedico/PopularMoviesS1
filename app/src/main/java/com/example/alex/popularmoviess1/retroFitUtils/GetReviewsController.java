package com.example.alex.popularmoviess1.retroFitUtils;

import com.example.alex.popularmoviess1.BuildConfig;
import com.example.alex.popularmoviess1.utilInterfaces.OnTaskCompleted;
import com.example.alex.popularmoviess1.model.Reviews;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by alex on 25/02/18.
 */

public class GetReviewsController implements Callback<Reviews> {

    private final static String API_KEY = BuildConfig.THE_MOVIE_DB_API_TOKEN;
    private final OnTaskCompleted listener;

    public GetReviewsController(OnTaskCompleted listener){
        this.listener = listener;
    }
    public void start(int id){
        Retrofit client = TmdbClient.getClient();
        TmdbApi tmdbApi = client.create(TmdbApi.class);
        Call<Reviews>  call = tmdbApi.loadReviews(id,API_KEY);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<Reviews> call, Response<Reviews> response) {
        if (response.isSuccessful()){
            Reviews reviews = response.body();
            listener.onTaskCompletedReviews(reviews,this.getClass());
        }
    }

    @Override
    public void onFailure(Call<Reviews> call, Throwable t) {
        t.printStackTrace();
    }
}
