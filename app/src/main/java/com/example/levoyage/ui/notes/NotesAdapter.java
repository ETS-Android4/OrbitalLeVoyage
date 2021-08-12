package com.example.levoyage.ui.notes;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.levoyage.R;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    Context context;
    ArrayList<NoteItem> list;

    public NotesAdapter(Context context, ArrayList<NoteItem> list) {
        this.context = context;
        this.list = list;
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView title, date;
        ConstraintLayout itemLayout;

        public NoteViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.itineraryLocation);
            date = itemView.findViewById(R.id.itineraryTime);
            itemLayout = itemView.findViewById(R.id.itineraryItemLayout);
        }
    }

    @NonNull
    @NotNull
    @Override
    public NotesAdapter.NoteViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.itinerary_item, parent,false);
        return new NotesAdapter.NoteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NotesAdapter.NoteViewHolder holder, int position) {
        NoteItem item = list.get(position);
        holder.title.setText(item.getTitle());
        holder.date.setText(item.getDate());

        holder.itemLayout.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("Note", item);
            Navigation.findNavController(v).navigate(
                    R.id.action_nav_notes_to_notesViewFragment, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
