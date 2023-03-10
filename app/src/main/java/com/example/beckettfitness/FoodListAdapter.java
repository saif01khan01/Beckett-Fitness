package com.example.beckettfitness;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.FoodViewHolder> {

    private List<FoodItem> foodItems;
    private Context context;

    public FoodListAdapter(List<FoodItem> foodItems, Context context) {
        this.foodItems = foodItems;
        this.context = context;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_item_layout, parent, false);
        return new FoodViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem foodItem = foodItems.get(position);
        holder.nameTextView.setText(foodItem.getName());
        holder.brandTextView.setText(foodItem.getBrand());
        holder.caloriesTextView.setText(String.valueOf(foodItem.getCalories()));
        holder.servingSizeTextView.setText(foodItem.getServingSize());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                FirebaseUser a = FirebaseAuth.getInstance().getCurrentUser();
                FoodDatabaseHelper dbHelper = new FoodDatabaseHelper(context);
                dbHelper.removeFoodItem(foodItem , FirebaseAuth.getInstance().getUid());
                foodItems.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, foodItems.size());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    public void setFoodItems(List<FoodItem> foodItems) {
        this.foodItems = foodItems;
        notifyDataSetChanged();
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView brandTextView;
        public TextView caloriesTextView;
        public TextView servingSizeTextView;

        public FoodViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.food_name);
            brandTextView = itemView.findViewById(R.id.food_brand);
            caloriesTextView = itemView.findViewById(R.id.food_calories);
            servingSizeTextView = itemView.findViewById(R.id.food_serving_size);
        }
    }
}