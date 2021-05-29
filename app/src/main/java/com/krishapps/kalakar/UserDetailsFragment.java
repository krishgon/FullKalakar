package com.krishapps.kalakar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class UserDetailsFragment extends Fragment {
    public UserDetailsFragment() {
        super(R.layout.user_details_fragment);
    }

    String fullName, userName, userID, phoneNumber;
    TextInputLayout userName_textInputLayout;
    TextInputLayout name_textInputLayout;
    EditText userName_editText;
    EditText name_editText;
    FirebaseFirestore firestore;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // assign ui elements to their programatic variables
        userName_textInputLayout = view.findViewById(R.id.userName_outlinedTextField);
        name_textInputLayout = view.findViewById(R.id.name_outlinedTextField);
        userName_editText = userName_textInputLayout.getEditText();
        name_editText = name_textInputLayout.getEditText();
        Button submit_button = view.findViewById(R.id.submit_button);

        //TODO: do something to not repeat the code below for disabling the animation of text field

        // disable the animation of label/hint of material design's text field
        userName_textInputLayout.setHint(null);
        userName_editText.setHint("username");
        name_textInputLayout.setHint(null);
        name_editText.setHint("full name");

        // get the current user's id and phone number
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        phoneNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

        firestore = FirebaseFirestore.getInstance();

        // register user in firebase when clicked on submit button
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(areUserDetailsValid()){
                    fullName = name_editText.getText().toString();
                    userName = userName_editText.getText().toString().trim();
                    Log.d("krishlog", "onClick: the number probably is " + FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());

                    DocumentReference documentReference = firestore.collection("users").document(userID);

                    HashMap<String, Object> user = new HashMap<>();
                    user.put("fullName", fullName);
                    user.put("phoneNumber", phoneNumber);
                    user.put("userName", userName);

                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("krishlog", "onSuccess: user profile is completed");
                            startActivity(new Intent(getActivity(), MainActivity.class));
                            getActivity().finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("krishlog", "onFailure: " + e.getMessage());
                        }
                    });
                }
            }
        });

        // set text watchers on edit text for errors to be cleared dynamically
        name_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty() == false){
                    name_textInputLayout.setError(null);
                }else {
                    name_textInputLayout.setError("Required");
                }
            }
        });
        userName_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty() == false){
                    userName_textInputLayout.setError(null);
                }else {
                    userName_textInputLayout.setError("Required");
                }
            }
        });


    }

    public boolean areUserDetailsValid(){
        String mName = name_editText.getText().toString();
        String mUserName = userName_editText.getText().toString();

        name_textInputLayout.setError(null);
        userName_textInputLayout.setError(null);

        if((mName.isEmpty()==false) && (mUserName.isEmpty()==false)){
            Log.d("krishlog", "areUserDetailsValid: everything is fine");
            return true;
        }

        if(mName.isEmpty()){
            name_textInputLayout.setError("Required");
            Log.d("krishlog", "areUserDetailsValid: name not typed");
        }

        if(mUserName.isEmpty()){
            userName_textInputLayout.setError("Required");
            Log.d("krishlog", "areUserDetailsValid: userName not typed");
        }
        return false;
    }
}

// TODO: add a progress bar to inform the user that some background process in running
