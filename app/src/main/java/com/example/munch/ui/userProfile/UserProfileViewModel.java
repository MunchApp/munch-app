package com.example.munch.ui.userProfile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.munch.data.model.Review;

import java.util.ArrayList;
import java.util.List;

public class UserProfileViewModel extends ViewModel {


    private MutableLiveData<Boolean> loggedIn;
    private MutableLiveData<String> fullName;
    private MutableLiveData<String> location;

    public UserProfileViewModel(){
    }
}