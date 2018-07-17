package com.example.alex.popularmoviess1.retroFitUtils;



import com.example.alex.popularmoviess1.BuildConfig;
import com.example.alex.popularmoviess1.utilInterfaces.OnTaskCompleted;
import com.example.alex.popularmoviess1.model.Trailers;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by alex on 25/02/18.
 */

public class GetTrailersController implements Callback <Trailers>{
    private final static String API_KEY = BuildConfig.THE_MOVIE_DB_API_TOKEN;
    private final OnTaskCompleted listener;

    public GetTrailersController(OnTaskCompleted listener){
        this.listener = listener;
    }

    public void start(int id){
        Retrofit client = TmdbClient.getClient();
        TmdbApi tmdbApi = client.create(TmdbApi.class);
        Call<Trailers>  call = tmdbApi.loadVideos(id,API_KEY);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<Trailers> call, Response<Trailers> response) {
        if (response.isSuccessful()){
            Trailers trailers = response.body();
            listener.onTaskCompletedTrailers(trailers,this.getClass());
        }

    }

    @Override
    public void onFailure(Call<Trailers> call, Throwable t) {
        t.printStackTrace();
    }
}
