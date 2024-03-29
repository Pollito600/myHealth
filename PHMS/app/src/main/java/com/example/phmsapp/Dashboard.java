package com.example.phmsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Dashboard extends AppCompatActivity {

    private Button dashNotesBtn, dashDietBtn, dashMedicationBtn, dashSearchBtn, dashVitalSignsBtn, dashbtnContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        dashNotesBtn = findViewById(R.id.btnNotes);
        dashDietBtn = findViewById(R.id.btnDiet);
        dashMedicationBtn = findViewById(R.id.btnMedication);
        dashVitalSignsBtn = findViewById(R.id.btnVitalSigns);
        dashbtnContacts = findViewById(R.id.btnContacts);
        dashNotesBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Dashboard.this, NotesActivity.class));
            }
        });
        dashDietBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Dashboard.this, DietActivity.class));
            }
        });
        dashMedicationBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Dashboard.this, MedicationActivity.class));
            }
        });

        dashVitalSignsBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Dashboard.this, VitalSignsActivity.class));
            }
        });

        dashbtnContacts.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Dashboard.this, ContactsActivity.class));
            }
        });
    }
}
