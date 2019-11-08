package com.example.munch.ui.favorites;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.fragment.app.Fragment;

import com.example.munch.R;
import com.example.munch.ui.foodTruck.FoodTruckFragment;

public class FavoritesFragment extends Fragment {

    private FavoritesViewModel favoritesViewModel;
    private ImageView imageClick;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        favoritesViewModel =
                ViewModelProviders.of(this).get(FavoritesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_list_truck, container, false);
        final TextView textView = root.findViewById(R.id.text_list_prompt);
        favoritesViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        //Make image clickable
        clickable(root);
        return root;
    }

    //Code to make the first image in favorites list be clickable TODO:Obviously implement so that its dynamic and also for every image
    private void clickable(View root){
        imageClick = (ImageView) root.findViewById(R.id.main_image);
        imageClick.bringToFront();
        imageClick.setClickable(true);
        imageClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FoodTruckFragment NAME = new FoodTruckFragment();
                fragmentTransaction.replace(R.id.nav_host_fragment, NAME);
                fragmentTransaction.commit();
            }
        });

    }
}