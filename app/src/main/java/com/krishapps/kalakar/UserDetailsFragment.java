package com.krishapps.kalakar;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

public class UserDetailsFragment extends Fragment {
    public UserDetailsFragment() {
        super(R.layout.user_details_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // assign ui elements to their programatic variables
        TextInputLayout userName_textInputLayout = view.findViewById(R.id.userName_outlinedTextField);
        TextInputLayout name_textInputLayout = view.findViewById(R.id.name_outlinedTextField);
        EditText userName_editText = userName_textInputLayout.getEditText();
        EditText name_editText = name_textInputLayout.getEditText();


        //TODO: do something to not repeat the code below for disabling the animation of text field

        // disable the animation of label/hint of material design's text field
        userName_textInputLayout.setHint(null);
        userName_editText.setHint("username");
        name_textInputLayout.setHint(null);
        name_editText.setHint("full name");
    }
}
