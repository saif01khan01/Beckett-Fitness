package com.example.beckettfitness;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link summary_frag#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class summary_frag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    TextView caloriesTotalTextView;

    TextView goalCalorieTextView;

    int calorieGoal;

    JSONObject rp;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     */
    // TODO: Rename and change types and number of parameters
    public static summary_frag newInstance(String param1, String param2) {
        summary_frag fragment = new summary_frag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public summary_frag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //goalCalorieTextView = (TextView)getView().findViewById(R.id.goal_value);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary_frag, container, false);

        // Get the current user's ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Retrieve the total calories for the user
        FoodDatabaseHelper databaseHelper = new FoodDatabaseHelper(getContext());
        int caloriesTotal = databaseHelper.getCaloriesTotal(userId);


        // Set the value in the TextView
        caloriesTotalTextView = view.findViewById(R.id.total_calories_value_textview);
        caloriesTotalTextView.setText(String.valueOf(caloriesTotal));

        goalCalorieTextView = view.findViewById(R.id.goal_value);
        AccountFragment accountFragment = new AccountFragment();
        goalCalorieTextView.setText(String.valueOf(accountFragment.getGoalNo()));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Retrieve the total calories for the user
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FoodDatabaseHelper databaseHelper = new FoodDatabaseHelper(getContext());
        int caloriesTotal = databaseHelper.getCaloriesTotal(userId);

        // Update the total calories TextView
        caloriesTotalTextView.setText(String.valueOf(caloriesTotal));

        // Retrieve the goal calories from SharedPreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int goalCalories = sharedPreferences.getInt("goal_calories", 0);

        // Update the goal calories TextView
        goalCalorieTextView.setText(String.valueOf(goalCalories));
    }





}



