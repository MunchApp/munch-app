package com.example.munch.ui.userProfile.personalInfo;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.munch.MunchTools;
import com.example.munch.R;
import com.example.munch.ui.MyViewModelFactory;
import com.example.munch.ui.userProfile.CircleTransform;
import com.example.munch.ui.userProfile.UserProfileController;
import com.example.munch.ui.userProfile.UserProfileViewModel;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class PersonalInfoFragment extends Fragment {
    private UserProfileViewModel userProfileViewModel;
    private UserProfileController userProfileController;
    private final int SELECT_PHOTO = 1;
    private Bitmap selectedImage;
    private ImageView profilePicture;
    private TextView location;
    private TextView fullName;
    private TextView phoneNumber;
    private TextView dateOfBirth;
    private TextView email;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_personal_info, container, false);
        View popup = inflater.inflate(R.layout.popup_edit_field, container, false);
        LinearLayout popup_layout = (LinearLayout) popup.findViewById(R.id.popup_edit);
        userProfileViewModel =
                ViewModelProviders.of(this,new MyViewModelFactory(null,getActivity())).get(UserProfileViewModel.class);
        userProfileController = new UserProfileController(userProfileViewModel);
        userProfileController.updateViewModel();

        //Get Text Views

        fullName = root.findViewById(R.id.first_and_last_name);
        dateOfBirth = root.findViewById(R.id.pi_dob);
        phoneNumber = root.findViewById(R.id.pi_phone_num);
        email = root.findViewById(R.id.pi_email);
        location  = root.findViewById(R.id.city_and_state);
        profilePicture = (ImageView) root.findViewById(R.id.profile_picture);

        setObservers();

        ImageView firstAndLast = root.findViewById(R.id.edit_name);
        ImageView dob = root.findViewById(R.id.edit_dob);
        dob.setVisibility(View.INVISIBLE);
        ImageView phoneNum = root.findViewById(R.id.edit_phone);
        ImageView cityState = root.findViewById(R.id.edit_cityState);

        Button uploadPic = root.findViewById(R.id.upload_pic);
        uploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);

            }
        });



        //Pop-up on clicking on field
        showPopup(firstAndLast, fullName, "NAME", "firstName", "lastName");
        showPopup(dob, dateOfBirth, "DATE OF BIRTH", "dateOfBirth", null);
        showPopup(phoneNum, phoneNumber, "PHONE", "phoneNumber", null);
        showPopup(cityState, location, "CITY AND STATE", "city", "state");


        return root;
    }

    private void showPopup(final ImageView field, final TextView edittedfield, final String prompt, final String jsonField, final String jsonField2) {
        field.setClickable(true);
        field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Popup window set up
                final View popupView = getLayoutInflater().inflate(R.layout.popup_edit_field, null);
                final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setFocusable(true);
                popupWindow.setBackgroundDrawable(new ColorDrawable());
                int location[] = new int[2];
                view.getLocationOnScreen(location);


                //Styling Popup
                Button save = (Button) popupView.findViewById(R.id.save_info);
                Button exit = (Button) popupView.findViewById(R.id.exit);

                LinearLayout popupLayout = (LinearLayout) popupView.findViewById(R.id.popup_edit);
                TextView popupPrompt = (TextView) popupView.findViewById(R.id.edit_prompt);
                final EditText popupInput1 = (EditText) popupView.findViewById(R.id.input1);
                final EditText popupInput2 = (EditText) popupView.findViewById(R.id.input2);
                popupInput2.setVisibility(View.GONE);
                if (prompt.equals("NAME")) {
                    popupInput1.setHint("First Name");
                    popupInput2.setHint("Last Name");
                    popupInput2.setVisibility(View.VISIBLE);
                }
                if (prompt.equals("CITY AND STATE")) {
                    popupInput1.setHint("City");
                    popupInput2.setHint("State");
                    popupInput2.setVisibility(View.VISIBLE);
                }

                popupPrompt.setText("ENTER NEW " + prompt);
                popupPrompt.setTextColor(Color.WHITE);
                popupInput1.setTextColor(Color.WHITE);
                popupInput2.setTextColor(Color.WHITE);
                popupInput1.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                popupInput2.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                save.setTextColor(Color.WHITE);
                exit.setTextColor(Color.WHITE);
                save.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                exit.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                popupLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.munchGreenDark));
                popupWindow.setElevation(10);

                // Save and Exit buttons
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HashMap<String, String> vals = new HashMap<>();

                        String input = popupInput1.getText().toString();
                        vals.put(jsonField, input);
                        if (!popupInput2.getText().toString().equals("")) {
                            input = popupInput2.getText().toString();
                            vals.put(jsonField2, input);
                        }
                        userProfileController.updateValue(vals);
                        popupWindow.dismiss();

                    }
                });
                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });

                //Show popup
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == getActivity().RESULT_OK) {
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                        selectedImage = BitmapFactory.decodeStream(imageStream);
                        userProfileController.updateProfilePicture(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


                }
        }
    }

    private void setObservers() {
        final Observer<String> firstNameObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newFirstName) {
                String displayedName = newFirstName + " " + userProfileViewModel.getLastName().getValue();
                if (displayedName.length() > 14)
                    displayedName = displayedName.substring(0,14);
                fullName.setText(displayedName);
            }
        };

        final Observer<String> lastNameObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newLastName) {
                String displayedName = userProfileViewModel.getFirstName().getValue() + " " + newLastName;
                if (displayedName.length() > 14)
                    displayedName = displayedName.substring(0,14);
                fullName.setText(displayedName);
            }
        };

        final Observer<String> cityObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newCity) {
                String displayedName = newCity +", " + userProfileViewModel.getState();
                if (displayedName.length() > 14)
                    displayedName = displayedName.substring(0,14);
                location.setText(displayedName);
                if (newCity.equals("")){
                    location.setText("Austin, Texas");
                }
            }
        };

        final Observer<String> stateObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newState) {
                String displayedName = userProfileViewModel.getCity().getValue() + ", " + userProfileViewModel.getState().getValue();
                if (displayedName.length() > 14)
                    displayedName = displayedName.substring(0,14);
                location.setText(displayedName);
                if (newState.equals("")){
                    location.setText("Austin, Texas");
                }
            }
        };

        final Observer<String> pictureObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newPicture) {
                Picasso.with(getContext()).load(newPicture)
                        .resize(100, 100)
                        .transform(new CircleTransform())
                        .into(profilePicture);
            }
        };
        final Observer<String> emailObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newEmail) {
                email.setText(newEmail);
            }
        };
        final Observer<String> phoneObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newPhone) {
                phoneNumber.setText(newPhone);
            }
        };

        final Observer<String> dobObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newDOB) {
                dateOfBirth.setText(MunchTools.ISOtoReg(newDOB));
            }
        };
        userProfileViewModel.getPhoneNumber().observe(this,phoneObserver);
        userProfileViewModel.getEmail().observe(this,emailObserver);
        userProfileViewModel.getDateOfBirth().observe(this,dobObserver);
        userProfileViewModel.getFirstName().observe(this, firstNameObserver);
        userProfileViewModel.getLastName().observe(this, lastNameObserver);
        userProfileViewModel.getCity().observe(this,cityObserver);
        userProfileViewModel.getState().observe(this,stateObserver);
        userProfileViewModel.getPicture().observe(this,pictureObserver);
    }
}
