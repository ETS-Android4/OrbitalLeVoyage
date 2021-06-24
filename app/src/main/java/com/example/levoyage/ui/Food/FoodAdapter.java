package com.example.levoyage.ui.Food;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.levoyage.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    Context context;

    ArrayList<FoodItineraryItem> list;

    public FoodAdapter(Context context, ArrayList<FoodItineraryItem> list) {
        this.context = context;
        this.list = list;
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {

        TextView name, category, rating, price;
        ImageView image;
        ConstraintLayout layout;

        public FoodViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.location);
            category = itemView.findViewById(R.id.itemAddress);
            rating = itemView.findViewById(R.id.itemRating);
            price = itemView.findViewById(R.id.itemPrice);
            image = itemView.findViewById(R.id.itemImage);
            layout = itemView.findViewById(R.id.itemLayout);
        }
    }

    @NotNull
    @Override
    public FoodAdapter.FoodViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.search_result, parent,false);
        return new FoodAdapter.FoodViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FoodAdapter.FoodViewHolder holder, int position) {
        FoodItineraryItem item = list.get(position);
        holder.name.setText(item.getLocation());
        holder.rating.setText(item.getRating());
        holder.category.setText(item.getCategory());
        holder.price.setText(item.getPrice());
        Picasso.get().load(item.getImageURL())
                .placeholder(R.drawable.error_placeholder_small).fit().into(holder.image);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("Food", item);
                bundle.putBoolean("Saved", false);
                Navigation.findNavController(v).navigate(
                        R.id.action_nav_food_to_foodDetailFragment, bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
