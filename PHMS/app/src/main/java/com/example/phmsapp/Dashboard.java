package com.example.phmsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*case R.id.menu_diet:
                startActivity(new Intent(this, DietActivity.class));
                return true;
            case R.id.menu_medication:
                startActivity(new Intent(this, MedicationActivity.class));
                return true;*/
            case R.id.menu_notes:
                startActivity(new Intent(this, NotesActivity.class));
                Log.d("NotesActivity", "onCreate() called");
                return true;
            /*case R.id.menu_search:
                startActivity(new Intent(this, SearchActivity.class));
                return true;
            case R.id.menu_vital_signs:
                startActivity(new Intent(this, VitalSignsActivity.class));
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
