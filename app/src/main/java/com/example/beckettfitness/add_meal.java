package com.example.beckettfitness;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class add_meal extends AppCompatActivity {

    RecyclerView recyclerView;
    SearchView searchView;

    FoodAdapter a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);

        recyclerView = findViewById(R.id.foodList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchView = findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit (String query){
                // This method is called when the user submits the search query
                // Perform the search operation here using the query string
                searchForFood(query);

                // Return true to indicate that the query has been handled
                return true;
            }

            /**
             * Called when the query text is changed by the user.
             *
             * @param newText the new content of the query text field.
             * @return false if the SearchView should perform the default action of showing any
             * suggestions if available, true if the action was handled by the listener.
             */
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void searchForFood(String query) {
        String url = "https://trackapi.nutritionix.com/v2/search/instant?query=" + query;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the API response here
                        try {
                            List<FoodItem> foodList = new ArrayList<>();

                            JSONArray jsonArray = response.getJSONArray("branded");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);

                                String name = hit.getString("food_name");
                                String brand = hit.getString("brand_name");
                                int calories = hit.getInt("nf_calories");
                                String servingSize = hit.getString("serving_unit");

                                FoodItem foodItem = new FoodItem(name, brand, calories, servingSize);
                                foodList.add(foodItem);
                            }

                            FoodAdapter adapter = new FoodAdapter(foodList);
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error parsing API response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle any errors here
                        Log.e("Lol", "Error searching for food: " + error.getMessage());
                        Toast.makeText(getApplicationContext(), "Error searching for food", Toast.LENGTH_SHORT).show();
                    }
                }) {
            // Override the getHeaders() method to include the API headers
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("x-app-id", "ed6e4ba9");
                headers.put("x-app-key", "a478fde62173ab8f947f877b2d6f6b22");
                return headers;
            }
        };

        // Add the API request to the Volley request queue
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }


}
