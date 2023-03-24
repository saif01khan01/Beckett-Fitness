package com.example.beckettfitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class MainScreen extends AppCompatActivity {

    //declaration of bottom nav
    BottomNavigationView bottomNav;

    //declaration of firebase auth
    FirebaseAuth mAuth;

    //declaration and initialization of fragments
    AddMealFragment addMealFragment = new AddMealFragment();
    AccountFragment accountFragment = new AccountFragment();


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null) {
            startActivity(new Intent(MainScreen.this, LoginActivity.class));
        }



    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        bottomNav = findViewById(R.id.BottomNav);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, addMealFragment).commit();

        //obtain instance of firebase auth
        mAuth = FirebaseAuth.getInstance();

        FoodDatabaseHelper foodDatabaseHelper = new FoodDatabaseHelper(getApplicationContext());
        foodDatabaseHelper.removeOldEntries(FirebaseAuth.getInstance().getUid());


        /**
         * This block switches between the fragments using the bottom nav
         */
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,addMealFragment).commit();
                        return true;
                    case R.id.account:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,accountFragment).commit();
                        return true;
                }

                return false;
            }
        });

        }




}
