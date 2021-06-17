package com.example.levoyage.ui.Food;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.levoyage.DetailFragment;
import com.example.levoyage.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class FoodDetailFragment extends DetailFragment<FoodItineraryItem> {

    private FoodItineraryItem restaurant;
    private TextView nameView, descriptionView, linkView, addressView, categoryView, priceView;
    private ImageView image;
    private FloatingActionButton detailFab;
    private Button addReviewBtn;
    private RecyclerView reviewsRecycler;
    private boolean saved;

    public FoodDetailFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            saved = getArguments().getBoolean("Saved");
        }
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameView = view.findViewById(R.id.detailName);
        addressView = view.findViewById(R.id.detailAddress);
        descriptionView = view.findViewById(R.id.detailDescription);
        priceView = view.findViewById(R.id.detailPrice);
        categoryView = view.findViewById(R.id.detailBooking);
        linkView = view.findViewById(R.id.detailLink);
        image = view.findViewById(R.id.detailImage);
        detailFab = view.findViewById(R.id.detailfab);
        addReviewBtn = view.findViewById(R.id.addReviewOpenBtn);
        reviewsRecycler = view.findViewById(R.id.detailReviews);

        if (saved) {
            detailFab.hide();
            String dateString = getArguments().getString("Date");
            String location = getArguments().getString("Location");
            retrieveSavedInfo(dateString, location, FoodItineraryItem.class);
        } else {
            restaurant = getArguments().getParcelable("Food");
            setDetails(restaurant);
        }
    }

    public void setDetails(FoodItineraryItem item) {
        String link = item.getLink();
        nameView.setText(item.getLocation());
        addressView.setText(String.format("Address: %s", item.getAddress()));
        descriptionView.setText(item.getDescription());
        categoryView.setText(String.format("Cuisine: %s", item.getCategory()));
        priceView.setText(String.format("Price level: %s", item.getPrice()));
        Picasso.get().load(item.getImageURL()).into(image);
        retrieveReviews(item.getId(), reviewsRecycler);

        linkView.setOnClickListener(v -> goToLink(link));
        detailFab.setOnClickListener(v -> addToItinerary(item));
        addReviewBtn.setOnClickListener(v -> addReview(item.getLocation(), item.getId()));
    }
}