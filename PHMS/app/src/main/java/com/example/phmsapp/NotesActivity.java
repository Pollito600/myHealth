package com.example.phmsapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.widget.ArrayAdapter;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

public class NotesActivity extends AppCompatActivity {
    private Button newNoteBtn;
    private ListView notesListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> notesList;
    private SharedPreferences sharedPreferences;
    private static final String NOTES_PREFS = "notes_prefs";
    private static final String NOTES_LIST_KEY = "notes_list_key";
    private void saveNotes(ArrayList<String> notesList) {
        SharedPreferences sharedPreferences = getSharedPreferences("notes", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // convert the ArrayList to a Set to save it in SharedPreferences
        Set<String> notesSet = new HashSet<String>(notesList);

        editor.putStringSet("notes", notesSet);
        editor.apply();
    }
    private void deleteNote(int position) {
        // remove the note from the notesList
        notesList.remove(position);
        adapter.notifyDataSetChanged();

        // remove the note from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("notes_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NOTES_LIST_KEY, new Gson().toJson(notesList));
        editor.apply();
        Log.d("NotesActivity", "Deleted note at position " + position);
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        newNoteBtn = findViewById(R.id.new_note_button);
        notesListView = findViewById(R.id.notes_list_view);
        notesList = new ArrayList<String>();

        // Initialize shared preferences
        sharedPreferences = getSharedPreferences(NOTES_PREFS, MODE_PRIVATE);

        // Load notes from shared preferences
        String notesJson = sharedPreferences.getString(NOTES_LIST_KEY, "");
        if (!notesJson.isEmpty()) {
            notesList = new Gson().fromJson(notesJson, new TypeToken<ArrayList<String>>(){}.getType());
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, notesList);
        notesListView.setAdapter(adapter);

        newNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create a new note dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(NotesActivity.this);
                builder.setTitle("New Note");

                // Set up the input
                final EditText inputTitle = new EditText(NotesActivity.this);
                final EditText inputBody = new EditText(NotesActivity.this);

                // Set up the input fields
                inputTitle.setHint("Note title");
                inputBody.setHint("Note body");

                LinearLayout layout = new LinearLayout(NotesActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.addView(inputTitle);
                layout.addView(inputBody);
                builder.setView(layout);

                // Set up the buttons
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title = inputTitle.getText().toString();
                        String body = inputBody.getText().toString();
                        String note = title + "\n" + body;

                        notesList.add(note);
                        adapter.notifyDataSetChanged();

                        // Save notes to shared preferences
                        String notesJson = new Gson().toJson(notesList);
                        sharedPreferences.edit().putString(NOTES_LIST_KEY, notesJson).apply();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // Show the dialog
                builder.show();
            }
        });

        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedNote = notesList.get(position);
                String[] noteParts = selectedNote.split("\n");

                // create a new note dialog with the selected note details
                AlertDialog.Builder builder = new AlertDialog.Builder(NotesActivity.this);
                builder.setTitle("Edit Note");

                // Set up the input
                final EditText inputTitle = new EditText(NotesActivity.this);
                final EditText inputBody = new EditText(NotesActivity.this);

                // Set up the input fields
                inputTitle.setHint("Note title");
                inputTitle.setText(noteParts[0]);
                inputBody.setHint("Note body");
                inputBody.setText(noteParts[1]);

                LinearLayout layout = new LinearLayout(NotesActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.addView(inputTitle);
                layout.addView(inputBody);
                builder.setView(layout);

                // Set up the buttons
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title = inputTitle.getText().toString();
                        String body = inputBody.getText().toString();
                        String note = title + "\n" + body;

                        // update the selected note in the notesList
                        notesList.set(position, note);
                        adapter.notifyDataSetChanged();

                        // save the updated notes list in SharedPreferences
                        saveNotes(notesList);
                    }
                });
                builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*notesList.remove(position);
                        adapter.notifyDataSetChanged();
                        saveNotes(notesList);*/
                        deleteNote(position);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // Show the dialog
                builder.show();
            }
        });
    }
}
