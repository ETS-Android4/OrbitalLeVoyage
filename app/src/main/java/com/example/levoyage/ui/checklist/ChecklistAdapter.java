package com.example.levoyage.ui.checklist;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.levoyage.R;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class ChecklistAdapter extends RecyclerView.Adapter<ChecklistAdapter.ChecklistViewHolder> {

    private List<ChecklistItem> list;
    private Context context;
    private DatabaseReference database;

    public ChecklistAdapter(Context context, List<ChecklistItem> list, DatabaseReference database) {
        this.context = context;
        this.list = list;
        this.database = database;
    }

    public static class ChecklistViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkBox;

        public ChecklistViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }

    @NonNull
    @Override
    public ChecklistAdapter.ChecklistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.checklist_item, parent,false);
        return new ChecklistAdapter.ChecklistViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChecklistAdapter.ChecklistViewHolder holder, int position) {
        ChecklistItem item = list.get(position);
        holder.checkBox.setText(item.getTask());
        holder.checkBox.setChecked(item.isChecked());

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            database.child(item.getId()).child("checked").setValue(isChecked);
            list.sort(ChecklistItem::compareTo);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
