package com.example.levoyage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.levoyage.ui.home.ItineraryItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

        Context context;
        ArrayList<ReviewItem> list;

public ReviewAdapter(Context context, ArrayList<ReviewItem> list) {
        this.context = context;
        this.list = list;
        }

public static class ReviewViewHolder extends RecyclerView.ViewHolder {

    TextView username, review, date;
    RatingBar rating;
    ConstraintLayout itemLayout;

    public ReviewViewHolder(View itemView) {
        super(itemView);
        username = itemView.findViewById(R.id.reviewUsername);
        review = itemView.findViewById(R.id.reviewText);
        date = itemView.findViewById(R.id.reviewDate);
        rating = itemView.findViewById(R.id.reviewRating);
        itemLayout = itemView.findViewById(R.id.reviewLayout);
    }
}

    @NotNull
    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.review_item, parent,false);
        return new ReviewAdapter.ReviewViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ReviewViewHolder holder, int position) {
        ReviewItem item = list.get(position);
        holder.username.setText(item.getUserID());
        holder.date.setText(item.getDate());
        holder.review.setText(item.getReview());
        holder.rating.setRating(item.getRating());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}