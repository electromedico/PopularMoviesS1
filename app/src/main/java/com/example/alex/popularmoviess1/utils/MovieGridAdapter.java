package com.example.alex.popularmoviess1.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.alex.popularmoviess1.R;
import com.example.alex.popularmoviess1.model.Movie;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

/**
 * Created by alex on 17/02/18.
 * Custom Grid Adapter
 */

public class MovieGridAdapter extends BaseAdapter{

    private final Context mContext;
    private List<Movie> movies;

    public MovieGridAdapter(Context c) {
        mContext = c;
    }

    @Override
    public int getCount() {
        if (null == movies) return 0;
        return movies.size();
    }

    @Override
    public Movie getItem(int i) {
        return movies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView;
        if (view == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setAdjustViewBounds(Data.ADJUST_VIEW_BOUNDS);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(Data.PADING,Data.PADING,Data.PADING,Data.PADING);
        } else {
            imageView = (ImageView) view;
        }
        URL url = NetworkUtils.buildGetPosterUrl(movies.get(i).getPosterPath());
        Picasso.get().
                load(url.toString())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageView);
        return imageView;

    }

    public void setMovies(List<Movie> list){
        movies=list;
        notifyDataSetChanged();

    }


}
