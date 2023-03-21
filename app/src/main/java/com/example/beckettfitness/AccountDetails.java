package com.example.beckettfitness;

public class AccountDetails {
    private int age;
    private double height;
    private double weight;
    private String exerciseLevel;

    public AccountDetails(int age, double height, double weight, String exerciseLevel) {
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.exerciseLevel = exerciseLevel;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getExerciseLevel() {
        return exerciseLevel;
    }

    public void setExerciseLevel(String exerciseLevel) {
        this.exerciseLevel = exerciseLevel;
    }
}
