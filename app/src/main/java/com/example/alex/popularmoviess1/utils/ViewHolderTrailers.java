package com.example.alex.popularmoviess1.utils;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.alex.popularmoviess1.R;
import com.example.alex.popularmoviess1.utilInterfaces.RvTrailersClickListener;

/**
 * Created by alex on 25/02/18.
 */

class ViewHolderTrailers extends RecyclerView.ViewHolder implements View.OnClickListener {
    private ConstraintLayout constraintLayout;
    private TextView trailerText;
    private final RvTrailersClickListener listener;
    private String videoKey;



    public void setVideoKey(String videoKey) {
        this.videoKey = videoKey;
    }

    public ViewHolderTrailers(View itemView, RvTrailersClickListener l) {
        super(itemView);
        constraintLayout = itemView.findViewById(R.id.trailers_cl);
        trailerText = itemView.findViewById(R.id.trailer_text);
        ImageButton iconIb = itemView.findViewById(R.id.imageButton);
        listener =l;
        itemView.setOnClickListener(this);
        iconIb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listener.onListItemClick(getVideoKey());
            }
        });

    }

    public ConstraintLayout getConstraintLayout() {
        return constraintLayout;
    }

    public void setConstraintLayout(ConstraintLayout constraintLayout) {
        this.constraintLayout = constraintLayout;
    }

    public TextView getTrailerText() {
        return trailerText;
    }

    public void setTrailerText(TextView trailerText) {
        this.trailerText = trailerText;
    }

    @Override
    public void onClick(View v) {
        String key = getVideoKey();
        listener.onListItemClick(key);

    }

    private String getVideoKey() {
        return videoKey;
    }
}
