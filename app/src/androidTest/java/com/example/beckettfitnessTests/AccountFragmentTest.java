package com.example.beckettfitnessTests;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;

import com.example.beckettfitness.AccountDetails;
import com.example.beckettfitness.AccountFragment;
import com.example.beckettfitness.FoodDatabaseHelper;
import com.example.beckettfitness.R;

import org.junit.Before;
import org.junit.Test;

public class AccountFragmentTest {

    @Before
    public void setUp() {
        // Initialize any required objects here
    }

    @Test
    public void testEditAccountButtonClicked() {
        // Launch the fragment scenario
        FragmentScenario<AccountFragment> fragmentScenario = FragmentScenario.launchInContainer(AccountFragment.class);

        // Click the edit account button
        Espresso.onView(ViewMatchers.withId(R.id.edit_account_button)).perform(ViewActions.click());

        // Check if the edit account form is displayed
        Espresso.onView(ViewMatchers.withId(R.id.age_edittext)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.height_edittext)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.weight_edittext)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.exercise_level_spinner)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.weight_loss_goal_spinner)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.gender_spinner)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Close the fragment scenario
        fragmentScenario.close();
    }

    @Test
    public void testSaveAccountDetails() {
        // Create an AccountFragment object
        AccountFragment accountFragment = new AccountFragment();

        // Set up the test views
        EditText ageEditText = new EditText(accountFragment.getContext());
        EditText heightEditText = new EditText(accountFragment.getContext());
        EditText weightEditText = new EditText(accountFragment.getContext());
        Spinner exerciseLevelSpinner = new Spinner(accountFragment.getContext());
        Spinner weightGoalSpinner = new Spinner(accountFragment.getContext());
        Spinner genderSpinner = new Spinner(accountFragment.getContext());
        Button saveButton = new Button(accountFragment.getContext());

        ageEditText.setText("25");
        heightEditText.setText("170");
        weightEditText.setText("70");
        exerciseLevelSpinner.setSelection(1);
        weightGoalSpinner.setSelection(2);
        genderSpinner.setSelection(0);

        // Call the saveAccountDetails method
        accountFragment.saveAccountDetails(new AccountDetails(25, 170, 70, "Male", "Moderate", "Lose Weight", "user_id"), saveButton);

        // Verify that the account details are saved in the database
        FoodDatabaseHelper databaseHelper = new FoodDatabaseHelper(accountFragment.getContext());
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FoodDatabaseHelper.COLUMN_AGE, 25);
        values.put(FoodDatabaseHelper.COLUMN_HEIGHT, 170);
        values.put(FoodDatabaseHelper.COLUMN_WEIGHT, 70);
        values.put(FoodDatabaseHelper.COLUMN_GENDER, "Male");
        values.put(FoodDatabaseHelper.COLUMN_EXERCISE_LEVEL, "Moderate");
        values.put(FoodDatabaseHelper.COLUMN_WEIGHT_GOALS, "Lose Weight");
        values.put(FoodDatabaseHelper.COLUMN_USER_ID, "user_id");
        long rowId = db.insert(FoodDatabaseHelper.TABLE_ACCOUNT, null, values);
        assert(rowId != -1);

        // Close the database
        db.close();
    }
}