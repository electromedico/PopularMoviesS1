package com.example.alex.popularmoviess1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by alex on 20/02/18.
 */

class MoviesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 6;


    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIES =
                "CREATE TABLE " + MoviesContract.favoritesEntry.TABLE_NAME + "("+
                        MoviesContract.favoritesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MoviesContract.favoritesEntry.COLUMN_ID_TMDB+" INTEGER not null, " +
                        MoviesContract.favoritesEntry.COLUMN_TITLE + " TEXT not null," +
                        MoviesContract.favoritesEntry.COLUMN_OVERVIEW + " TEXT not null, " +
                        MoviesContract.favoritesEntry.COLUMN_VOTE_AVERAGE + " REAL not null, " +
                        MoviesContract.favoritesEntry.COLUMN_RELEASE_DATE + " TEXT not null, " +
                        MoviesContract.favoritesEntry.COLUMN_RUNTIME + " INTEGER not null, " +
                        MoviesContract.favoritesEntry.COLUMN_POSTER_PATH + " TEXT not null," +
                        MoviesContract.favoritesEntry.COLUMN_POSTER + " NONE," +
                        MoviesContract.favoritesEntry.COLUMN_TIMESTAMP+" TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                        "); ";

        db.execSQL(SQL_CREATE_MOVIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.favoritesEntry.TABLE_NAME);
        onCreate(db);
    }
}
