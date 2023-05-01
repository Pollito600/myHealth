package com.example.phmsapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class MedicationActivity extends AppCompatActivity {

    private ListView medicationListView;
    private ArrayAdapter<String> medicationAdapter;
    private ArrayList<Medication> medicationList = new ArrayList<>();

    private void saveMedication(Medication medication) {
        SharedPreferences sharedPreferences = getSharedPreferences("medications", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(medication);

        // Get the medications set from shared preferences
        Set<String> medicationsSet = sharedPreferences.getStringSet("medicationsSet", new HashSet<String>());

        // Convert the medications set to an ArrayList of Medication objects
        ArrayList<Medication> medicationsList = new ArrayList<>();
        for (String medicationJson : medicationsSet) {
            medicationsList.add(gson.fromJson(medicationJson, Medication.class));
        }
        // Add the new medication to the ArrayList
        medicationsList.add(medication);

        // Convert the ArrayList of Medication objects back to a set of Strings
        Set<String> updatedMedicationsSet = new HashSet<>();
        for (Medication m : medicationsList) {
            updatedMedicationsSet.add(gson.toJson(m));
        }

        // Save the updated medications set to shared preferences
        editor.putStringSet("medicationsSet", updatedMedicationsSet);
        editor.apply();

        Toast.makeText(this, "Medication saved", Toast.LENGTH_SHORT).show();
    }
    private void deleteMedication(Medication medication) {
        // Retrieve the medications set from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("medications", MODE_PRIVATE);
        Set<String> medicationsSet = sharedPreferences.getStringSet("medicationsSet", new HashSet<String>());

        // Remove the medication from the set
        Gson gson = new Gson();
        String json = gson.toJson(medication);
        medicationsSet.remove(json);

        // Update shared preferences with the modified set
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("medicationsSet", medicationsSet);
        editor.apply();

        // Notify the user that the medication has been deleted
        Toast.makeText(this, "Medication deleted", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication);
        setTitle("Medication");

        Button addMedicationButton = findViewById(R.id.addMedicationButton);
        addMedicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MedicationActivity.this);
                builder.setTitle("Add Medication");

                View view = LayoutInflater.from(MedicationActivity.this).inflate(R.layout.dialog_add_medication, null);
                builder.setView(view);

                final EditText nameEditText = view.findViewById(R.id.medicationNameEditText);
                final EditText rxNumberEditText = view.findViewById(R.id.rxNumberEditText);
                final EditText dosageEditText = view.findViewById(R.id.dosageEditText);
                final EditText strengthEditText = view.findViewById(R.id.strengthEditText);
                final EditText frequencyEditText = view.findViewById(R.id.frequencyEditText);
                final EditText refillsEditText = view.findViewById(R.id.refillsEditText);
                final EditText expirationEditText = view.findViewById(R.id.expirationEditText);
                final EditText pharmacyPhoneEditText = view.findViewById(R.id.pharmacyPhoneEditText);

                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = nameEditText.getText().toString().trim();
                        String rxNumber = rxNumberEditText.getText().toString().trim();
                        String dosage = dosageEditText.getText().toString().trim();
                        String strength = strengthEditText.getText().toString().trim();
                        String frequency = frequencyEditText.getText().toString().trim();
                        String refills = refillsEditText.getText().toString().trim();
                        String expiration = expirationEditText.getText().toString().trim();
                        String pharmacyPhone = pharmacyPhoneEditText.getText().toString().trim();

                        Medication medication = new Medication(name, rxNumber, dosage, strength, frequency, refills, expiration, pharmacyPhone);

                        saveMedication(medication);

                        medicationAdapter.add(name);
                        medicationAdapter.notifyDataSetChanged();

                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(MedicationActivity.this);
        builder.setTitle("Edit Medication");
        medicationListView = findViewById(R.id.medicationList);
        ArrayList<Medication> medicationList = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("medications", MODE_PRIVATE);
        Set<String> medicationsSet = sharedPreferences.getStringSet("medicationsSet", new HashSet<String>());

        Gson gson = new Gson();
        for (String json : medicationsSet) {
            Medication medication = gson.fromJson(json, Medication.class);
            medicationList.add(medication);

        }
        ArrayList<String> medicationNames = new ArrayList<>();
        for (Medication medication : medicationList) {
            medicationNames.add(medication.getName());
        }
        medicationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, medicationNames);
        medicationListView.setAdapter(medicationAdapter);

        medicationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Medication medication = medicationList.get(position);



                View dialogView = LayoutInflater.from(MedicationActivity.this).inflate(R.layout.dialog_add_medication, null);
                // Initialize dialog views
                EditText medicationNameEditText = dialogView.findViewById(R.id.medicationNameEditText);
                EditText medicationDosageEditText = dialogView.findViewById(R.id.dosageEditText);
                EditText medicationFrequencyEditText = dialogView.findViewById(R.id.frequencyEditText);
                EditText medicationRXEditText = dialogView.findViewById(R.id.rxNumberEditText);
                EditText medicationStrengthEditText = dialogView.findViewById(R.id.strengthEditText);
                EditText medicationRefillsEditText = dialogView.findViewById(R.id.refillsEditText);
                EditText medicationExpirationEditText = dialogView.findViewById(R.id.expirationEditText);
                EditText medicationPharamacyPhoneEditText = dialogView.findViewById(R.id.pharmacyPhoneEditText);

                medicationNameEditText.setText(medication.getName());
                medicationDosageEditText.setText(medication.getDosage());
                medicationFrequencyEditText.setText(medication.getFrequency());
                medicationRXEditText.setText(medication.getRxNumber());
                medicationStrengthEditText.setText(medication.getStrength());
                medicationRefillsEditText.setText(medication.getRefills());
                medicationExpirationEditText.setText(medication.getExpirationDate());
                medicationPharamacyPhoneEditText.setText(medication.getPharmacyPhoneNumber());

                builder.setView(dialogView);

                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String medicationName = medicationNameEditText.getText().toString();
                        String medicationDosage = medicationDosageEditText.getText().toString();
                        String medicationFrequency = medicationFrequencyEditText.getText().toString();
                        String medicationRX = medicationRXEditText.getText().toString();
                        String medicationStrength = medicationStrengthEditText.getText().toString();
                        String medicationRefills = medicationRefillsEditText.getText().toString();
                        String medicationExpiration = medicationExpirationEditText.getText().toString();
                        String pharmacyPhone = medicationPharamacyPhoneEditText.getText().toString();

                        // Update medication object
                        medication.setName(medicationName);
                        medication.setDosage(medicationDosage);
                        medication.setFrequency(medicationFrequency);
                        medication.setRxNumber(medicationRX);
                        medication.setStrength(medicationStrength);
                        medication.setRefills(medicationRefills);
                        medication.setExpirationDate(medicationExpiration);
                        medication.setPharmacyPhoneNumber(pharmacyPhone);

                        // Update medication in the database
                        //MedicationDatabase.getInstance(MedicationActivity.this).medicationDao().update(medication);
                        saveMedication(medication);

                        medicationAdapter.add(medicationName);
                        medicationAdapter.notifyDataSetChanged();

                        // Update medication list view
                        medicationList.set(position, medication);
                        //medicationListAdapter.notifyDataSetChanged();
                        ArrayList<String> medicationNameList = new ArrayList<>();
                        for (Medication med : medicationList) {
                            medicationNameList.add(med.getName());
                        }
                        medicationAdapter.clear();
                        medicationAdapter.addAll(medicationNameList);
                        medicationAdapter.notifyDataSetChanged();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                });
                builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Medication medication = medicationList.get(position);

                        deleteMedication(medication);


                        medicationList.remove(position);
                        ArrayList<String> medicationNameList = new ArrayList<>();
                        for (Medication med : medicationList) {
                            medicationNameList.add(med.getName());
                        }
                        medicationAdapter.clear();
                        medicationAdapter.addAll(medicationNameList);
                        medicationAdapter.notifyDataSetChanged();
                        //medication.setDeleted(true);
                    }
                });
                builder.create().show();
            }
        });

    }
}