package com.example.munch.ui.favorites;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.fragment.app.Fragment;

import com.example.munch.AboutPageActivity;
import com.example.munch.R;
import com.example.munch.ui.FoodTruck.FoodTruck;

public class FavoritesFragment extends Fragment {

    private FavoritesViewModel favoritesViewModel;
    private ImageView imageClick;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        favoritesViewModel =
                ViewModelProviders.of(this).get(FavoritesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_favorites, container, false);
        final TextView textView = root.findViewById(R.id.text_favorites);
        favoritesViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        clickable(root);
        return root;
    }

    private void clickable(View root){
        imageClick = (ImageView) root.findViewById(R.id.imageView2);
        imageClick.bringToFront();
        imageClick.setClickable(true);
        imageClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FoodTruck.class);
                startActivity(intent);
            }
        });

    }
}