package com.example.munch.ui.explore;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ExploreViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ExploreViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Favorites");
    }

    public LiveData<String> getText() {
        return mText;
    }
}