package com.example.phmsapp;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class VitalSignsActivity extends AppCompatActivity {

    private EditText editTextBloodPressure, editTextHeartRate, editTextOxygenSaturation, editTextBodyTemperature, editTextDate;
    private Button buttonSave, buttonHistory;

    private SharedPreferences sharedPreferences;
    private ArrayList<String> history;

    private String selectedDate = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vital_signs);
        setTitle("Vital Signs");

        editTextBloodPressure = findViewById(R.id.editTextBloodPressure);
        editTextHeartRate = findViewById(R.id.editTextHeartRate);
        editTextOxygenSaturation = findViewById(R.id.editTextOxygenSaturation);
        editTextBodyTemperature = findViewById(R.id.editTextBodyTemperature);
        editTextDate = findViewById(R.id.editTextDate);
        buttonSave = findViewById(R.id.buttonSave);
        buttonHistory = findViewById(R.id.buttonHistory);
        sharedPreferences = getSharedPreferences("vital_signs", MODE_PRIVATE);

        // Load history
        history = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            String historyItem = sharedPreferences.getString("history_" + i, "");
            if (!historyItem.isEmpty()) {
                history.add(historyItem);
            }
        }

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the DatePickerDialog when the date EditText is clicked
                showDatePickerDialog();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the entered values from the EditText views
                String bloodPressure = editTextBloodPressure.getText().toString().trim();
                String heartRate = editTextHeartRate.getText().toString().trim();
                String oxygenSaturation = editTextOxygenSaturation.getText().toString().trim();
                String BodyTemperature = editTextBodyTemperature.getText().toString().trim();
                String Date = selectedDate.trim();

                // Check if the entered values are valid and if not, make the field turn red
                boolean isInvalid = false;
                if (bloodPressure.isEmpty() || !isNumberFormat(bloodPressure)) {
                    editTextBloodPressure.setBackgroundColor(Color.RED);
                    isInvalid = true;
                } else {
                    editTextBloodPressure.setBackgroundColor(Color.TRANSPARENT);
                }
                if (heartRate.isEmpty() || isNumeric(heartRate)) {
                    editTextHeartRate.setBackgroundColor(Color.RED);
                    isInvalid = true;
                } else {
                    editTextHeartRate.setBackgroundColor(Color.TRANSPARENT);
                }
                if (oxygenSaturation.isEmpty() || isNumeric(oxygenSaturation)) {
                    editTextOxygenSaturation.setBackgroundColor(Color.RED);
                    isInvalid = true;
                } else {
                    editTextOxygenSaturation.setBackgroundColor(Color.TRANSPARENT);
                }
                if (BodyTemperature.isEmpty() || isNumeric(BodyTemperature)) {
                    editTextBodyTemperature.setBackgroundColor(Color.RED);
                    isInvalid = true;
                } else {
                    editTextBodyTemperature.setBackgroundColor(Color.TRANSPARENT);
                }
                if (Date.isEmpty() || isDateValid(Date, "MM/DD/YYYY")) {
                    editTextDate.setBackgroundColor(Color.RED);
                    isInvalid = true;
                } else {
                    editTextDate.setBackgroundColor(Color.TRANSPARENT);
                }

                if (isInvalid) {
                    Toast.makeText(VitalSignsActivity.this, "Please enter valid vital signs values", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save the entered values
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("blood_pressure", bloodPressure);
                editor.putString("heart_rate", heartRate);
                editor.putString("oxygen_saturation", oxygenSaturation);
                editor.putString("body_temperature", BodyTemperature);
                editor.putString("date", Date);
                editor.apply();

                // Get the saved values from history and add the new entry to history
                String savedValues = "\n-Blood pressure: " + bloodPressure + " mmHg" + "\n-Heart rate: " + heartRate + " bpm" + "\n-Oxygen saturation: " + oxygenSaturation + " %" + "\n-Body Temperature: " + BodyTemperature + " °F" + "\n Date: " + Date;
                history.add(0, savedValues);
                if (history.size() > 15) {
                    history.remove(history.size() - 1);
                }
                StringBuilder historyString = new StringBuilder();
                for (int i = 0; i < history.size(); i++) {
                    historyString.append(history.get(i)).append("\n");
                }
                editor.putString("history", historyString.toString().trim());
                editor.apply();

                Toast.makeText(VitalSignsActivity.this, "Vital signs saved: " + savedValues, Toast.LENGTH_SHORT).show();

                // Clear the fields
                editTextBloodPressure.getText().clear();
                editTextHeartRate.getText().clear();
                editTextOxygenSaturation.getText().clear();
                editTextBodyTemperature.getText().clear();
                editTextDate.getText().clear();

                // Reset the selected date
                selectedDate = "";
            }
        });


        buttonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the saved history and sort it chronologically
                String historyString = sharedPreferences.getString("history", "");
                if (!historyString.isEmpty()) {
                    // Show the last 10 entries
                    int numEntriesToShow = Math.min(10, history.size());
                    StringBuilder historyToShow = new StringBuilder();
                    for (int i = 0; i < numEntriesToShow; i++) {
                        historyToShow.append(history.get(i)).append("\n");
                    }

                    // Create and show the dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(VitalSignsActivity.this);
                    builder.setTitle("History");
                    builder.setMessage("Last " + numEntriesToShow + " entries:\n" + historyToShow);
                    builder.setPositiveButton("Clear history", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Clear the history
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.remove("history");
                            for (int i = 1; i <= 15; i++) {
                                editor.remove("history_" + i);
                            }
                            editor.apply();
                            history.clear();
                            Toast.makeText(VitalSignsActivity.this, "History cleared", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    Toast.makeText(VitalSignsActivity.this, "No history to show", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void showDatePickerDialog() {
        // Get the current date as a default date
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog with the current date as a default date
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                VitalSignsActivity.this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    // Store the selected date in the selectedDate variable
                    selectedDate = String.format("%02d/%02d/%04d", monthOfYear + 1, dayOfMonth, year1);
                    editTextDate.setText(selectedDate);
                },
                year, month, day
        );

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    private boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return true;
        }
        return false;
    }

    private boolean isNumberFormat(String value) {
        String[] parts = value.split("/");
        if (parts.length != 2) {
            return false;
        }
        try {
            Integer.parseInt(parts[0]);
            Integer.parseInt(parts[1]);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }



    public static boolean isDateValid(String dateStr, String format) {
        try {
            // Create a DateTimeFormatter object with the expected format
            DateTimeFormatter formatter = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                formatter = DateTimeFormatter.ofPattern(format);
            }

            // Parse the date string using the formatter
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDate.parse(dateStr, formatter);
            }

            // If parsing is successful, return True
            return true;
        } catch (Exception e) {
            // If parsing fails, return False
            return false;
        }
    }
}