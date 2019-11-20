package com.example.munch.ui.userProfile;



import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.munch.R;
import com.example.munch.data.model.LoggedInUser;
import com.example.munch.ui.foodTruck.createTruckActivity;
import com.example.munch.ui.login.LoginActivity;
import com.example.munch.ui.userProfile.manageTruck.ManageTruckFragment;
import com.example.munch.ui.userProfile.personalInfo.PersonalInfoFragment;

import java.util.ArrayList;

public class UserProfileFragment extends Fragment {
    public static LoggedInUser currentUser = new LoggedInUser();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_user_profile, container, false);
        final Button signInOut = root.findViewById(R.id.sign_in_out);
        TextView personalInfo = root.findViewById(R.id.personal_information);
        TextView manageTrucks = root.findViewById(R.id.manage_trucks);
        TextView listTruck = root.findViewById(R.id.list_truck);
        TextView firstAndLast = root.findViewById(R.id.first_and_last_name);
        ArrayList<TextView> clickables = new ArrayList<TextView>();
        clickables.add(listTruck);
        clickables.add(personalInfo);
        clickables.add(manageTrucks);

        firstAndLast.setText(currentUser.getFullName());
        if (currentUser.getLoggedIn()){
            signInOut.setText("SIGN OUT");
            listTruck.setOnClickListener(           //action triggered on button click
                    new View.OnClickListener() {
                        public void onClick(View view) {

                            Intent toListPage = new Intent(getActivity(), createTruckActivity.class);
                            startActivity(toListPage);

                        }
                    });


            personalInfo.setOnClickListener(           //action triggered on button click
                    new View.OnClickListener() {
                        public void onClick(View view) {
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            PersonalInfoFragment NAME = new PersonalInfoFragment();
                            fragmentTransaction.replace(R.id.nav_host_fragment, NAME);
                            fragmentTransaction.commit();
                        }
                    });

            manageTrucks.setOnClickListener(           //action triggered on button click
                    new View.OnClickListener() {
                        public void onClick(View view) {
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            ManageTruckFragment NAME = new ManageTruckFragment();
                            fragmentTransaction.replace(R.id.nav_host_fragment, NAME);
                            fragmentTransaction.commit();
                        }
                    });
        } else {
            signInOut.setText("SIGN IN");
        }

        signInOut.setOnClickListener(           //action triggered on button click
                new View.OnClickListener() {
                    public void onClick(View view) {
                        if (signInOut.getText().toString().equals("SIGN IN")) {
                            Intent toLoginPage = new Intent(getActivity(), LoginActivity.class);
                            startActivity(toLoginPage);

                        } else {
                            currentUser.signOut();
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            UserProfileFragment NAME = new UserProfileFragment();
                            fragmentTransaction.replace(R.id.nav_host_fragment, NAME);
                            fragmentTransaction.commit();
                        }
                    }
                });

        return root;
    }

}