package com.example.alex.popularmoviess1.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alex.popularmoviess1.DetailsActivity;
import com.example.alex.popularmoviess1.R;

import com.example.alex.popularmoviess1.model.MovieDetails;
import com.example.alex.popularmoviess1.model.Review;
import com.example.alex.popularmoviess1.model.Reviews;
import com.example.alex.popularmoviess1.model.Trailer;
import com.example.alex.popularmoviess1.model.Trailers;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 25/02/18.
 * Custom RecyclerViewAdapter
 * It can handle several types of ViewHolders
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int DETAILS_CL = 0;
    private final List<Object> items= new ArrayList<>();
    private Trailers trailers;
    private Reviews reviews;
    private MovieDetails movieDetails;
    private  byte[] posterBytes;
    private Drawable posterDrawable;
    private final Context context;

    private final int REVIEWS_CL = 1;
    private final int TRAILERS_CL = 2;

    public RecyclerViewAdapter(Context c ) {
        context=c;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case REVIEWS_CL:
                View v1 = inflater.inflate(R.layout.reviews_list_layout, viewGroup, false);
                viewHolder = new ViewHolderReviews(v1);
                break;
            case TRAILERS_CL:
                View v2 = inflater.inflate(R.layout.video_list_layout, viewGroup, false);
                viewHolder = new ViewHolderTrailers(v2,(DetailsActivity) context);
                break;
            default:
                View v = inflater.inflate(R.layout.movie_details_layout, viewGroup, false);
                viewHolder = new ViewHolderDetails(v,(DetailsActivity) context);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        switch (viewHolder.getItemViewType()) {
            case REVIEWS_CL:
                ViewHolderReviews vhr = (ViewHolderReviews) viewHolder;
                configureViewHolderReviews(vhr, position);
                break;
            case TRAILERS_CL:
                ViewHolderTrailers vh2 = (ViewHolderTrailers) viewHolder;
                configureViewHolderTrailers(vh2, position);
                break;
            default:
                ViewHolderDetails vhd = (ViewHolderDetails) viewHolder;
                configureViewHolderDetails(vhd);
                break;
        }
    }

    private void configureViewHolderTrailers(ViewHolderTrailers vh2, int position) {
        //For compensate the espace taken by the details viewholder
        int element = position-1;
        vh2.getTrailerText().setText(trailers.getTrailers().get(element).getName());
        vh2.setVideoKey(trailers.getTrailers().get(element).getKey());
    }

    private void configureViewHolderReviews(ViewHolderReviews vhr, int position) {
        //For compensate the espace taken by the details viewholder
        int element = position-1;
        vhr.getReviewtv().setText(reviews.getReviews().get(element).getContent());
        vhr.getNametv().setText(reviews.getReviews().get(element).getAuthor());
    }
    private void configureViewHolderDetails(ViewHolderDetails vhd) {

        if (movieDetails != null) {

            vhd.getRuntimeTv().setText(String.valueOf(movieDetails.getRuntime()).concat(context.getString(R.string.min)));
            vhd.getRatingTv().setText(String.valueOf(movieDetails.getVoteAverage()).concat(context.getString(R.string.out10)));
            vhd.getYearTv().setText(movieDetails.getReleaseDate());
            vhd.getOverviewTv().setText(movieDetails.getOverview());

            URL url = NetworkUtils.buildGetPosterUrl(
                    movieDetails.getPosterPath());


            if (posterDrawable !=null){

                Picasso.get()
                        .load(url.toString())
                        .placeholder(R.mipmap.ic_launcher)
                        .error(posterDrawable)
                        .into(vhd.getPosterIv());
            }
            else {
                Picasso.get()
                        .load(url.toString())
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(vhd.getPosterIv());
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Bitmap image = ((BitmapDrawable) vhd.getPosterIv().getDrawable()).getBitmap();
            image.compress(Bitmap.CompressFormat.PNG, 100, bos);
            posterBytes = bos.toByteArray();
        }

    }



    @Override
    public int getItemCount() {
        if (items != null)return items.size();
        return 0;
    }

    public void setmDataset(Reviews mDataset) {
        reviews = mDataset;
        if (trailers!=null)items.removeAll(trailers.getTrailers());
        items.addAll(reviews.getReviews());
        notifyDataSetChanged();
    }

    public void setmDataset(Trailers mDataset) {
        trailers = mDataset;
        if (reviews!=null)items.removeAll(reviews.getReviews());
        items.addAll(trailers.getTrailers());
        notifyDataSetChanged();
    }

    public void setmDataset(MovieDetails mDataset, Drawable drawable) {
        movieDetails = mDataset;
        items.add(movieDetails);
        posterDrawable = drawable;
    }

    @Override
    public int getItemViewType(int position) {

        if (items.get(position) instanceof Review) {
            return REVIEWS_CL;
        } else if (items.get(position) instanceof Trailer){
            return TRAILERS_CL;
        }
        else {
            return DETAILS_CL;
        }
    }

    public byte[] getImageBytes() {
        return posterBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.posterBytes = imageBytes;
    }
}
