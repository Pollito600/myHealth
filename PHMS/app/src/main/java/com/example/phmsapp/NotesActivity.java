package com.example.phmsapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import android.widget.ArrayAdapter;
import android.widget.SearchView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NotesActivity extends AppCompatActivity {
    private Button newNoteBtn;

    private Button showAllNotesBtn;
    private SearchView searchView;
    private Button searchButton;
    private ListView notesListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> notesList;

    private SharedPreferences sharedPreferences;
    private static final String NOTES_PREFS = "notes_prefs";
    private static final String NOTES_LIST_KEY = "notes_list_key";
    private void saveNotes(ArrayList<String> notesList) {
        SharedPreferences sharedPreferences = getSharedPreferences("notes_prefs", MODE_PRIVATE);
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
    private void performSearch(String query) {
        // Load notes from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(NOTES_PREFS, MODE_PRIVATE);
        String notesJson = sharedPreferences.getString(NOTES_LIST_KEY, "");
        ArrayList<String> filteredNotesList = new ArrayList<String>();
        if (!notesJson.isEmpty()) {
            notesList = new Gson().fromJson(notesJson, new TypeToken<ArrayList<String>>(){}.getType());
        }

        // Filter notes based on search query
        if (query.isEmpty()) {
            filteredNotesList = notesList;
        } else {
            for (int i = notesList.size() - 1; i >= 0; i--) {
                String note = notesList.get(i);
                if (!note.toLowerCase().contains(query)) {
                    notesList.remove(i);
                } else {
                    filteredNotesList.add(note);
                }
            }
        }

        // Update adapter with filtered notes
        adapter.clear();
        adapter.addAll(filteredNotesList);
    }

    private void showAllNotes() {
        // Load notes from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(NOTES_PREFS, MODE_PRIVATE);
        String notesJson = sharedPreferences.getString(NOTES_LIST_KEY, "");
        ArrayList<String> allNotesList = new ArrayList<String>();
        if (!notesJson.isEmpty()) {
            allNotesList = new Gson().fromJson(notesJson, new TypeToken<ArrayList<String>>(){}.getType());
        }

        // Update adapter with all notes
        adapter.clear();
        adapter.addAll(allNotesList);
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        setTitle("Notes");

        newNoteBtn = findViewById(R.id.new_note_button);
        notesListView = findViewById(R.id.notes_list_view);
        notesList = new ArrayList<String>();
        showAllNotesBtn = findViewById(R.id.show_all_notes_button);

        // Initialize shared preferences
        sharedPreferences = getSharedPreferences(NOTES_PREFS, MODE_PRIVATE);

        // Load notes from shared preferences
        String notesJson = sharedPreferences.getString(NOTES_LIST_KEY, "");
        if (!notesJson.isEmpty()) {
            notesList = new Gson().fromJson(notesJson, new TypeToken<ArrayList<String>>(){}.getType());
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, notesList);
        notesListView.setAdapter(adapter);

        searchView = findViewById(R.id.search_view);
        searchButton = findViewById(R.id.search_button);

        // Set query text listener for the search view
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform search
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        showAllNotesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllNotes();
            }
        });

        // Set click listener for the search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchView.getQuery().toString();
                performSearch(query);
            }
        });


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

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the search query from the search view
                String query = searchView.getQuery().toString().toLowerCase();

                // Perform search operation
                performSearch(query);
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