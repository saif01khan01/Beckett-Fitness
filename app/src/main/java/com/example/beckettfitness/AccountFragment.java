package com.example.beckettfitness;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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

                        // Create an AccountDetails object with the retrieved values
                        AccountDetails accountDetails = new AccountDetails(age, height, weight, exerciseLevel);
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


}
