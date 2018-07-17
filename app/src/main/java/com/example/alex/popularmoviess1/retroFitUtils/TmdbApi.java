package com.example.alex.popularmoviess1.retroFitUtils;

import com.example.alex.popularmoviess1.model.MovieDetails;
import com.example.alex.popularmoviess1.model.MovieList;
import com.example.alex.popularmoviess1.model.Reviews;
import com.example.alex.popularmoviess1.model.Trailers;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by alex on 22/02/18.
 */

public interface TmdbApi {
    String POPULAR_TAG = "movie/popular";
    String TOP_RATED_TAG = "movie/top_rated";
    String ID_TAG = "movie/{id}";
    String VIDEOS_TAG = "movie/{id}/videos";
    String REVIEWS_TAG = "movie/{id}/reviews";


    @GET(POPULAR_TAG)
    Call<MovieList> loadPopular(@Query("api_key") String status);

    @GET(TOP_RATED_TAG)
    Call<MovieList> loadTopRated(@Query("api_key") String status);

    @GET(ID_TAG)
    Call<MovieDetails> loadMovie(@Path("id") int groupId, @Query("api_key") String status);

    @GET(VIDEOS_TAG)
    Call<Trailers> loadVideos(@Path("id") int groupId, @Query("api_key") String status);

    @GET(REVIEWS_TAG)
    Call<Reviews> loadReviews(@Path("id") int groupId, @Query("api_key") String status);

}
