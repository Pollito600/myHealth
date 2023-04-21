package com.example.phmsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
public class NotesActivity extends AppCompatActivity {

    private ListView mNotesListView;
    private Button mNewNoteButton;
    private Button mOpenNoteButton;
    private Button mModifyNoteButton;
    private ArrayList<Note> notes;
    private NoteAdapter noteAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        mNotesListView = findViewById(R.id.notes_listview);
        mNewNoteButton = findViewById(R.id.new_note_button);
        mOpenNoteButton = findViewById(R.id.open_note_button);
        mModifyNoteButton = findViewById(R.id.modify_note_button);
        mNewNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the "New Note" button click
                // You can launch a new activity to create a new note, for example
            }
        });

        mOpenNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the "Open Note" button click
                // You can launch a new activity to open an existing note, for example
            }
        });

        mModifyNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the "Modify Note" button click
                // You can launch a new activity to modify an existing note, for example
            }
        });*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        Log.d("NotesActivity", "onCreate() called");
        mNotesListView = findViewById(R.id.notes_listview);
        mNewNoteButton = findViewById(R.id.new_note_button);
        notes = new ArrayList<>();
        noteAdapter = new NoteAdapter(this, R.layout.activity_notes, notes);
        mNotesListView.setAdapter(noteAdapter);

        mNewNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the "New Note" button click
                notes.add(new Note("New Note", ""));
                noteAdapter.notifyDataSetChanged();
                Toast.makeText(NotesActivity.this, "New Note added", Toast.LENGTH_SHORT).show();
            }
        });
    }

}