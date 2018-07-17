package com.example.alex.popularmoviess1.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by alex on 20/02/18.
 */

public class MoviesContract {
    public static final String CONTENT_AUTHORITY = "com.example.alex.popularmoviess1";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    public static final class favoritesEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES)
                .build();

        public static final String TABLE_NAME ="favorites";
        public static final String COLUMN_ID_TMDB = "id_TMDB";
        public static final String COLUMN_TITLE="title";
        public static final String COLUMN_RUNTIME="runtime";
        public static final String COLUMN_VOTE_AVERAGE="vote_average";
        public static final String COLUMN_RELEASE_DATE="release";
        public static final String COLUMN_OVERVIEW="overview";
        public static final String COLUMN_POSTER="poster";
        public static final String COLUMN_POSTER_PATH="poster_path";
        public static final String COLUMN_TIMESTAMP = "timestamp";

        public static Uri buildMovieUriWithID(int id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(String.valueOf(id))
                    .build();
        }

    }
}
