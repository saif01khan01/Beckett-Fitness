package com.example.beckettfitness;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link daily_meal_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class daily_meal_frag extends Fragment {

    private Button breakfast_add;
    private RecyclerView recyclerView;
    private FoodListAdapter foodListAdapter;

    public daily_meal_frag() {
        // Required empty public constructor
    }

    public static daily_meal_frag newInstance() {
        return new daily_meal_frag();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_meal_frag, container, false);

        breakfast_add = (Button) view.findViewById(R.id.breakfast_add);
        recyclerView = (RecyclerView) view.findViewById(R.id.add_meal_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        foodListAdapter = new FoodListAdapter(new ArrayList<FoodItem>(), getActivity());
        recyclerView.setAdapter(foodListAdapter);

        breakfast_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity2();
            }
        });

        loadFoodItems();

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();

        // Refresh the data
        loadFoodItems();
        int totalCalories = getTotalCaloriesConsumed();

        // Update the adapter with the new data
        FoodDatabaseHelper dbHelper = new FoodDatabaseHelper(getContext());
        foodListAdapter.setFoodItems(dbHelper.getAllFoodItems(FirebaseAuth.getInstance().getUid()));
        foodListAdapter.notifyDataSetChanged();
    }

    public void openActivity2(){
        Intent intent = new Intent(getActivity(), add_meal.class);
        startActivity(intent);
    }

    void loadFoodItems() {
        FoodDatabaseHelper dbHelper = new FoodDatabaseHelper(getActivity());

        List<FoodItem> foodItems = dbHelper.getAllFoodItems(FirebaseAuth.getInstance().getUid());
        foodListAdapter.setFoodItems(foodItems);
    }

    public int getTotalCaloriesConsumed() {
        int totalCalories = 0;

        FoodDatabaseHelper dbHelper = new FoodDatabaseHelper(getContext());
        List<FoodItem> foodItemList = dbHelper.getAllFoodItems(FirebaseAuth.getInstance().getUid());

        for (FoodItem foodItem : foodItemList) {
            totalCalories += foodItem.getCalories();
        }
        return totalCalories;
    }

}