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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button breakfast_add;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public breakfast_frag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment breakfast_frag.
     */
    // TODO: Rename and change types and number of parameters
    public static breakfast_frag newInstance(String param1, String param2) {
        breakfast_frag fragment = new breakfast_frag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
        Intent intent = new Intent(breakfast_frag.this.getActivity(), breakfast_add_Meal.class);
        startActivity(intent);
    }
}