package com.example.beckettfitness;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class FoodDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "food_database";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_FOOD = "food";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_BRAND = "brand";
    public static final String COLUMN_CALORIES = "calories";
    public static final String COLUMN_SERVING_SIZE = "serving_size";

    public static final String COLUMN_USER_ID = "user_id";


    private static final String CREATE_FOOD_TABLE =
            "CREATE TABLE " + TABLE_FOOD + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_ID + " TEXT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_BRAND + " TEXT, " +
                    COLUMN_CALORIES + " INTEGER, " +
                    COLUMN_SERVING_SIZE + " TEXT)";


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



    public void addFoodItem(FoodItem foodItem, String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_NAME, foodItem.getName());
        values.put(COLUMN_BRAND, foodItem.getBrand());
        values.put(COLUMN_CALORIES, foodItem.getCalories());
        values.put(COLUMN_SERVING_SIZE, foodItem.getServingSize());
        db.insert(TABLE_FOOD, null, values);
        db.close();
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD);
        onCreate(db);
    }
}
