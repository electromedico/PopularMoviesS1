package com.example.alex.popularmoviess1.utils;


import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by alex on 17/02/18.
 */

class NetworkUtils {

    private final static String SIZE_TAG="w185";
    private final static String BASE_POSTER_URL ="http://image.tmdb.org/t/p/";

    public static URL buildGetPosterUrl(String parameter) {
        Uri builtUri = Uri.parse(BASE_POSTER_URL).buildUpon()
                .appendEncodedPath(SIZE_TAG)
                .appendEncodedPath(parameter)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
}
