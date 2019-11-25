package com.example.munch.ui.userProfile.personalInfo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.munch.Config;
import com.example.munch.R;
import com.example.munch.data.model.MunchUser;
import com.example.munch.ui.userProfile.CircleTransform;
import com.example.munch.ui.userProfile.UserProfileFragment;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class PersonalInfoFragment extends Fragment {
    private final int SELECT_PHOTO = 1;
    private Bitmap selectedImage;
    private ImageView proPic;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_personal_info, container, false);
        View popup = inflater.inflate(R.layout.popup_edit_field, container, false);
        LinearLayout popup_layout = (LinearLayout) popup.findViewById(R.id.popup_edit);

        //Get Text Views

        TextView firstAndLastT = root.findViewById(R.id.first_and_last_name);
        TextView dobT = root.findViewById(R.id.pi_dob);
        TextView phoneNumT = root.findViewById(R.id.pi_phone_num);
        TextView emailT = root.findViewById(R.id.pi_email);
        TextView cityStateT = root.findViewById(R.id.city_and_state);
        proPic = (ImageView) root.findViewById(R.id.profile_picture);
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

        Picasso.with(getContext()).load(MunchUser.getInstance().getPicture())
                .resize(100, 100)
                .transform(new CircleTransform())
                .into(proPic);


        //Set values
        firstAndLastT.setText(MunchUser.getInstance().getFullName());
        dobT.setText(MunchUser.getInstance().getDateOfBirth());
        if (MunchUser.getInstance().getPhoneNumber().equals("") || MunchUser.getInstance().getPhoneNumber() == null) {
            phoneNumT.setText("Tap the pencil to add a phone number");
        } else {
            phoneNumT.setText(MunchUser.getInstance().getPhoneNumber());
        }
        emailT.setText(MunchUser.getInstance().getEmail());


        //Pop-up on clicking on field
        showPopup(firstAndLast, firstAndLastT, "NAME", "firstName", "lastName");
        showPopup(dob, dobT, "DATE OF BIRTH", "dateOfBirth", null);
        showPopup(phoneNum, phoneNumT, "PHONE", "phoneNumber", null);
        showPopup(cityState, cityStateT, "CITY AND STATE", "city", "state");


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
                        int responseCode = 0;
                        responseCode = MunchUser.getInstance().updateUserInfo(vals);

                        if (responseCode == 200) {
                            if (prompt.equals("NAME")) {
                                edittedfield.setText(popupInput1.getText().toString() + " " + popupInput2.getText().toString());
                            }
                            if (prompt.equals("CITY AND STATE")) {
                                edittedfield.setText(popupInput1.getText().toString() + ", " + popupInput2.getText().toString());
                            } else {
                                edittedfield.setText(popupInput1.getText().toString());
                            }
                        }
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
                        //imageView.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (selectedImage != null)
                        Config.profileImage = encodeTobase64(selectedImage);
                    MunchUser.getInstance().uploadProfilePic();


                    Picasso.with(getContext()).load(MunchUser.getInstance().getPicture())
                            .resize(100, 100)
                            .transform(new CircleTransform())
                            .into(proPic);


                }
        }
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }
}
