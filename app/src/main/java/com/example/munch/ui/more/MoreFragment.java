package com.example.munch.ui.more;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.munch.AboutPageActivity;
import com.example.munch.R;

public class MoreFragment extends Fragment {

    private MoreViewModel moreViewModel;
    Button mSubmitButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       moreViewModel =
                ViewModelProviders.of(this).get(MoreViewModel.class);
        View root = inflater.inflate(R.layout.fragment_more, container, false);

//        Intent intent = new Intent(getActivity(), AboutPageActivity.class);
//        startActivity(intent);

        mSubmitButton = root.findViewById(R.id.button2);
            mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutPageActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }
}