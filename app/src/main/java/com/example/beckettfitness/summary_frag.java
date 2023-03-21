package com.example.beckettfitness;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView caloriesTotalTextView;

    TextView goalCalorieTextView;

    int calorieGoal;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment summary_frag.
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

        if(calorieGoal == 0) {
            goalCalorieTextView.setText("Set up Account. go to Account > Edit account");
        } else {
            goalCalorieTextView.setText(String.valueOf(calorieGoal));
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Get the current user's ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Retrieve the total calories for the user
        FoodDatabaseHelper databaseHelper = new FoodDatabaseHelper(getContext());
        int caloriesTotal = databaseHelper.getCaloriesTotal(userId);

        // Set the value in the TextView
        caloriesTotalTextView.setText(String.valueOf(caloriesTotal));

    }

    public void makeApiCall(int age, String gender, double height, double weight, String activityLevel, String goals) {
        String url = "https://fitness-calculator.p.rapidapi.com/dailycalorie?age=" + age +
                "&gender=" + gender +
                "&height=" + height +
                "&weight=" + weight +
                "&activitylevel=" + activityLevel;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response
                        try {
                           FoodDatabaseHelper foodDatabaseHelper = new FoodDatabaseHelper(getContext());

                           if(goals.equals("Maintain weight")){
                               calorieGoal = foodDatabaseHelper.getMaintainWeightCalories(response);
                           } else if (goals.equals("Mild weight loss")) {
                               calorieGoal = foodDatabaseHelper.getMildWeightLossCalories(response);
                           }else if (goals.equals("Weight loss")) {
                               calorieGoal = foodDatabaseHelper.getMildWeightLossCalories(response);
                           }else if (goals.equals("Extreme weight loss")) {
                               calorieGoal = foodDatabaseHelper.getMildWeightLossCalories(response);
                           }else if (goals.equals("Mild weight gain")) {
                               calorieGoal = foodDatabaseHelper.getMildWeightLossCalories(response);
                           }else if (goals.equals("Weight gain")) {
                               calorieGoal = foodDatabaseHelper.getMildWeightLossCalories(response);
                           }else if (goals.equals("Extreme weight gain")) {
                               calorieGoal = foodDatabaseHelper.getMildWeightLossCalories(response);
                           } else {
                               Toast.makeText(getContext(), "Set up account. Go to Account > Edit Account", Toast.LENGTH_SHORT).show();
                           }
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
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(jsonObjectRequest);
    }



}