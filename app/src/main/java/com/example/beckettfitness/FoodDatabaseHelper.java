package com.example.beckettfitness;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FoodDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "food_database";
    private static final int DATABASE_VERSION = 11;


    //Food table columns
    public static final String TABLE_FOOD = "food";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_BRAND = "brand";
    public static final String COLUMN_CALORIES = "calories";
    public static final String COLUMN_SERVING_SIZE = "serving_size";

    public static final String COLUMN_USER_ID = "user_id";


    //Calories table columns
    public static final String TABLE_CALORIES = "calories";
    public static final String COLUMN_CALORIES_ID = "_id";
    public static final String COLUMN_CALORIES_VALUE = "calories_value";


    // Account details columns
    public static final String TABLE_ACCOUNT = "food";
    static final String COLUMN_ACCOUNT_ID = "account_id";
    static final String COLUMN_AGE = "age";
    static final String COLUMN_HEIGHT = "height";
    static final String COLUMN_WEIGHT = "weight";
    static final String COLUMN_EXERCISE_LEVEL = "exercise_level";

    static  final String COLUMN_WEIGHT_GOALS = "weight_goals";

    static  final String COLUMN_GENDER = "weight_goals";







    private static final String CREATE_FOOD_TABLE =
            "CREATE TABLE " + TABLE_FOOD + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_ID + " TEXT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_BRAND + " TEXT, " +
                    COLUMN_CALORIES + " INTEGER, " +
                    COLUMN_SERVING_SIZE + " TEXT)";

    private static final String CREATE_CALORIES_TABLE =
            "CREATE TABLE " + TABLE_CALORIES + " (" +
                    COLUMN_CALORIES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CALORIES_VALUE + " INTEGER, " +
                    COLUMN_USER_ID + " TEXT)";

    private static final String CREATE_ACCOUNT_TABLE =
            "CREATE TABLE " + TABLE_ACCOUNT + " (" +
                    COLUMN_ACCOUNT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_ID + " TEXT, " +
                    COLUMN_AGE + " INTEGER, " +
                    COLUMN_GENDER + " REAL, " +
                    COLUMN_HEIGHT + " REAL, " +
                    COLUMN_WEIGHT + " REAL, " +
                    COLUMN_WEIGHT_GOALS + " REAL, " +
                    COLUMN_EXERCISE_LEVEL + " TEXT)";



    public FoodDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public List<FoodItem> getAllFoodItems(String userId) {
        List<FoodItem> foodItemList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_NAME, COLUMN_BRAND, COLUMN_CALORIES, COLUMN_SERVING_SIZE};
        String selection = COLUMN_USER_ID + "=?";
        String[] selectionArgs = {userId};
        Cursor cursor = db.query(TABLE_FOOD, columns, selection, selectionArgs, null, null, COLUMN_NAME);

        int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
        int brandIndex = cursor.getColumnIndex(COLUMN_BRAND);
        int caloriesIndex = cursor.getColumnIndex(COLUMN_CALORIES);
        int servingSizeIndex = cursor.getColumnIndex(COLUMN_SERVING_SIZE);

        while (cursor.moveToNext()) {
            String name = cursor.getString(nameIndex);
            String brand = cursor.getString(brandIndex);
            int calories = cursor.getInt(caloriesIndex);
            String servingSize = cursor.getString(servingSizeIndex);

            FoodItem foodItem = new FoodItem(name, brand, calories, servingSize);
            foodItemList.add(foodItem);
        }

        cursor.close();
        db.close();

        return foodItemList;
    }



    public int getMaintainWeightCalories(JSONObject jsonResponse) throws JSONException {
        JSONObject goals = jsonResponse.getJSONObject("data").getJSONObject("goals");
        return goals.getInt("maintain weight");
    }

    public int getMildWeightLossCalories(JSONObject jsonResponse) throws JSONException {
        JSONObject goals = jsonResponse.getJSONObject("data").getJSONObject("goals");
        JSONObject mildWeightLoss = goals.getJSONObject("Mild weight loss");
        return mildWeightLoss.getInt("calory");
    }

    public int getWeightLossCalories(JSONObject jsonResponse) throws JSONException {
        JSONObject goals = jsonResponse.getJSONObject("data").getJSONObject("goals");
        JSONObject weightLoss = goals.getJSONObject("Weight loss");
        return weightLoss.getInt("calory");
    }

    public int getExtremeWeightLossCalories(JSONObject jsonResponse) throws JSONException {
        JSONObject goals = jsonResponse.getJSONObject("data").getJSONObject("goals");
        JSONObject extremeWeightLoss = goals.getJSONObject("Extreme weight loss");
        return extremeWeightLoss.getInt("calory");
    }

    public int getMildWeightGainCalories(JSONObject jsonResponse) throws JSONException {
        JSONObject goals = jsonResponse.getJSONObject("data").getJSONObject("goals");
        JSONObject mildWeightGain = goals.getJSONObject("Mild weight gain");
        return mildWeightGain.getInt("calory");
    }

    public int getWeightGainCalories(JSONObject jsonResponse) throws JSONException {
        JSONObject goals = jsonResponse.getJSONObject("data").getJSONObject("goals");
        JSONObject weightGain = goals.getJSONObject("Weight gain");
        return weightGain.getInt("calory");
    }

    public int getExtremeWeightGainCalories(JSONObject jsonResponse) throws JSONException {
        JSONObject goals = jsonResponse.getJSONObject("data").getJSONObject("goals");
        JSONObject extremeWeightGain = goals.getJSONObject("Extreme weight gain");
        return extremeWeightGain.getInt("calory");
    }




    public void resetValues() {
        SQLiteDatabase db = getWritableDatabase();

        // Reset TABLE_CALORIES
        ContentValues caloriesValues = new ContentValues();
        caloriesValues.put(COLUMN_CALORIES_VALUE, 0);
        db.update(TABLE_CALORIES, caloriesValues, null, null);

        // Reset TABLE_FOOD
        db.delete(TABLE_FOOD, null, null);

        // Reset TABLE_ACCOUNT
        //db.delete(TABLE_ACCOUNT, null, null);

        db.close();
    }



    public void removeAccInfo(String user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_ACCOUNT + " WHERE " + COLUMN_USER_ID + " = ?";
        db.execSQL(query, new String[]{user_id});
        db.close();
    }

    public int getCaloriesTotal(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the columns to be retrieved
        String[] projection = {
                "SUM(" + COLUMN_CALORIES_VALUE + ")"
        };

        // Define the selection criteria
        String selection = COLUMN_USER_ID + "=?";
        String[] selectionArgs = { userId };

        // Execute the query
        Cursor cursor = db.query(
                TABLE_CALORIES,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        int caloriesTotal = 0;

        if (cursor.moveToFirst()) {
            caloriesTotal = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return caloriesTotal;
    }




    public void addFoodItem(FoodItem foodItem, String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_NAME, foodItem.getName());
        values.put(COLUMN_BRAND, foodItem.getBrand());
        values.put(COLUMN_CALORIES, foodItem.getCalories());
        values.put(COLUMN_SERVING_SIZE, foodItem.getServingSize());
        long foodItemId = db.insert(TABLE_FOOD, null, values);
        db.close();

        // Insert the calories value into the calories table
        SQLiteDatabase dbCalories = this.getWritableDatabase();
        ContentValues valuesCalories = new ContentValues();
        valuesCalories.put(COLUMN_CALORIES_ID, foodItemId);
        valuesCalories.put(COLUMN_CALORIES_VALUE, foodItem.getCalories());
        valuesCalories.put(COLUMN_USER_ID, userId);
        dbCalories.insert(TABLE_CALORIES, null, valuesCalories);
        dbCalories.close();
    }



    public void removeFoodItem(FoodItem foodItem, String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_NAME + "=? AND " + COLUMN_BRAND + "=? AND " + COLUMN_CALORIES + "=? AND " + COLUMN_SERVING_SIZE + "=? AND " + COLUMN_USER_ID + "=?";
        String[] selectionArgs = {foodItem.getName(), foodItem.getBrand(), String.valueOf(foodItem.getCalories()), foodItem.getServingSize(), userId};
        db.delete(TABLE_FOOD, selection, selectionArgs);
        db.close();
    }








    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FOOD_TABLE);
        db.execSQL(CREATE_ACCOUNT_TABLE);
        db.execSQL(CREATE_CALORIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CALORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
        onCreate(db);
    }
}
