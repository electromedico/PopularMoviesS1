package com.example.alex.popularmoviess1.utils;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alex.popularmoviess1.R;
import com.example.alex.popularmoviess1.utilInterfaces.RvBtListener;

/**
 * Created by alex on 25/02/18.
 */

public class ViewHolderDetails extends RecyclerView.ViewHolder {
    private ConstraintLayout constraintLayout;
    private TextView overviewTv;
    private TextView yearTv;
    private ImageView posterIv;
    private TextView runtimeTv;
    private TextView ratingTv;
    private byte[] bytes;
    private final RvBtListener rvBtListener;
    public static final String TRAILERS_BT_TAG = "trailer_button_tag";
    public static final String REVIEWS_BT_TAG = "reviews_button_tag";



    public ViewHolderDetails(View itemView, RvBtListener listener) {
        super(itemView);
        constraintLayout = itemView.findViewById(R.id.review_cl);
        overviewTv =  itemView.findViewById(R.id.overview_textview);
        yearTv = itemView.findViewById(R.id.year_textview);
        posterIv = itemView.findViewById(R.id.poster);
        runtimeTv = itemView.findViewById(R.id.runtime_textview);
        ratingTv = itemView.findViewById(R.id.vote_average_text_view);

        Button trailersBt = itemView.findViewById(R.id.trailes_bt);
        trailersBt.setTag(TRAILERS_BT_TAG);
        trailersBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                rvBtListener.onButtonClicked(TRAILERS_BT_TAG);
            }
        });

        Button reviewsBt = itemView.findViewById(R.id.reviews_bt);
        reviewsBt.setTag(REVIEWS_BT_TAG);
        reviewsBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                rvBtListener.onButtonClicked(REVIEWS_BT_TAG);
            }
        });
        rvBtListener = listener;
    }

    public ConstraintLayout getConstraintLayout() {
        return constraintLayout;
    }

    public void setConstraintLayout(ConstraintLayout constraintLayout) {
        this.constraintLayout = constraintLayout;
    }

    public TextView getOverviewTv() {
        return overviewTv;
    }

    public void setOverviewTv(TextView overviewTv) {
        this.overviewTv = overviewTv;
    }

    public TextView getYearTv() {
        return yearTv;
    }

    public void setYearTv(TextView yearTv) {
        this.yearTv = yearTv;
    }

    public ImageView getPosterIv() {
        return posterIv;
    }

    public void setPosterIv(ImageView posterIv) {
        this.posterIv = posterIv;
    }

    public TextView getRuntimeTv() {
        return runtimeTv;
    }

    public void setRuntimeTv(TextView runtimeTv) {
        this.runtimeTv = runtimeTv;
    }

    public TextView getRatingTv() {
        return ratingTv;
    }

    public void setRatingTv(TextView ratingTv) {
        this.ratingTv = ratingTv;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

}
