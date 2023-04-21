package com.example.phmsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class NoteAdapter extends ArrayAdapter<Note> {
    private Context context;
    private int resource;
    private List<Note> notes;

    public NoteAdapter(Context context, int resource, List<Note> notes) {
        super(context, resource, notes);
        this.context = context;
        this.resource = resource;
        this.notes = notes;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        TextView titleTextView = convertView.findViewById(R.id.note_title);
        TextView contentTextView = convertView.findViewById(R.id.note_content);

        Note note = notes.get(position);

        titleTextView.setText(note.getTitle());
        contentTextView.setText(note.getContent());

        return convertView;
    }
    public void add(Note note) {
        notes.add(note);
        notifyDataSetChanged();
    }

    public void update(int position, Note note) {
        notes.set(position, note);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        notes.remove(position);
        notifyDataSetChanged();
    }
}
