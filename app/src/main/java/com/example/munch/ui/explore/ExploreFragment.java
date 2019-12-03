package com.example.munch.ui.explore;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.munch.R;
import com.example.munch.ui.map.MapFragment;

public class ExploreFragment extends Fragment {

    private ExploreViewModel exploreViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        exploreViewModel =
                ViewModelProviders.of(this).get(ExploreViewModel.class);
        View root = inflater.inflate(R.layout.fragment_explore, container, false);
        final EditText search = root.findViewById(R.id.search_explore_pg1);
        search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    search.setHint("");
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.addToBackStack("explore");
                    MapFragment map = new MapFragment();
                    ExploreFragment explore = new ExploreFragment();
                    fragmentTransaction.replace(((ViewGroup)getView().getParent()).getId(), map);
                    fragmentTransaction.commit();
                }
            }
        });
        return root;
    }
}