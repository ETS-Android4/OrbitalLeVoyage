package com.example.levoyage.ui.notes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.levoyage.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import java.time.LocalDate;
import java.util.HashMap;

public class NotesViewFragment extends Fragment {

    private NoteItem noteItem;
    private FloatingActionButton saveFab;
    private EditText text;
    private Toolbar toolbar;

    public NotesViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           noteItem = getArguments().getParcelable("Note");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        saveFab = view.findViewById(R.id.savefab);
        text = view.findViewById(R.id.notesViewText);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(noteItem.getTitle());

        text.setText(noteItem.getContent());

        saveFab.setOnClickListener(v -> {
            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference database = FirebaseDatabase
                    .getInstance(getString(R.string.database_link))
                    .getReference("Users").child(userID).child("Notes").child(noteItem.getId());
            HashMap<String, Object> updates = new HashMap<>();
            String content = text.getText().toString();
            updates.put("content", content);
            updates.put("date", LocalDate.now().toString());
            database.updateChildren(updates, (error, ref) ->
                    Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show());

        });
    }
}