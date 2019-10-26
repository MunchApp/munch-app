package com.example.munch.ui.userProfile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.munch.R;

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
        email.setText(UserProfileFragment.currentUser.getEmail());

        return root;
    }

}