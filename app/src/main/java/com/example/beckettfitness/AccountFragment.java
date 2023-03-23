package com.example.beckettfitness;

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
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class AccountFragment extends Fragment {

    private Button logoutButton;
    private Button editAccountButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        logoutButton = view.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        int age = Integer.parseInt(ageEditText.getText().toString());

                        EditText heightEditText = editAccountView.findViewById(R.id.height_edittext);
                        int height = Integer.parseInt(heightEditText.getText().toString());

                        EditText weightEditText = editAccountView.findViewById(R.id.weight_edittext);
                        int weight = Integer.parseInt(weightEditText.getText().toString());

                        Spinner exerciseLevelSpinner = editAccountView.findViewById(R.id.exercise_level_spinner);
                        String exerciseLevel = exerciseLevelSpinner.getSelectedItem().toString();


                        Spinner weightGoalSpinner = editAccountView.findViewById(R.id.weight_loss_goal_spinner);
                        String weightGoal = weightGoalSpinner.getSelectedItem().toString();

                        Spinner genderSpinner = editAccountView.findViewById(R.id.gender_spinner);
                        String gender = genderSpinner.getSelectedItem().toString();

                        // Create an AccountDetails object with the retrieved values
                        AccountDetails accountDetails = new AccountDetails(age, height, weight, gender, exerciseLevel , weightGoal, FirebaseAuth.getInstance().getUid());

                        saveAccountDetails(accountDetails);
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

    private void saveAccountDetails(AccountDetails accountDetails) {
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
            summary_frag.makeApiCall(accountDetails.getAge(), accountDetails.getGender(), accountDetails.getHeight(), accountDetails.getWeight(), accountDetails.getExerciseLevel(), accountDetails.getWeightGoal(), getView());
        }
        db.close();
    }




}
