package com.example.beckettfitness;

public class FoodItem {
    private String name;
    private int calories;
    private String brand;

    private String servingSize;

    public FoodItem(String name, String brand, int calories, String servingSize) {
        this.name = name;
        this.calories = calories;
        this.brand = brand;
        this.servingSize = servingSize;

    }

    public String getName() {
        return name;
    }

    public int getCalories() {
        return calories;
    }

    public String getBrand() {
        return brand;
    }


    public String getServingSize() {
        return servingSize;
    }

}