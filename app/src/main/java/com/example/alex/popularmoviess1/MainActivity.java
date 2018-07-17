package com.example.alex.popularmoviess1;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;

import com.example.alex.popularmoviess1.data.MoviesContract;
import com.example.alex.popularmoviess1.utilInterfaces.OnTaskCompleted;
import com.example.alex.popularmoviess1.model.Movie;
import com.example.alex.popularmoviess1.model.MovieDetails;
import com.example.alex.popularmoviess1.model.Reviews;
import com.example.alex.popularmoviess1.model.Trailers;
import com.example.alex.popularmoviess1.retroFitUtils.GetDetailsController;
import com.example.alex.popularmoviess1.retroFitUtils.GetMoviesController;
import com.example.alex.popularmoviess1.retroFitUtils.TmdbApi;
import com.example.alex.popularmoviess1.utils.MovieGridAdapter;


import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements OnTaskCompleted,LoaderManager.LoaderCallbacks<Cursor> {

    private MovieGridAdapter movieGridAdapter;
    private GridView moviesGrid;
    private String lastSort;
    private List<Movie>movies;
    private int itemClicked;
    private int positionGv;

    // Constants for logging and referring to a unique loader
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MOVIES_LOADER = 0;
    private static final int MOVIE_BY_ID_LOADER=1;

    //LifeCycle
    private static boolean onCreateFlag = true;
    private static final String LAST_SORT_CALLBACKS_TEXT_KEY="last_sort";
    private static final String GRID_VIEW_STATE_KEY="gv_state";

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        moviesGrid = findViewById(R.id.gridview);
        movieGridAdapter =new MovieGridAdapter(this);
        moviesGrid.setAdapter(movieGridAdapter);


        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(LAST_SORT_CALLBACKS_TEXT_KEY)) {
                lastSort = savedInstanceState.getString(LAST_SORT_CALLBACKS_TEXT_KEY);
            }
            if (savedInstanceState.containsKey(GRID_VIEW_STATE_KEY)) {
                positionGv = savedInstanceState.getInt(GRID_VIEW_STATE_KEY);
            }
        }
        else {
            lastSort = getString(R.string.favorites);
            positionGv = 0;
        }

        moviesGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemClicked=i;
                // Check if we have internet access and flag favorites
                if(!isNetworkConnected()&&lastSort.equals(getString(R.string.favorites))){
                    getSupportLoaderManager().initLoader(MOVIE_BY_ID_LOADER,null,MainActivity.this);
                }
                else {
                    GetDetailsController getDetailsController = new GetDetailsController(MainActivity.this);
                    getDetailsController.start(movies.get(itemClicked).getId());
                }
            }
        });

    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(LAST_SORT_CALLBACKS_TEXT_KEY)) {
                lastSort = savedInstanceState.getString(LAST_SORT_CALLBACKS_TEXT_KEY);

            }
            if (savedInstanceState.containsKey(GRID_VIEW_STATE_KEY)) {
                positionGv = savedInstanceState.getInt(GRID_VIEW_STATE_KEY);


            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchPosters(lastSort);
    }

    /**
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int position = moviesGrid.getFirstVisiblePosition();
        outState.putInt(GRID_VIEW_STATE_KEY,position);
        outState.putString(LAST_SORT_CALLBACKS_TEXT_KEY, lastSort);

    }

    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.settings_menu,menu);

        MenuItem item = menu.findItem(R.id.sort);
        Spinner spinner = (Spinner) item.getActionView();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.spinner_list_item_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        if (lastSort != null) {
            int spinnerPosition = adapter.getPosition(lastSort);
            spinner.setSelection(spinnerPosition);
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (onCreateFlag){
                    onCreateFlag=false;
                    return;
                }
                String selectedItem = adapterView.getItemAtPosition(i).toString();
                fetchPosters(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    /**
     * Method to start the DetailsActivity and show the information from
     * the selected Movie
     * @param movie
     */
    private void gotoDetails(MovieDetails movie){
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(getString(R.string.movie), movie);
        startActivity(intent);

    }

    /**
     * Fetch of the movie poster from the API or the Favorites from the content provider
     * @param selectedItem
     */
    private void fetchPosters(String selectedItem){
        String tag;

        if (selectedItem.equals(getString(R.string.highest_rated))){

            tag = TmdbApi.TOP_RATED_TAG;
            lastSort =getString(R.string.highest_rated);
            GetMoviesController getMoviesController = new GetMoviesController(this);
            getMoviesController.start(tag);
        }
        else if (selectedItem.equals(getString(R.string.most_popular))){

            tag = TmdbApi.POPULAR_TAG;
            lastSort =getString(R.string.most_popular);
            GetMoviesController getMoviesController = new GetMoviesController(this);
            getMoviesController.start(tag);
        }
        else {
            tag = getString(R.string.favorites);
            lastSort = getString(R.string.favorites);
            queryMoviesDB();
        }

    }

    /**
     *
     */
    private void queryMoviesDB(){
        if ( getSupportLoaderManager().getLoader(MOVIES_LOADER)!=null){
            getSupportLoaderManager().restartLoader(MOVIES_LOADER,null,this);
        }
        else {
            getSupportLoaderManager().initLoader(MOVIES_LOADER,null,this);
        }

    }

    /**
     *
     * @param movieDetails
     * @param c
     */
    @Override
    public void onTaskCompletedDetails(MovieDetails movieDetails, Class c) {
        gotoDetails(movieDetails);
    }

    /**
     *
     * @param movieList
     * @param c
     */
    @Override
    public void onTaskCompletedList(List<Movie> movieList, Class c) {
        movies = movieList;
        movieGridAdapter.setMovies(movies);
        moviesGrid.smoothScrollToPosition(positionGv);
    }

    //Do nothing
    @Override
    public void onTaskCompletedTrailers(Trailers trailerList, Class c) {
    }

    //Do nothing
    @Override
    public void onTaskCompletedReviews(Reviews reviews, Class c) {
    }

    /**
     * Show message error
     */
    @Override
    public void onTaskFailed() {
        createSnackBar(getString(R.string.no_internet));
    }

    /**
     *check internet connection
     * @return boolean
     */
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    /**
     * Loader selection
     * @param id
     * @param args
     * @return
     */
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Loader<Cursor> loader =null;
        switch(id) {
            case MOVIE_BY_ID_LOADER:
                loader=  movieByIdLOADER(this);
                break;
            case MOVIES_LOADER:
                loader = favoritesLoader(this);
                break;
            default: new UnsupportedOperationException("Unknown Loader");

        }
    return loader;
    }

    /**
     *
     * @param loader
     * @param data
     */
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        switch(loader.getId()) {
            case MOVIE_BY_ID_LOADER:
                MovieDetails movie = fromCursorToMovieDetails(data);
                gotoDetails(movie);
                break;
            case MOVIES_LOADER:
                fromCursorToMovieList(data);
                if(movies.size()<1){
                    createSnackBar(getString(R.string.no_favorites));
                }
                movieGridAdapter.setMovies(movies);
                moviesGrid.smoothScrollToPosition(positionGv);

                break;
            default: new UnsupportedOperationException("Unknown Loader");


        }
        getSupportLoaderManager().destroyLoader(loader.getId());
    }
    //Do nothing
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    /**
     * Methode for creating a MovieList from a cursor
     * @param cursor
     */
    private void fromCursorToMovieList(Cursor cursor){
        List<Movie> movies= new ArrayList<>();

        try {
            while (cursor.moveToNext()) {
                Movie movie = new Movie();
                movie.setTitle(cursor.getString(
                        cursor.getColumnIndex(MoviesContract.favoritesEntry.COLUMN_TITLE)));
                movie.setOverview(cursor.getString(
                        cursor.getColumnIndex(MoviesContract.favoritesEntry.COLUMN_OVERVIEW)));
                movie.setReleaseDate(cursor.getString(
                        cursor.getColumnIndex(MoviesContract.favoritesEntry.COLUMN_RELEASE_DATE)));
                movie.setId(cursor.getInt(
                        cursor.getColumnIndex(MoviesContract.favoritesEntry.COLUMN_ID_TMDB)));
                movie.setVoteAverage(cursor.getFloat(
                        cursor.getColumnIndex(MoviesContract.favoritesEntry.COLUMN_VOTE_AVERAGE)));
                movie.setPosterPath(cursor.getString(
                        cursor.getColumnIndex(MoviesContract.favoritesEntry.COLUMN_POSTER_PATH)));
                movies.add(movie);
            }
        } finally {
            cursor.close();
        }
        this.movies= movies;

    }

    /**
     * Async Task for accessing the Favorites Database
     * @param c
     * @return
     */
    private AsyncTaskLoader<Cursor> favoritesLoader(Context c){
        return new AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor, this will hold all the task data
            Cursor favoritesCursor = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (favoritesCursor != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(favoritesCursor);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                 try {
                     return getContentResolver().query(
                             MoviesContract.favoritesEntry.CONTENT_URI,
                             null,
                             null,
                             null,
                             null,
                             null

                     );

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                favoritesCursor = data;
                super.deliverResult(data);
            }
        };
    }

    /**
     * Async Task for accessing the Favorites Database to get a movie by ID
     * @param c
     * @return
     */
    private AsyncTaskLoader<Cursor> movieByIdLOADER(Context c){
        final int movieid=movies.get(itemClicked).getId();
        return new AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor, this will hold all the data
            Cursor movieDetailsCursor = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (movieDetailsCursor != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(movieDetailsCursor);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                // Will implement to load data

                try {
                    return getContentResolver().query(
                            MoviesContract.favoritesEntry.buildMovieUriWithID(movieid),
                            null,
                            null,
                            null,
                            null,
                            null

                    );

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                movieDetailsCursor = data;
                super.deliverResult(data);
            }
        };
    }

    /**
     * Method for transform the cursor into a MovieDetails
     * @param cursor
     * @return
     */
    private MovieDetails fromCursorToMovieDetails(Cursor cursor){
        MovieDetails movie = new MovieDetails();
        if (cursor.getCount()<=0) return null ;

        try {
            while (cursor.moveToNext()) {

                movie.setTitle(cursor.getString(
                        cursor.getColumnIndex(MoviesContract.favoritesEntry.COLUMN_TITLE)));
                movie.setOverview(cursor.getString(
                        cursor.getColumnIndex(MoviesContract.favoritesEntry.COLUMN_OVERVIEW)));
                movie.setReleaseDate(cursor.getString(
                        cursor.getColumnIndex(MoviesContract.favoritesEntry.COLUMN_RELEASE_DATE)));
                movie.setId(cursor.getInt(
                        cursor.getColumnIndex(MoviesContract.favoritesEntry.COLUMN_ID_TMDB)));
                movie.setVoteAverage(cursor.getFloat(
                        cursor.getColumnIndex(MoviesContract.favoritesEntry.COLUMN_VOTE_AVERAGE)));
                movie.setRuntime(cursor.getInt(
                        cursor.getColumnIndex(MoviesContract.favoritesEntry.COLUMN_RUNTIME)));
                movie.setPosterPath(cursor.getString(
                        cursor.getColumnIndex(MoviesContract.favoritesEntry.COLUMN_POSTER_PATH)));
            }

        } finally {
            cursor.close();
        }
        return movie;
    }

    /**
     *
     * @param msg
     */
    private void createSnackBar(String msg){
            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.gridview),
                    msg, Snackbar.LENGTH_LONG);
            mySnackbar.show();

    }
}
