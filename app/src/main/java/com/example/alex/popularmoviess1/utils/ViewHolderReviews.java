package com.example.alex.popularmoviess1.utils;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.alex.popularmoviess1.R;

/**
 * Created by alex on 25/02/18.
 */

class ViewHolderReviews extends RecyclerView.ViewHolder {
    private ConstraintLayout constraintLayout;
    private TextView nametv;
    private TextView  reviewtv;

    public ViewHolderReviews(View itemView) {
        super(itemView);
        constraintLayout = itemView.findViewById(R.id.review_cl);
        nametv=itemView.findViewById(R.id.name_reviewer);
        reviewtv=itemView.findViewById(R.id.text_review);

    }

    public ConstraintLayout getConstraintLayout() {
        return constraintLayout;
    }

    public void setConstraintLayout(ConstraintLayout constraintLayout) {
        this.constraintLayout = constraintLayout;
    }

    public TextView getNametv() {
        return nametv;
    }

    public void setNametv(TextView nametv) {
        this.nametv = nametv;
    }

    public TextView getReviewtv() {
        return reviewtv;
    }

    public void setReviewtv(TextView reviewtv) {
        this.reviewtv = reviewtv;
    }
}
