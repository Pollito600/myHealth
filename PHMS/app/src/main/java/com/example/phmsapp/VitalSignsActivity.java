package com.example.phmsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;


import java.util.ArrayList;
import java.util.Collections;

public class VitalSignsActivity extends AppCompatActivity {

    private EditText editTextBloodPressure, editTextHeartRate, editTextOxygenSaturation, editTextBodyTemperature, editTextDate;
    private Button buttonSave, buttonHistory;
    private TextView textViewSavedValues;
    private SharedPreferences sharedPreferences;
    private ArrayList<String> history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vital_signs);

        editTextBloodPressure = findViewById(R.id.editTextBloodPressure);
        editTextHeartRate = findViewById(R.id.editTextHeartRate);
        editTextOxygenSaturation = findViewById(R.id.editTextOxygenSaturation);
        editTextBodyTemperature = findViewById(R.id.editTextBodyTemperature);
        editTextDate = findViewById(R.id.editTextDate);
        buttonSave = findViewById(R.id.buttonSave);
        buttonHistory = findViewById(R.id.buttonHistory);
        textViewSavedValues = findViewById(R.id.textViewSavedValues);
        sharedPreferences = getSharedPreferences("vital_signs", MODE_PRIVATE);

        // Load saved values
        String savedBloodPressure = sharedPreferences.getString("blood_pressure", "");
        String savedHeartRate = sharedPreferences.getString("heart_rate", "");
        String savedOxygenSaturation = sharedPreferences.getString("oxygen_saturation", "");
        String savedBodyTemperature = sharedPreferences.getString("body_temperature", "");
        String savedDate = sharedPreferences.getString("date", "");
        if (!savedBloodPressure.isEmpty() && !savedHeartRate.isEmpty() && !savedOxygenSaturation.isEmpty() && !savedDate.isEmpty()) {
            textViewSavedValues.setText("Last saved vital signs: Blood pressure= " + savedBloodPressure +
                    ", Heart rate= " + savedHeartRate + ", Oxygen saturation= " + savedOxygenSaturation + ", Body Temperature= " + savedBodyTemperature + ", Date: " + savedDate);
        }

        // Load history
        history = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            String historyItem = sharedPreferences.getString("history_" + i, "");
            if (!historyItem.isEmpty()) {
                history.add(historyItem);
            }
        }

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the entered values from the EditText views
                String bloodPressure = editTextBloodPressure.getText().toString().trim();
                String heartRate = editTextHeartRate.getText().toString().trim();
                String oxygenSaturation = editTextOxygenSaturation.getText().toString().trim();
                String BodyTemperature = editTextBodyTemperature.getText().toString().trim();
                String Date = editTextDate.getText().toString().trim();

                // Validate the entered values
                if (bloodPressure.isEmpty() || heartRate.isEmpty() || oxygenSaturation.isEmpty() || BodyTemperature.isEmpty() || Date.isEmpty()) {
                    Toast.makeText(VitalSignsActivity.this, "Please enter all vital signs values", Toast.LENGTH_SHORT).show();
                } else if (!isNumeric(bloodPressure) || !isNumeric(heartRate) || !isNumeric(oxygenSaturation) || !isNumeric(BodyTemperature)) {
                    Toast.makeText(VitalSignsActivity.this, "Please enter valid vital signs values", Toast.LENGTH_SHORT).show();
                } else {
                    // Save the entered values
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("blood_pressure", bloodPressure);
                    editor.putString("heart_rate", heartRate);
                    editor.putString("oxygen_saturation", oxygenSaturation);
                    editor.putString("body_temperature", BodyTemperature);
                    editor.putString("Date", Date);
                    editor.apply();

                    // Get the saved values from history and add the new entry to history
                    String savedValues = "Blood pressure= " + bloodPressure + ", Heart rate= " + heartRate + ", Oxygen saturation= " + oxygenSaturation + ", Body Temperature= " + BodyTemperature + ", Date: " + Date;
                    history.add(0, savedValues);
                    if (history.size() > 15) {
                        history.remove(history.size() - 1);
                    }
                    String historyString = "";
                    for (int i = 0; i < history.size(); i++) {
                        historyString += history.get(i) + "\n";
                    }
                    editor.putString("history", historyString.trim());
                    editor.apply();

                    Toast.makeText(VitalSignsActivity.this, "Vital signs saved: " + savedValues, Toast.LENGTH_SHORT).show();

                    // Update saved values text view
                    textViewSavedValues.setText("Last saved vital signs: " + savedValues);

                    // Clear the fields
                    editTextBloodPressure.getText().clear();
                    editTextHeartRate.getText().clear();
                    editTextOxygenSaturation.getText().clear();
                    editTextBodyTemperature.getText().clear();
                    editTextDate.getText().clear();
                }
            }
        });

        buttonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the saved history and sort it chronologically
                String historyString = sharedPreferences.getString("history", "");
                if (!historyString.isEmpty()) {
                    String[] historyArray = historyString.split("\n");
                    ArrayList<String> historyList = new ArrayList<>();
                    Collections.addAll(historyList, historyArray);
                    Collections.reverse(historyList);

                    // Show the last 10 entries
                    int numEntriesToShow = Math.min(10, historyList.size());
                    String historyToShow = "";
                    for (int i = 0; i < numEntriesToShow; i++) {
                        historyToShow += historyList.get(i) + "\n" + "\n";
                    }

                    // Create and show the dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(VitalSignsActivity.this);
                    builder.setTitle("History");
                    builder.setMessage("Last " + numEntriesToShow + " entries:\n\n" + historyToShow);
                    builder.setPositiveButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    Toast.makeText(VitalSignsActivity.this, "No history yet", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    private boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
