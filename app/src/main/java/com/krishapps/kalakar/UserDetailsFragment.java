package com.krishapps.kalakar;

import android.os.Bundle;
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // assign ui elements to their programatic variables
        TextInputLayout userName_textInputLayout = view.findViewById(R.id.userName_outlinedTextField);
        TextInputLayout name_textInputLayout = view.findViewById(R.id.name_outlinedTextField);
        EditText userName_editText = userName_textInputLayout.getEditText();
        EditText name_editText = name_textInputLayout.getEditText();
        Button logOut_button = view.findViewById(R.id.logOut_button);

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


    }
}
