package com.example.munch.ui.userProfile;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class UserProfileViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public UserProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}