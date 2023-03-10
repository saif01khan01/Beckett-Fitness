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

import com.example.beckettfitness.FoodDatabaseHelper;
import com.example.beckettfitness.FoodItem;
import com.example.beckettfitness.FoodListAdapter;
import com.example.beckettfitness.R;
import com.example.beckettfitness.breakfast_add_Meal;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link breakfast_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class breakfast_frag extends Fragment {

    private Button breakfast_add;
    private RecyclerView recyclerView;
    private FoodListAdapter foodListAdapter;

    public breakfast_frag() {
        // Required empty public constructor
    }

    public static breakfast_frag newInstance() {
        return new breakfast_frag();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_breakfast_frag, container, false);

        breakfast_add = (Button) view.findViewById(R.id.breakfast_add);
        recyclerView = (RecyclerView) view.findViewById(R.id.breakfast_recycler_view);

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

    public void openActivity2(){
        Intent intent = new Intent(getActivity(), breakfast_add_Meal.class);
        startActivity(intent);
    }

    private void loadFoodItems() {
        FoodDatabaseHelper dbHelper = new FoodDatabaseHelper(getActivity());

        List<FoodItem> foodItems = dbHelper.getAllFoodItems(FirebaseAuth.getInstance().getUid());
        foodListAdapter.setFoodItems(foodItems);
    }
}