package com.example.beckettfitnessTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.beckettfitness.FoodAdapter;
import com.example.beckettfitness.R;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class FoodSearchTest {
    public com.example.beckettfitness.add_meal add_meal;

    @Before
    public void setUp() {
        // Initialize the MainActivity and set up the RecyclerView
        Looper.prepare();
        add_meal = new com.example.beckettfitness.add_meal();

        // Wrap addObserver method call in runOnUiThread to run it on the main thread
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                add_meal.getLifecycle().addObserver(new LifecycleEventObserver() {
                    @Override
                    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                        // Handle lifecycle event
                    }
                });
            }
        });
        RecyclerView recyclerView = new RecyclerView(add_meal);
        add_meal.setContentView(recyclerView);
    }

    @Test
    public void testSearchForFood() throws InterruptedException {
        // Set up a mock response
        String json = "{\n" +
                "  \"branded\": [\n" +
                "    {\n" +
                "      \"food_name\": \"Test Food 1\",\n" +
                "      \"brand_name\": \"Test Brand 1\",\n" +
                "      \"nf_calories\": 100,\n" +
                "      \"serving_unit\": \"g\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"food_name\": \"Test Food 2\",\n" +
                "      \"brand_name\": \"Test Brand 2\",\n" +
                "      \"nf_calories\": 200,\n" +
                "      \"serving_unit\": \"g\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        // Set up the Volley request queue
        RequestQueue queue = Volley.newRequestQueue(add_meal);

        // Create a new request with a mock response
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "http://test.com", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Make sure that the RecyclerView is updated with the correct data
                        FoodAdapter adapter = (FoodAdapter) ((RecyclerView) add_meal.findViewById(R.id.foodList)).getAdapter();
                        assertNotNull(adapter);
                        assertEquals(2, adapter.getItemCount());
                        assertEquals("Test Food 1", adapter.foodList.get(0).getName());
                        assertEquals("Test Brand 1", adapter.foodList.get(0).getBrand());
                        assertEquals(100, adapter.foodList.get(0).getCalories());
                        assertEquals("g", adapter.foodList.get(0).getServingSize());
                        assertEquals("Test Food 2", adapter.foodList.get(1).getName());
                        assertEquals("Test Brand 2", adapter.foodList.get(1).getBrand());
                        assertEquals(200, adapter.foodList.get(1).getCalories());
                        assertEquals("g", adapter.foodList.get(1).getServingSize());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Fail the test if there is an error
                        fail(error.getMessage());
                    }
                }) {
            // Override the getHeaders() method to include the API headers
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("x-app-id", "your_app_id");
                headers.put("x-app-key", "your_app_key");
                return headers;
            }
        };    // Add the request to the Volley queue
        queue.add(request);

        // Wait for the request to finish before continuing with the test
        final CountDownLatch latch = new CountDownLatch(1);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                latch.countDown();
            }
        }, 5000);
        latch.await(10, TimeUnit.SECONDS);
    }
}
