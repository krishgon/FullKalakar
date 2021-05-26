package com.krishapps.kalakar;

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

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class UserDetailsFragment extends Fragment {
    public UserDetailsFragment() {
        super(R.layout.user_details_fragment);
    }

    String fullName, userName;
    TextInputLayout userName_textInputLayout;
    TextInputLayout name_textInputLayout;
    EditText userName_editText;
    EditText name_editText;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // assign ui elements to their programatic variables
        userName_textInputLayout = view.findViewById(R.id.userName_outlinedTextField);
        name_textInputLayout = view.findViewById(R.id.name_outlinedTextField);
        userName_editText = userName_textInputLayout.getEditText();
        name_editText = name_textInputLayout.getEditText();
        Button logOut_button = view.findViewById(R.id.logOut_button);
        Button submit_button = view.findViewById(R.id.submit_button);

        //TODO: do something to not repeat the code below for disabling the animation of text field

        // disable the animation of label/hint of material design's text field
        userName_textInputLayout.setHint(null);
        userName_editText.setHint("username");
        name_textInputLayout.setHint(null);
        name_editText.setHint("full name");

        // code to logout the user when clicked on log out button
        logOut_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                // send user back to login screen
                Fragment fragment = new LoginFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.authentication_fragment_container_view, fragment)
                        .addToBackStack(null) // this line will not exist in the published app
                        .commit();
            }
        });

        // register user in firebase when clicked on submit button
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(areUserDetailsValid()){
                    Log.d("krishlog", "onClick: entered in the process");
                    fullName = name_editText.getText().toString();
                    userName = userName_editText.getText().toString();
                }
            }
        });

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
