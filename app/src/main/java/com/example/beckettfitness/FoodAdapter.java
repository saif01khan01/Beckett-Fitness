package com.example.beckettfitness;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private List<FoodItem> foodList;


    // Create a variable to hold the total calories
    int[] totalCalories = {0};

    // Create a calendar object to store the previous date
    Calendar[] prevDate = {Calendar.getInstance()};

    public FoodAdapter(List<FoodItem> foodList) {
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item_layout, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem foodItem = foodList.get(position);
        holder.foodName.setText(foodItem.getName());
        holder.foodBrand.setText("Brand: " + foodItem.getBrand());
        holder.foodCalories.setText("Calories: " + String.valueOf(foodItem.getCalories()));
        holder.foodServing.setText("Serving Size: " + foodItem.getServingSize());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the popup here, passing in the FoodItem object
                showPopup(foodItem, v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView foodName;
        TextView foodBrand;
        TextView foodCalories;
        TextView foodServing;

        Button addFood;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.food_name);
            foodBrand = itemView.findViewById(R.id.food_brand);
            foodCalories = itemView.findViewById(R.id.food_calories);
            foodServing = itemView.findViewById(R.id.food_serving_size);

            }
    }




    private void showPopup(FoodItem foodItem, View context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context.getContext());
        LayoutInflater inflater = LayoutInflater.from(context.getContext());
        View popupView = inflater.inflate(R.layout.food_result_pop_up, null);

        TextView foodName = popupView.findViewById(R.id.food_name_popup);
        foodName.setText(foodItem.getName());

        NumberPicker quantityInput = popupView.findViewById(R.id.quantity_picker);
        quantityInput.setMinValue(1);
        quantityInput.setMaxValue(100);

        TextView caloriesValue = popupView.findViewById(R.id.food_calories_popup);
        int initialCalories = foodItem.getCalories();
        caloriesValue.setText("Calories: " + initialCalories);

        TextView brandName = popupView.findViewById(R.id.food_brand_popup);
        brandName.setText("Brand: " + foodItem.getBrand());

        TextView serving = popupView.findViewById(R.id.food_serving_size_popup);
        serving.setText("Serving Size: " + foodItem.getServingSize());

        quantityInput.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                int calories = initialCalories * newVal;
                caloriesValue.setText("Calories: " + calories);
            }
        });

        // Create a variable to hold the total calories
        int[] totalCalories = {0};

// Create a calendar object to store the previous date
        final Calendar[] prevDate = {Calendar.getInstance()};

        builder.setView(popupView)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int quantity = quantityInput.getValue();
                        int calories = initialCalories * quantity;
                        FoodItem selectedFood = new FoodItem(foodItem.getName(), foodItem.getBrand(), calories, foodItem.getServingSize());

                        // Add the selected food item to the database
                        FirebaseUser a = FirebaseAuth.getInstance().getCurrentUser();
                        FoodDatabaseHelper databaseHelper = new FoodDatabaseHelper(context.getContext());
                        databaseHelper.addFoodItem(selectedFood, FirebaseAuth.getInstance().getUid());

                        // Add the calories to the total calories variable
                        totalCalories[0] += calories;

                        System.out.println(totalCalories[0]);

                        // Check if the current date is different from the previous date
                        Calendar currDate = Calendar.getInstance();
                        if (prevDate[0].get(Calendar.DAY_OF_YEAR) != currDate.get(Calendar.DAY_OF_YEAR)) {
                            // Reset the total calories if it's a new day
                            totalCalories[0] = 0;
                        }
                        prevDate[0] = currDate;
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();


    }

    }