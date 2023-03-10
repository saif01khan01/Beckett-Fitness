package com.example.beckettfitness;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link breakfast_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class breakfast_frag extends Fragment {

    private Button breakfast_add;

    public breakfast_frag() {
        // Required empty public constructor
    }

    public static breakfast_frag newInstance() {
        return new breakfast_frag();
    }

    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_breakfast_frag, container, false);

        breakfast_add = (Button) view.findViewById(R.id.breakfast_add);

        breakfast_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity2();
            }
        });

        return view;
    }

    public void openActivity2(){
        Intent intent = new Intent(getActivity(), breakfast_add_Meal.class);
        startActivity(intent);
    }
}