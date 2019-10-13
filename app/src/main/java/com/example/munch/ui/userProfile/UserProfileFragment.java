package com.example.munch.ui.userProfile;



import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.munch.MainActivity;
import com.example.munch.R;
import com.example.munch.ui.login.LoginActivity;
import com.example.munch.ui.login.PersonalInfoFragment;
import com.example.munch.ui.map.MapFragment;
import com.example.munch.ui.map.MapViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class UserProfileFragment extends Fragment {

    private UserProfileViewModel userProfileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userProfileViewModel =
                ViewModelProviders.of(this).get(UserProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_user_profile, container, false);
        final View root2 = inflater.inflate(R.layout.fragment_personal_info, container, false);
        boolean pi = false;
        Button signInOut = root.findViewById(R.id.sign_in_out);
        TextView personalInfo = root.findViewById(R.id.personal_information);

        signInOut.setOnClickListener(           //action triggered on button click
                new View.OnClickListener() {
                    public void onClick(View view) {
                       Intent toLoginPage = new Intent(getActivity(), LoginActivity.class);
                       startActivity(toLoginPage);
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

        return root;
    }

}