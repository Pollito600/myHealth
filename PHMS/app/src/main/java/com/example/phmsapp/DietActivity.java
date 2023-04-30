package com.example.phmsapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.widget.TimePicker;
import android.widget.Toast;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class DietActivity extends AppCompatActivity {
    private EditText editTextFoodName, editTextDate, editTextTotalCalories,
            editTextTimeEaten, editTextWeight, editTextDailyCalorieGoal;

    private SharedPreferences sharedPreferences;

    private Button buttonSave, buttonHistory;

    private ArrayList<String> history;

    private String selectedDate = "";
    private String selectedTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);
        setTitle("Diet");

        editTextFoodName = findViewById(R.id.editTextFoodName);
        editTextTotalCalories = findViewById(R.id.editTextTotalCalories);
        editTextTimeEaten = findViewById(R.id.editTextTimeEaten);
        editTextWeight = findViewById(R.id.editTextWeight);
        editTextDailyCalorieGoal = findViewById(R.id.editTextDailyCalorieGoal);
        editTextDate = findViewById(R.id.editTextDate);

        buttonSave = findViewById(R.id.buttonSave);
        buttonHistory = findViewById(R.id.buttonHistory);

        sharedPreferences = getSharedPreferences("diet", MODE_PRIVATE);

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

        editTextTimeEaten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String foodName = editTextFoodName.getText().toString().trim();
                String totalCalories = editTextTotalCalories.getText().toString().trim();
                String timeEaten = editTextTimeEaten.getText().toString().trim();
                String weight = editTextWeight.getText().toString().trim();
                String dailyCalorieGoal = editTextDailyCalorieGoal.getText().toString().trim();
                String date = selectedDate.trim();

                // Check if the entered values are valid and if not, make the field turn red
                boolean isInvalid = false;
                if (foodName.isEmpty()) {
                    editTextFoodName.setHintTextColor(Color.RED);
                    isInvalid = true;
                } else {
                    editTextFoodName.setHintTextColor(Color.TRANSPARENT);
                }
                if (totalCalories.isEmpty() || !isNumeric(totalCalories)) {
                    editTextTotalCalories.setHintTextColor(Color.RED);
                    isInvalid = true;
                } else {
                    editTextTotalCalories.setHintTextColor(Color.TRANSPARENT);
                }
                if (timeEaten.isEmpty()) {
                    editTextTimeEaten.setHintTextColor(Color.RED);
                    isInvalid = true;
                } else {
                    editTextTimeEaten.setHintTextColor(Color.TRANSPARENT);
                }
                if (weight.isEmpty() || !isNumeric(weight)) {
                    editTextWeight.setHintTextColor(Color.RED);
                    isInvalid = true;
                } else {
                    editTextWeight.setHintTextColor(Color.TRANSPARENT);
                }
                if (dailyCalorieGoal.isEmpty() || !isNumeric(dailyCalorieGoal)) {
                    editTextDailyCalorieGoal.setHintTextColor(Color.RED);
                    isInvalid = true;
                } else {
                    editTextDailyCalorieGoal.setHintTextColor(Color.TRANSPARENT);
                }
                if (date.isEmpty() || isDateValid(date, "MM/DD/YYYY")) {
                    editTextDate.setHintTextColor(Color.RED);
                    isInvalid = true;
                } else {
                    editTextDate.setHintTextColor(Color.TRANSPARENT);
                }

                if (isInvalid) {
                    Toast.makeText(DietActivity.this, "Please enter valid diet values", Toast.LENGTH_SHORT).show();
                    return;
                }

                int numTotalCal, numDailyCalGoal;

                numTotalCal = Integer.parseInt(totalCalories);
                numDailyCalGoal = Integer.parseInt(dailyCalorieGoal);

                // Alert user that total calories are greater than their daily calorie goal
                if (numTotalCal > numDailyCalGoal) {
                    Toast.makeText(DietActivity.this, "WARNING: Total Calories intake is GREATER than Daily Calorie Goal", Toast.LENGTH_SHORT).show();
                }

                // Save the entered values
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("foodName", foodName);
                editor.putString("totalCalories", totalCalories);
                editor.putString("timeEaten", timeEaten);
                editor.putString("weight", weight);
                editor.putString("dailyCalorieGoal", dailyCalorieGoal);
                editor.putString("date", date);
                editor.apply();

                // Get the saved values from history and add the new entry to history
                String savedValues = "\n- Food Name: " + foodName +
                        "\n- Total Calories: " + totalCalories + "\n- Time Eaten: " + timeEaten + "\n- Weight: " + weight +
                        "\n- Daily Calorie Goal: " + dailyCalorieGoal + "\n- Date: " + date;
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

                Toast.makeText(DietActivity.this, "Diet saved: " + savedValues, Toast.LENGTH_SHORT).show();

                // Clear the fields
                editTextFoodName.getText().clear();
                editTextTotalCalories.getText().clear();
                editTextTimeEaten.getText().clear();
                editTextWeight.getText().clear();
                editTextDailyCalorieGoal.getText().clear();
                editTextDate.getText().clear();
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(DietActivity.this);
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
                            Toast.makeText(DietActivity.this, "History cleared", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    Toast.makeText(DietActivity.this, "No history yet", Toast.LENGTH_SHORT).show();
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
                DietActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Store the selected date in the selectedDate variable
                        selectedDate = String.format("%02d/%02d/%04d", monthOfYear + 1, dayOfMonth, year);
                        editTextDate.setText(selectedDate);
                    }
                },
                year, month, day
        );

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                DietActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String amOrPm = (hourOfDay < 12) ? "am" : "pm";
                        int hour = (hourOfDay > 12) ? (hourOfDay - 12) : hourOfDay;
                        hour = (hour == 0) ? 12 : hour;

                        selectedTime = String.format(Locale.getDefault(), "%02d:%02d %s", hour, minute, amOrPm);
                        editTextTimeEaten.setText(selectedTime);
                    }
                }, hour, minute, false);
        timePickerDialog.show();
    }

    private boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
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