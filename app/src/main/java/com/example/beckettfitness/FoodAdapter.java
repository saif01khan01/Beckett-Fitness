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

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private List<FoodItem> foodList;

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

        builder.setView(popupView)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int quantity = quantityInput.getValue();
                        int calories = initialCalories * quantity;
                        FoodItem selectedFood = new FoodItem(foodItem.getName(), foodItem.getBrand(), calories, foodItem.getServingSize());

                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}