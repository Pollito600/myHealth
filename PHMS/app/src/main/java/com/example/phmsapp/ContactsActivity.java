package com.example.phmsapp;
import com.example.phmsapp.ContactsAdapter;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {

    private ArrayList<Contact> contacts;
    private RecyclerView recyclerView;
    private ContactsAdapter adapter;
    private EditText nameEditText, phoneEditText, emailEditText, addressEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        setTitle("Contacts");

        SharedPreferences sharedPreferences = getSharedPreferences("contacts", Context.MODE_PRIVATE);
        Gson gson = new Gson();

        String contactsJson = sharedPreferences.getString("contactsList", "");
        Type type = new TypeToken<ArrayList<Contact>>() {}.getType();
        contacts = gson.fromJson(contactsJson, type);

        if (contacts == null) {
            contacts = new ArrayList<>();
        }

        adapter = new ContactsAdapter(contacts);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setContacts(); // Call setContacts() method to load and display the contacts

        nameEditText = findViewById(R.id.name_edit_text);
        phoneEditText = findViewById(R.id.phone_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        addressEditText = findViewById(R.id.address_edit_text);


        Button addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String address = addressEditText.getText().toString();

                boolean isInvalid = false;
                if (name.isEmpty()) {
                    nameEditText.setHintTextColor(Color.RED);
                    isInvalid = true;
                } else {
                    nameEditText.setHintTextColor(Color.TRANSPARENT);
                }
                if (phone.isEmpty()) {
                    phoneEditText.setHintTextColor(Color.RED);
                    isInvalid = true;
                } else {
                    phoneEditText.setHintTextColor(Color.TRANSPARENT);
                }
                if (email.isEmpty()) {
                    emailEditText.setHintTextColor(Color.RED);
                    isInvalid = true;
                } else {
                    emailEditText.setHintTextColor(Color.TRANSPARENT);
                }
                if (address.isEmpty()) {
                    addressEditText.setHintTextColor(Color.RED);
                    isInvalid = true;
                } else {
                    addressEditText.setHintTextColor(Color.TRANSPARENT);
                }

                if (isInvalid) {
                    Toast.makeText(ContactsActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                Contact contact = new Contact(name, phone, email, address);
                contacts.add(contact);
                adapter.notifyItemInserted(contacts.size() - 1);
                clearFields();
                saveContacts();
            }
        });

        Button deleteButton = findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (contacts.isEmpty()) {
                    Toast.makeText(ContactsActivity.this, "No contacts to delete", Toast.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(ContactsActivity.this);
                builder.setTitle("Select Contact to Delete");
                String[] contactNames = new String[contacts.size()];
                for (int i = 0; i < contacts.size(); i++) {
                    contactNames[i] = contacts.get(i).getName();
                }
                builder.setItems(contactNames, (dialog, which) -> {
                    contacts.remove(which);
                    adapter.notifyItemRemoved(which);
                    saveContacts();
                });
                builder.create().show();
            }
        });
    }

    private void saveContacts() {
        SharedPreferences prefs = getSharedPreferences("myContacts", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(contacts);
        editor.putString("contacts", json);
        editor.apply();
    }

    private void loadContacts() {
        SharedPreferences prefs = getSharedPreferences("myContacts", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("contacts", "");
        Type type = new TypeToken<ArrayList<Contact>>(){}.getType();
        contacts = gson.fromJson(json, type);
        if (contacts == null) {
            contacts = new ArrayList<>();
        }
    }

    private void setContacts() {
        loadContacts();
        adapter = new ContactsAdapter(contacts);
        recyclerView.setAdapter(adapter);
    }

    private void clearFields() {
        nameEditText.getText().clear();
        nameEditText.setHintTextColor(Color.GRAY);
        phoneEditText.getText().clear();
        phoneEditText.setHintTextColor(Color.GRAY);
        emailEditText.getText().clear();
        emailEditText.setHintTextColor(Color.GRAY);
        addressEditText.getText().clear();
        addressEditText.setHintTextColor(Color.GRAY);
    }
}
