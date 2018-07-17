package com.example.alex.popularmoviess1.utilInterfaces;

import com.example.alex.popularmoviess1.model.Movie;
import com.example.alex.popularmoviess1.model.MovieDetails;
import com.example.alex.popularmoviess1.model.Reviews;

import com.example.alex.popularmoviess1.model.Trailers;

import java.util.List;

/**
 * Created by alex on 21/02/18.
 * Interface to make an activity a custom listener
 */

public interface OnTaskCompleted {

    void onTaskCompletedDetails(MovieDetails movieDetails, Class c);
    void onTaskCompletedList(List<Movie> movies, Class c);
    void onTaskCompletedTrailers(Trailers trailerList, Class c);
    void onTaskCompletedReviews(Reviews reviews, Class c);
    void onTaskFailed();

}
