package com.example.alex.popularmoviess1.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.alex.popularmoviess1.data.MoviesContract.favoritesEntry.buildMovieUriWithID;

/**
 * Created by alex on 20/02/18.
 */

public class MoviesContentProvider extends ContentProvider {

    private static final int CODE_MOVIES = 100;
    private static final int CODE_MOVIES_WITH_ID = 101;

    private static final UriMatcher URI_MATCHER = buildUriMatcher();

    private MoviesDbHelper moviesDbHelper;
    @Override
    public boolean onCreate() {

        moviesDbHelper = new MoviesDbHelper(getContext());
        return true;
    }

    private static UriMatcher buildUriMatcher(){
        final  UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;

        matcher.addURI(authority,MoviesContract.PATH_MOVIES,CODE_MOVIES);
        matcher.addURI(authority,MoviesContract.PATH_MOVIES+ "/#",CODE_MOVIES_WITH_ID);

        return matcher;

    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = null;
        int match = URI_MATCHER.match(uri);
        switch (match){
            case CODE_MOVIES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                cursor= moviesDbHelper.getReadableDatabase().query(MoviesContract.favoritesEntry.TABLE_NAME,
                        projection,
                        MoviesContract.favoritesEntry.COLUMN_ID_TMDB+"=?",
                        new String[]{id},
                        null,
                        null,
                        sortOrder
                );
                break;
            case CODE_MOVIES:
                cursor= moviesDbHelper.getReadableDatabase().query(MoviesContract.favoritesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;


            default: new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return cursor;
    }

    /*Not Implemented*/
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri returnedUri;
        int match = URI_MATCHER.match(uri);

        switch (match){
            case CODE_MOVIES:
                int id = (int) moviesDbHelper.getWritableDatabase().insert(
                        MoviesContract.favoritesEntry.TABLE_NAME,
                        null,
                        values);
                returnedUri=buildMovieUriWithID(id);
                getContext().getContentResolver().notifyChange(returnedUri,null);
                break;
            default:
                throw new UnsupportedOperationException("not yet implemented");
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnedUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numRowsDeleted;
        int match = URI_MATCHER.match(uri);
        String id = uri.getPathSegments().get(1);
        switch (match){
            case CODE_MOVIES_WITH_ID:
                numRowsDeleted= moviesDbHelper.getWritableDatabase().delete(
                        MoviesContract.favoritesEntry.TABLE_NAME,
                        MoviesContract.favoritesEntry.COLUMN_ID_TMDB+"=?",
                        new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numRowsDeleted != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;

    }

    /*Not Implemented*/
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
