package com.example.levoyage.ui.notes;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.levoyage.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;

public class NotesFragment extends Fragment {

    private ArrayList<NoteItem> list;
    private RecyclerView notesRecycler;
    private FloatingActionButton notesFab;
    private NotesAdapter adapter;
    private DatabaseReference database;
    private String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
                .getInstance("https://orbital-le-voyage-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference(userID).child("Notes");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {
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
            public void onCancelled(@NotNull DatabaseError error) {
                Toast.makeText(getContext(), "Error occurred", Toast.LENGTH_SHORT).show();
            }
        });

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                NoteItem deleted = list.remove(position);
                adapter.notifyItemRemoved(position);
                database.child(deleted.getId()).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError error, @NonNull @NotNull DatabaseReference ref) {
                        Toast.makeText(getContext(), "Note Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(notesRecycler);

        notesFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder = new AlertDialog.Builder(getContext());
                View popupView = getLayoutInflater().inflate(R.layout.notes_popup, null);
                dialogBuilder.setView(popupView);
                dialog = dialogBuilder.create();
                dialog.show();

                ImageButton closeBtn = popupView.findViewById(R.id.newNoteClose);
                Button createBtn = popupView.findViewById(R.id.newNoteCreate);
                EditText titleText = popupView.findViewById(R.id.newNoteTitle);

                createBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = titleText.getText().toString();
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
            }
        });
    }


}
