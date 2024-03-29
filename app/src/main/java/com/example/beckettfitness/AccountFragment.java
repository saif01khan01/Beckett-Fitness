package com.example.beckettfitness;

import static android.content.Context.MODE_PRIVATE;
import static android.media.tv.TvContract.PreviewPrograms.COLUMN_WEIGHT;
import static com.example.beckettfitness.FoodDatabaseHelper.COLUMN_AGE;
import static com.example.beckettfitness.FoodDatabaseHelper.COLUMN_EXERCISE_LEVEL;
import static com.example.beckettfitness.FoodDatabaseHelper.COLUMN_GENDER;
import static com.example.beckettfitness.FoodDatabaseHelper.COLUMN_HEIGHT;
import static com.example.beckettfitness.FoodDatabaseHelper.COLUMN_USER_ID;
import static com.example.beckettfitness.FoodDatabaseHelper.COLUMN_WEIGHT_GOALS;
import static com.example.beckettfitness.FoodDatabaseHelper.TABLE_ACCOUNT;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountFragment extends Fragment {

    private Button logoutButton;
    private Button editAccountButton;

    View view;

    int goalNo;

    public void setGoalNo(int goalNo) {
        this.goalNo = goalNo;

    }

    public int getGoalNo() {
        return goalNo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);

        logoutButton = view.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the SharedPreferences object
                SharedPreferences sharedPrefs = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

                // Get the SharedPreferences.Editor object
                SharedPreferences.Editor editor = sharedPrefs.edit();

                // Remove the goal_calories key-value pair
                editor.remove("goal_calories");

                // Commit the changes
                editor.apply();

                FirebaseAuth.getInstance().signOut();
                // Add code to navigate to the login screen after logout
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        editAccountButton = view.findViewById(R.id.edit_account_button);
        editAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflate the edit account form layout
                View editAccountView = getLayoutInflater().inflate(R.layout.account_edit_form, null);

                // Create a dialog to show the edit account form
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(editAccountView);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Retrieve the values from the form
                        EditText ageEditText = editAccountView.findViewById(R.id.age_edittext);
                        EditText heightEditText = editAccountView.findViewById(R.id.height_edittext);
                        EditText weightEditText = editAccountView.findViewById(R.id.weight_edittext);
                        Spinner exerciseLevelSpinner = editAccountView.findViewById(R.id.exercise_level_spinner);
                        Spinner weightGoalSpinner = editAccountView.findViewById(R.id.weight_loss_goal_spinner);
                        Spinner genderSpinner = editAccountView.findViewById(R.id.gender_spinner);

                        String ageString = ageEditText.getText().toString();
                        String heightString = heightEditText.getText().toString();
                        String weightString = weightEditText.getText().toString();
                        String exerciseLevel = exerciseLevelSpinner.getSelectedItem().toString();
                        String weightGoal = weightGoalSpinner.getSelectedItem().toString();
                        String gender = genderSpinner.getSelectedItem().toString();

                        // Validate the values to ensure they match the rules
                        List<String> errorMessages = new ArrayList<>();
                        try {
                            int age = Integer.parseInt(ageString);
                            if (age < 0 || age > 80) {
                                errorMessages.add("Age must be between 0 and 80");
                            }
                        } catch (NumberFormatException e) {
                            errorMessages.add("Invalid age input");
                        }

                        try {
                            int height = Integer.parseInt(heightString);
                            if (height < 130 || height > 230) {
                                errorMessages.add("Height must be between 130 and 230");
                            }
                        } catch (NumberFormatException e) {
                            errorMessages.add("Invalid height input");
                        }

                        try {
                            int weight = Integer.parseInt(weightString);
                            if (weight < 40 || weight > 160) {
                                errorMessages.add("Weight must be between 40 and 160");
                            }
                        } catch (NumberFormatException e) {
                            errorMessages.add("Invalid weight input");
                        }

                        if (!errorMessages.isEmpty()) {
                            // Show error messages on the dialog
                            AlertDialog.Builder errorBuilder = new AlertDialog.Builder(getActivity());
                            errorBuilder.setTitle("Invalid Input");
                            errorBuilder.setMessage(TextUtils.join("\n", errorMessages));
                            errorBuilder.setPositiveButton("OK", null);
                            errorBuilder.show();
                        } else {
                            // Create an AccountDetails object with the retrieved values
                            AccountDetails accountDetails = new AccountDetails(Integer.parseInt(ageString), Integer.parseInt(heightString), Integer.parseInt(weightString), gender, exerciseLevel, weightGoal, FirebaseAuth.getInstance().getUid());
                            saveAccountDetails(accountDetails, v);
                        }
                    }

                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dismiss the dialog
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }


        });

        return view;
    }

    public void saveAccountDetails(AccountDetails accountDetails, View v) {
        // Add the AccountDetails object to the database
        FoodDatabaseHelper databaseHelper = new FoodDatabaseHelper(getContext());
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, FirebaseAuth.getInstance().getUid());
        values.put(COLUMN_AGE, accountDetails.getAge());
        values.put(COLUMN_GENDER, accountDetails.getGender());
        values.put(COLUMN_HEIGHT, accountDetails.getHeight());
        values.put(COLUMN_WEIGHT, accountDetails.getWeight());
        values.put(COLUMN_WEIGHT_GOALS, accountDetails.getWeightGoal());
        values.put(COLUMN_EXERCISE_LEVEL, accountDetails.getExerciseLevel());

        databaseHelper.removeAccInfo(FirebaseAuth.getInstance().getUid());

        long newRowId = db.insert(TABLE_ACCOUNT, null, values);

        // Show a toast message to indicate that the account details have been saved
        if (newRowId == -1) {
            // Insert failed
            Toast.makeText(getContext(), "Error: Failed to save account details", Toast.LENGTH_SHORT).show();
        } else {
            // Insert successful
            Toast.makeText(getContext(), "Account details saved", Toast.LENGTH_SHORT).show();
            summary_frag summary_frag = new summary_frag();
            makeApiCall(accountDetails.getAge(), accountDetails.getGender(), accountDetails.getHeight(), accountDetails.getWeight(), accountDetails.getExerciseLevel(), accountDetails.getWeightGoal(), v);
        }
        db.close();
    }

    public void makeApiCall(int age, String gender, double height, double weight, String activityLevel, String goals, View v) {

        String dbActivityLevel;

        if (activityLevel.equals("Sedentary (Little or no exercise)")) {
            dbActivityLevel = "level_1";
        } else if (activityLevel.equals("Light (light exercise or sports 1-3 days/week)")) {
            dbActivityLevel = "level_2";
        } else if (activityLevel.equals("Moderate (moderate exercise or sports 4-5 days/week)")) {
            dbActivityLevel = "level_3";
        } else if (activityLevel.equals("Active (intense exercise 3-4 times/week)")) {
            dbActivityLevel = "level_4";
        } else if (activityLevel.equals("Very Active (Intense exercise 6-7 times/week)")) {
            dbActivityLevel = "level_5";
        } else if (activityLevel.equals("Extra Active (Very intense exercise daily)")) {
            dbActivityLevel = "level_6";
        } else {
            dbActivityLevel = null;
            Toast.makeText(v.getContext(), "Check exercise level value", Toast.LENGTH_LONG).show();
        }
        String url = "https://fitness-calculator.p.rapidapi.com/dailycalorie?age=" + age +
                "&gender=" + gender +
                "&height=" + height +
                "&weight=" + weight +
                "&activitylevel=" + dbActivityLevel;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response
                        try {
                            FoodDatabaseHelper foodDatabaseHelper = new FoodDatabaseHelper(getContext());

                            SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("goal_calories", foodDatabaseHelper.getGoalCalories(response, goals));
                            editor.apply();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-RapidAPI-Key", "6c7b75ffd0msha91b4c46d576001p10417djsn2af2134e89a3");
                headers.put("X-RapidAPI-Host", "fitness-calculator.p.rapidapi.com");
                return headers;
            }
        };

        // Add the request to the Volley request queue
        RequestQueue queue = Volley.newRequestQueue(v.getContext());
        queue.add(jsonObjectRequest);
    }


}
