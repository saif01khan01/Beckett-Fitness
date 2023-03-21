package com.example.beckettfitness;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

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
}