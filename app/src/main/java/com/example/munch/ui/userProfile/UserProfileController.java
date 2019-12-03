package com.example.munch.ui.userProfile;
import android.graphics.Bitmap;

import com.example.munch.MunchTools;
import com.example.munch.data.model.MunchUser;

import java.util.HashMap;

public class UserProfileController {
    private UserProfileViewModel userProfileViewModel;

    public UserProfileController(UserProfileViewModel userProfileViewModel){
        this.userProfileViewModel = userProfileViewModel;
    }

    public void updateViewModel(){
        MunchUser currentUser = MunchUser.getInstance();
        userProfileViewModel.setLoggedIn(currentUser.getLoggedIn());
        if (userProfileViewModel.getLoggedIn().getValue()) {
            if (!(currentUser.getCity()==null))
                userProfileViewModel.setCity(currentUser.getCity());
            else
                userProfileViewModel.setCity("Austin");
            if (!(currentUser.getState()==null))
            userProfileViewModel.setState(currentUser.getState());
            else
                userProfileViewModel.setState("Texas");
            userProfileViewModel.setPhoneNumber(currentUser.getPhoneNumber());
            userProfileViewModel.setFirstName(currentUser.getFirstName());
            userProfileViewModel.setLastName(currentUser.getLastName());
            userProfileViewModel.setLoggedIn(currentUser.getLoggedIn());
            if (!(currentUser.getPicture() ==  null || currentUser.getPicture().equals("")))
                userProfileViewModel.setPicture(currentUser.getPicture());
            else
                userProfileViewModel.setPicture("https://www.warnersstellian.com/Content/images/product_image_not_available.png");
        }
    }

    public void updateValue(HashMap<String, String> vals){
        int responseCode = 0;
        responseCode = MunchUser.getInstance().updateUserInfo(vals);
        updateViewModel();
    }

    public void updateProfilePicture(Bitmap selectedImage){
        MunchUser currentUser = MunchUser.getInstance();
        userProfileViewModel.setLoggedIn(currentUser.getLoggedIn());
        if (selectedImage != null) {
            userProfileViewModel.setPictureBitmap(MunchTools.encodeTobase64(selectedImage));
            currentUser.uploadProfilePic(userProfileViewModel.getPictureBitmap().getValue());
            userProfileViewModel.setPicture(currentUser.getPicture());
        }
    }

}
