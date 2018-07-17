package com.example.alex.popularmoviess1.retroFitUtils;

import com.example.alex.popularmoviess1.BuildConfig;
import com.example.alex.popularmoviess1.utilInterfaces.OnTaskCompleted;
import com.example.alex.popularmoviess1.model.MovieList;
import com.example.alex.popularmoviess1.model.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by alex on 25/02/18.
 */

public class GetMoviesController implements Callback<MovieList> {

    private final static String API_KEY = BuildConfig.THE_MOVIE_DB_API_TOKEN;
    private final OnTaskCompleted listener;

    public GetMoviesController (OnTaskCompleted listener) {
        this.listener = listener;
    }

    public void start(String tag){
        Retrofit client = TmdbClient.getClient();
        TmdbApi tmdbApi = client.create(TmdbApi.class);


        if (tag.equals(TmdbApi.POPULAR_TAG)){
            Call<MovieList>  call = tmdbApi.loadPopular(API_KEY);
            call.enqueue(this);

        }
        else if (tag.equals(TmdbApi.TOP_RATED_TAG)){
            Call<MovieList>  call = tmdbApi.loadTopRated(API_KEY);
            call.enqueue(this);
        }
    }

    @Override
    public void onResponse(Call<MovieList> call, Response<MovieList> response) {
        if (response.isSuccessful()) {
            List<Movie> movies = response.body().getMovies();
            listener.onTaskCompletedList(movies,this.getClass());
        }
    }

    @Override
    public void onFailure(Call<MovieList> call, Throwable t) {
        listener.onTaskFailed();
        t.printStackTrace();
    }
}
