package com.example.beckettfitness;

public class AccountDetails {
    private int age;
    private double height;
    private double weight;
    private String exerciseLevel;

    private String gender;

    private String weightGoal;

    public int caloriesGoal;

    String userID;

    public AccountDetails(int age, double height, double weight, String gender, String exerciseLevel, String weightGoal, String userID) {
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.exerciseLevel = exerciseLevel;
        this.weightGoal = weightGoal;
        this.userID = userID;
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

    public String getWeightGoal(){ return weightGoal; }

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

    public String getGender() {
        return gender;
    }
}
