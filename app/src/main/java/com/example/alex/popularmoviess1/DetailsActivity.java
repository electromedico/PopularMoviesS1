package com.example.alex.popularmoviess1;

import android.content.ActivityNotFoundException;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.content.AsyncTaskLoader;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.example.alex.popularmoviess1.data.MoviesContract;
import com.example.alex.popularmoviess1.utilInterfaces.OnTaskCompleted;
import com.example.alex.popularmoviess1.model.Movie;
import com.example.alex.popularmoviess1.model.MovieDetails;
import com.example.alex.popularmoviess1.model.Reviews;
import com.example.alex.popularmoviess1.model.Trailers;
import com.example.alex.popularmoviess1.retroFitUtils.GetTrailersController;
import com.example.alex.popularmoviess1.retroFitUtils.GetReviewsController;
import com.example.alex.popularmoviess1.utilInterfaces.RvBtListener;
import com.example.alex.popularmoviess1.utilInterfaces.RvTrailersClickListener;

import com.example.alex.popularmoviess1.utils.RecyclerViewAdapter;

import java.util.List;

import static com.example.alex.popularmoviess1.utils.ViewHolderDetails.REVIEWS_BT_TAG;
import static com.example.alex.popularmoviess1.utils.ViewHolderDetails.TRAILERS_BT_TAG;


public class DetailsActivity extends AppCompatActivity implements OnTaskCompleted, RvTrailersClickListener,LoaderManager.LoaderCallbacks<Cursor> ,RvBtListener{

    private RecyclerViewAdapter recyclerViewAdapter;
    private MovieDetails movieDetails;
    private Boolean isMovieFav= false;
    private byte[] imageBytes;
    private Drawable stockedPosterImage=null;
    private String btClicked;
    private LinearLayoutManager mLayoutManager;
    private int positionRv;

    private FloatingActionButton fab;
    Button reviewsBt;
    Button trailersBt;

    // Constants for logging and referring to a unique loader
    private static final String TAG = DetailsActivity.class.getSimpleName();
    private static final int MOVIE_LOADER_ID = 0;

    //LifeCycle
    private static final String LAST_CLICKED ="last_CLICKED";
    private static final String RECYCLER_VIEW_STATE_KEY="RV_state";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        fab= findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            manageFavorites();
            }
        });
        setFabColor(isMovieFav);

        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)

        recyclerViewAdapter = new RecyclerViewAdapter(this);
        mRecyclerView.setAdapter(recyclerViewAdapter);
        Intent intent = getIntent();
        movieDetails= intent.getParcelableExtra(getString(R.string.movie));

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(LAST_CLICKED)) {
                btClicked = savedInstanceState.getString(LAST_CLICKED);
            }
        }
        else {
            btClicked = TRAILERS_BT_TAG;
        }

        if (movieDetails != null){
            getSupportLoaderManager().initLoader(MOVIE_LOADER_ID,null,this);
            setTitle(movieDetails.getTitle());
        }
    }

    /**
     * Method to add or delete a favorite
     */
    private void manageFavorites() {

        String actiondone;
        if (!isMovieFav){
            imageBytes =recyclerViewAdapter.getImageBytes();
            ContentValues contentValues=createMovieDetailsContentValues();
            Uri uri = getContentResolver().insert(
                    MoviesContract.favoritesEntry.CONTENT_URI,
                    contentValues);
            assert uri != null;
            uri.getEncodedPath();
            actiondone = getString(R.string.film_added);
            if (uri!=null) isMovieFav=true;
            setFabColor(isMovieFav);

        }
        else {
         int sqlcode = getContentResolver().delete(
                    MoviesContract.favoritesEntry.buildMovieUriWithID(movieDetails.getId()),
                    null,
                    null);
            //sqlcode = SQLiteQuerys.removeMovie(moviesDatabase,movieDetails);
            actiondone = getString(R.string.film_removed);
            if (sqlcode>0) isMovieFav=false;
            setFabColor(isMovieFav);
        }
        createSnackBar(actiondone);
    }

    /**
     * Acces the API to get all the trailers
     * @param id
     */
    private void fetchTrailers(int id){
        GetTrailersController trailersController = new GetTrailersController(this);
        trailersController.start(id);
    }

    /**
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(LAST_CLICKED, btClicked);
        outState.putInt(
                RECYCLER_VIEW_STATE_KEY,
                mLayoutManager.findFirstVisibleItemPosition());

    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(LAST_CLICKED)) {
                btClicked = savedInstanceState.getString(LAST_CLICKED);
            }
            if (savedInstanceState.containsKey(RECYCLER_VIEW_STATE_KEY)) {
                positionRv =savedInstanceState.getInt(RECYCLER_VIEW_STATE_KEY);

            }
        }
    }

    //Not Implemented
    @Override
    public void onTaskCompletedDetails(MovieDetails movieDetails, Class c) {
    }

    //Not Implemented
    @Override
    public void onTaskCompletedList(List<Movie> movies, Class c) {
    }

    /**
     * Listener for API access
     * @param trailerList
     * @param c
     */
    @Override
    public void onTaskCompletedTrailers(Trailers trailerList, Class c) {

        recyclerViewAdapter.setmDataset(trailerList);
        mLayoutManager.scrollToPosition(positionRv);
    }

    /**
     * Listener for API access
     * @param reviews
     * @param c
     */
    @Override
    public void onTaskCompletedReviews(Reviews reviews, Class c) {

        recyclerViewAdapter.setmDataset(reviews);
        mLayoutManager.scrollToPosition(positionRv);

    }

    /**
     * No favorites
     */
    @Override
    public void onTaskFailed() {
        createSnackBar(getString(R.string.no_favorites));
    }

    /**
     * Listener for the trailer click
     * @param key
     */
    @Override
    public void onListItemClick(String key) {
        watchYoutubeVideo(key);
    }

    /**
     * Intent to Launch YouTube
     * @param id
     */
    private void watchYoutubeVideo(String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            this.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            this.startActivity(webIntent);
        }
    }

    /**
     * connectivity check
     * @return
     */
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    /**
     *
     * @return
     */
    private ContentValues createMovieDetailsContentValues(){
        ContentValues contentValues= new ContentValues();

        contentValues.put(MoviesContract.favoritesEntry.COLUMN_ID_TMDB,
                movieDetails.getId());
        contentValues.put(MoviesContract.favoritesEntry.COLUMN_OVERVIEW,
                movieDetails.getOverview());
        contentValues.put(MoviesContract.favoritesEntry.COLUMN_VOTE_AVERAGE,
                movieDetails.getPopularity());
        contentValues.put(MoviesContract.favoritesEntry.COLUMN_POSTER_PATH,
                movieDetails.getPosterPath());
        contentValues.put(MoviesContract.favoritesEntry.COLUMN_RELEASE_DATE,
                movieDetails.getReleaseDate());
        contentValues.put(MoviesContract.favoritesEntry.COLUMN_RUNTIME,
                movieDetails.getRuntime());
        contentValues.put(MoviesContract.favoritesEntry.COLUMN_TITLE,
                movieDetails.getTitle());
        contentValues.put(MoviesContract.favoritesEntry.COLUMN_POSTER,
                imageBytes);
        return contentValues;
    }

    /**
     * Get the poster from the database
     * @param cursor
     */
    private void getDrawableFromDB(Cursor cursor){

        byte[] outputStream=null;

        if (cursor.getCount()<0) return;

        try {
            while (cursor.moveToNext()) {
                outputStream =cursor.getBlob(
                        cursor.getColumnIndex(MoviesContract.favoritesEntry.COLUMN_POSTER));
            }

        } finally {
            cursor.close();
        }
        stockedPosterImage = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(outputStream, 0, outputStream.length));

    }

    /**
     *
     * @param id
     * @param args
     * @return
     */
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        return new AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor, this will hold all the task data
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

                // Query and load all task data in the background; sort by priority
                // [Hint] use a try/catch block to catch any errors in loading data

                try {
                    return getContentResolver().query(
                            MoviesContract.favoritesEntry.buildMovieUriWithID(movieDetails.getId()),
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
     * Listener
     * @param loader
     * @param data
     */
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if(data.getCount()>0) isMovieFav=true;
        if (isMovieFav)
        {
            getDrawableFromDB(data);
            setFabColor(isMovieFav);

        }
        recyclerViewAdapter.setmDataset(movieDetails,stockedPosterImage);
        if(!isNetworkConnected()&&isMovieFav){
            recyclerViewAdapter.notifyDataSetChanged();
        }
        else {
            fetchSelection(btClicked);
        }

        getSupportLoaderManager().destroyLoader(loader.getId());
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    private void createSnackBar(String msg){
        Snackbar mySnackbar = Snackbar.make(findViewById(R.id.recycler_view),
                msg, Snackbar.LENGTH_LONG);
        mySnackbar.show();

    }

    /**
     * changes the color of the FAB if the movies is favorite or not
     * @param b
     */
    private void setFabColor(Boolean b){
        if (b){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                fab.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorFabFav)));
            }
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                fab.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorFabNotFav)));
            }
        }
    }

    //Listener for the buttons in the Movie details view holder
    @Override
    public void onButtonClicked(String key) {
        fetchSelection(key);
    }

    /**
     * to switch between The trailers and the Reviews
     * @param key
     */
    private void fetchSelection(String key){
        int id= movieDetails.getId();
        if (key== REVIEWS_BT_TAG){
            fetchReviews(id);
            btClicked=REVIEWS_BT_TAG;
        }
        else {
            fetchTrailers(id);
            btClicked=TRAILERS_BT_TAG;
        }

    }

    private void fetchReviews(int id){
        GetReviewsController reviewsController = new GetReviewsController(this);
        reviewsController.start(id);
    }
}
