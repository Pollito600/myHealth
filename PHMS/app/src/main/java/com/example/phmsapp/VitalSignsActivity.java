package com.example.phmsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class VitalSignsActivity extends AppCompatActivity {

    private EditText editTextBloodPressure, editTextHeartRate, editTextOxygenSaturation;
    private Button buttonSave;
    private TextView textViewSavedValues;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vital_signs);

        editTextBloodPressure = findViewById(R.id.editTextBloodPressure);
        editTextHeartRate = findViewById(R.id.editTextHeartRate);
        editTextOxygenSaturation = findViewById(R.id.editTextOxygenSaturation);
        buttonSave = findViewById(R.id.buttonSave);
        textViewSavedValues = findViewById(R.id.textViewSavedValues);
        sharedPreferences = getSharedPreferences("vital_signs", MODE_PRIVATE);

        // Load saved values
        String savedBloodPressure = sharedPreferences.getString("blood_pressure", "");
        String savedHeartRate = sharedPreferences.getString("heart_rate", "");
        String savedOxygenSaturation = sharedPreferences.getString("oxygen_saturation", "");
        if (!savedBloodPressure.isEmpty() && !savedHeartRate.isEmpty() && !savedOxygenSaturation.isEmpty()) {
            textViewSavedValues.setText("Last saved vital signs: Blood pressure = " + savedBloodPressure +
                    ", Heart rate = " + savedHeartRate + ", Oxygen saturation = " + savedOxygenSaturation);
        }

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the entered values from the EditText views
                String bloodPressure = editTextBloodPressure.getText().toString().trim();
                String heartRate = editTextHeartRate.getText().toString().trim();
                String oxygenSaturation = editTextOxygenSaturation.getText().toString().trim();

                // Validate the entered values
                if (bloodPressure.isEmpty() || heartRate.isEmpty() || oxygenSaturation.isEmpty()) {
                    Toast.makeText(VitalSignsActivity.this, "Please enter all vital signs values", Toast.LENGTH_SHORT).show();
                } else if (!isNumeric(bloodPressure) || !isNumeric(heartRate) || !isNumeric(oxygenSaturation)) {
                    Toast.makeText(VitalSignsActivity.this, "Please enter valid vital signs values", Toast.LENGTH_SHORT).show();
                } else {
                    // Save the entered values
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("blood_pressure", bloodPressure);
                    editor.putString("heart_rate", heartRate);
                    editor.putString("oxygen_saturation", oxygenSaturation);
                    editor.apply();
                    Toast.makeText(VitalSignsActivity.this, "Vital signs saved: Blood pressure = " + bloodPressure +
                            ", Heart rate = " + heartRate + ", Oxygen saturation = " + oxygenSaturation, Toast.LENGTH_SHORT).show();

                    // Update saved values text view
                    textViewSavedValues.setText("Last saved vital signs: Blood pressure = " + bloodPressure +
                            ", Heart rate = " + heartRate + ", Oxygen saturation = " + oxygenSaturation);
                }
            }
        });
    }

    // Helper function to check if a string is numeric
    private boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
