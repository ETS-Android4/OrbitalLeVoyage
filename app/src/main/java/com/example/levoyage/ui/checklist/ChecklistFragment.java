package com.example.levoyage.ui.checklist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.levoyage.R;
import com.example.levoyage.RecyclerItemTouchHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ChecklistFragment extends Fragment {

    private RecyclerView checklistRecycler;
    private FloatingActionButton fab;
    private ChecklistAdapter adapter;
    private List<ChecklistItem> list;
    private DatabaseReference database;
    private final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        checklistRecycler = view.findViewById(R.id.recycler);
        fab = view.findViewById(R.id.fab);

        database = FirebaseDatabase
                .getInstance(getString(R.string.database_link))
                .getReference(userID).child("Checklist");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                checklistRecycler.setHasFixedSize(true);
                checklistRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new ChecklistAdapter(getContext(), list, database);
                checklistRecycler.setAdapter(adapter);
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChecklistItem checklistItem = dataSnapshot.getValue(ChecklistItem.class);
                    list.add(checklistItem);
                }
                list.sort(ChecklistItem::compareTo);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(getContext(), "Error occurred", Toast.LENGTH_SHORT).show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder = new AlertDialog.Builder(getContext());
                View popupView = getLayoutInflater().inflate(R.layout.simple_popup, null);
                dialogBuilder.setView(popupView);
                dialog = dialogBuilder.create();
                dialog.show();

                ImageButton closeBtn = popupView.findViewById(R.id.simpleClose);
                Button createBtn = popupView.findViewById(R.id.simpleButton);
                EditText taskText = popupView.findViewById(R.id.simpleText);
                TextView header = popupView.findViewById(R.id.simpleHeader);

                taskText.setHint(R.string.task);
                header.setText(R.string.add_task);

                createBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String task = taskText.getText().toString();
                        if (task.isEmpty()) {
                            taskText.setError(getString(R.string.empty_task));
                            taskText.requestFocus();
                        } else {
                            String id = database.push().getKey();
                            ChecklistItem newTask = new ChecklistItem(false, task, id);
                            database.child(id).setValue(newTask);
                            dialog.dismiss();
                        }
                    }
                });

                closeBtn.setOnClickListener(t -> dialog.dismiss());
            }
        });

        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerItemTouchHelper(getContext()) {
            @Override
            public void deleteItem(int position) {
                dialogBuilder = new AlertDialog.Builder(getContext());
                dialogBuilder.setTitle("Delete Task");
                dialogBuilder.setMessage("Are you sure you want to delete this task from the checklist?");
                dialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ChecklistItem deleted = list.remove(position);
                        adapter.notifyItemRemoved(position);
                        database.child(deleted.getId()).removeValue();
                    }
                });
                dialogBuilder.setNegativeButton("Cancel",
                        (dialog, which) -> adapter.notifyItemChanged(position));
                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
            }

            @Override
            public void editItem(int position) {
                dialogBuilder = new AlertDialog.Builder(getContext());
                View popupView = getLayoutInflater().inflate(R.layout.simple_popup, null);
                dialogBuilder.setView(popupView);
                dialog = dialogBuilder.create();
                dialog.show();

                ImageButton closeBtn = popupView.findViewById(R.id.simpleClose);
                Button editBtn = popupView.findViewById(R.id.simpleButton);
                EditText taskText = popupView.findViewById(R.id.simpleText);
                TextView header = popupView.findViewById(R.id.simpleHeader);

                ChecklistItem item = list.get(position);
                editBtn.setText(R.string.edit);
                taskText.setText(item.getTask());
                header.setText(R.string.edit_task);

                editBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String task = taskText.getText().toString();
                        if (task.isEmpty()) {
                            taskText.setError(getString(R.string.empty_task));
                            taskText.requestFocus();
                        } else {
                            database.child(item.getId()).child("task").setValue(task);
                            dialog.dismiss();
                            adapter.notifyItemChanged(position);
                        }
                    }
                });

                closeBtn.setOnClickListener(t -> {
                    dialog.dismiss();
                    adapter.notifyItemChanged(position); });
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(checklistRecycler);
    }
}