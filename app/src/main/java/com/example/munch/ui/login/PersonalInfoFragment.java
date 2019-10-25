package com.example.munch.ui.login;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.munch.R;
import com.example.munch.ui.map.MapViewModel;
import com.example.munch.ui.userProfile.UserProfileFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class PersonalInfoFragment extends Fragment{


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_personal_info, container, false);
        TextView pifirstAndLast = root.findViewById(R.id.pi_first_and_last_name);
        TextView dob = root.findViewById(R.id.pi_dob);
        TextView gender = root.findViewById(R.id.pi_gender);
        TextView phoneNum = root.findViewById(R.id.pi_phone_num);
        TextView email = root.findViewById(R.id.pi_email);

        SharedPreferences sp = getActivity().getSharedPreferences("key", 0);
        pifirstAndLast.setText(UserProfileFragment.currentUser.getFullName());
        dob.setText(UserProfileFragment.currentUser.getDateOfBirth());
        gender.setText(UserProfileFragment.currentUser.getGender());
        phoneNum.setText(UserProfileFragment.currentUser.getPhoneNum());
        email.setText(UserProfileFragment.currentUser.getFullName());

        return root;
    }

}