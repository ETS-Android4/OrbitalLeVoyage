package com.example.levoyage.ui.notes;

import android.app.AlertDialog;
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
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
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

import java.time.LocalDate;
import java.util.ArrayList;

public class NotesFragment extends Fragment {

    private ArrayList<NoteItem> list;
    private RecyclerView notesRecycler;
    private FloatingActionButton notesFab;
    private NotesAdapter adapter;
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
    public void onViewCreated(@NonNull @NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        notesRecycler = view.findViewById(R.id.recycler);
        notesFab = view.findViewById(R.id.fab);
        database = FirebaseDatabase
                .getInstance(getString(R.string.database_link))
                .getReference("Users").child(userID).child("Notes");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                notesRecycler.setHasFixedSize(true);
                notesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new NotesAdapter(getContext(), list);
                notesRecycler.setAdapter(adapter);
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    NoteItem note = dataSnapshot.getValue(NoteItem.class);
                    list.add(note);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(getContext(), "Error occurred", Toast.LENGTH_SHORT).show();
            }
        });

        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerItemTouchHelper(getContext()) {
            @Override
            public void deleteItem(int position) {
                dialogBuilder = new AlertDialog.Builder(getContext());
                dialogBuilder.setTitle("Delete Note");
                dialogBuilder.setMessage("Are you sure you want to delete this note?");
                dialogBuilder.setPositiveButton("Confirm", (dialog, which) -> {
                    NoteItem deleted = list.remove(position);
                    adapter.notifyItemRemoved(position);
                    database.child(deleted.getId()).removeValue();
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
                EditText titleText = popupView.findViewById(R.id.simpleText);
                TextView header = popupView.findViewById(R.id.simpleHeader);

                NoteItem item = list.get(position);
                editBtn.setText(R.string.edit);
                titleText.setText(item.getTitle());
                header.setText(R.string.edit_title);

                editBtn.setOnClickListener(v -> {
                    String title = titleText.getText().toString();
                    if (title.isEmpty()) {
                        titleText.setError(getString(R.string.empty_title));
                        titleText.requestFocus();
                    } else {
                        database.child(item.getId()).child("title").setValue(title);
                        adapter.notifyItemChanged(position);
                        dialog.dismiss();
                    }
                });

                closeBtn.setOnClickListener(t -> {
                    dialog.dismiss();
                    adapter.notifyItemChanged(position); });
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(notesRecycler);

        notesFab.setOnClickListener(v -> {
            dialogBuilder = new AlertDialog.Builder(getContext());
            View popupView = getLayoutInflater().inflate(R.layout.simple_popup, null);
            dialogBuilder.setView(popupView);
            dialog = dialogBuilder.create();
            dialog.show();

            ImageButton closeBtn = popupView.findViewById(R.id.simpleClose);
            Button createBtn = popupView.findViewById(R.id.simpleButton);
            EditText titleText = popupView.findViewById(R.id.simpleText);

            createBtn.setOnClickListener(v1 -> {
                String title = titleText.getText().toString();
                if (title.isEmpty()) {
                    titleText.setError(getString(R.string.empty_title));
                    titleText.requestFocus();
                } else {
                    Bundle bundle = new Bundle();
                    String id = database.push().getKey();
                    NoteItem newNote = new NoteItem("", id, title, LocalDate.now().toString());
                    database.child(id).setValue(newNote);
                    bundle.putParcelable("Note", newNote);
                    dialog.dismiss();
                    Navigation.findNavController(view).navigate(
                            R.id.action_nav_notes_to_notesViewFragment, bundle);
                }
            });

            closeBtn.setOnClickListener(t -> dialog.dismiss());
        });
    }

}
