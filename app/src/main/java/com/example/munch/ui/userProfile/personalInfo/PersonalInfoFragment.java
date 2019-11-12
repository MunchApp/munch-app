package com.example.munch.ui.userProfile.personalInfo;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.munch.R;
import com.example.munch.ui.userProfile.UserProfileFragment;

public class PersonalInfoFragment extends Fragment{


    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_personal_info, container, false);
        View popup = inflater.inflate(R.layout.popup_edit_field, container, false);
        LinearLayout popup_layout = (LinearLayout)popup.findViewById(R.id.popup_edit);

        //Get Text Views
        TextView firstAndLast = root.findViewById(R.id.pi_first_and_last_name);
        TextView dob = root.findViewById(R.id.pi_dob);
        TextView gender = root.findViewById(R.id.pi_gender);
        TextView phoneNum = root.findViewById(R.id.pi_phone_num);
        TextView email = root.findViewById(R.id.pi_email);


        //Set values
        firstAndLast.setText(UserProfileFragment.currentUser.getFullName());
        dob.setText(UserProfileFragment.currentUser.getDateOfBirth());
        gender.setText(UserProfileFragment.currentUser.getGender());
        phoneNum.setText(UserProfileFragment.currentUser.getPhoneNum());
        email.setText(UserProfileFragment.currentUser.getEmail());


        //Pop-up on clicking on field
        showPopup(firstAndLast, "NAME");
        showPopup(dob, "DATE OF BIRTH");
        showPopup(gender, "GENDER");
        showPopup(phoneNum, "PHONE");
        showPopup(email, "EMAIL");


        return root;
    }

    private void showPopup (TextView editField, final String prompt){
        final TextView field = editField;
        final String newPrompt = prompt;
        field.setClickable(true);
        field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //Popup window set up
                final View popupView = getLayoutInflater().inflate(R.layout.popup_edit_field, null);
                final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setFocusable(true);
                popupWindow.setBackgroundDrawable(new ColorDrawable());
                int location[] = new int[2];
                view.getLocationOnScreen(location);


                //Styling Popup
                Button save = (Button)popupView.findViewById(R.id.save_info);
                Button exit = (Button)popupView.findViewById(R.id.exit);

                LinearLayout popupLayout = (LinearLayout) popupView.findViewById(R.id.popup_edit);
                TextView popupPrompt= (TextView) popupView.findViewById(R.id.edit_prompt);
                final EditText popupInput1 = (EditText) popupView.findViewById(R.id.input1);
                final EditText popupInput2 = (EditText) popupView.findViewById(R.id.input2);
                popupInput2.setVisibility(View.GONE);
                if (prompt.equals("NAME")){
                    popupInput1.setHint("First Name");
                    popupInput2.setHint("Last Name");
                    popupInput2.setVisibility(View.VISIBLE);
                }

                popupPrompt.setText("ENTER NEW " + newPrompt);
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
                    public void onClick(View view){
                        //todo add call to edit elements
                        int responseCode = 0;
                        if (responseCode == 200){
                            if (prompt.equals("NAME")){
                                field.setText(popupInput1.getText() + " " +popupInput2.getText());
                            } else {
                                field.setText(popupInput1.getText());
                            }
                        }

                    }
                });
                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        popupWindow.dismiss();
                    }
                });

                //Show popup
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
            }
        });
    }
}
