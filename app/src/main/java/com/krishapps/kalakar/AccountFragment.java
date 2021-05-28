package com.krishapps.kalakar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class AccountFragment  extends Fragment {
    public AccountFragment(){
        super(R.layout.account_fragment);
    }

    private Button logOut_button;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        logOut_button = view.findViewById(R.id.logOut_button_original);

        // code to logout the user when clicked on log out button
        logOut_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getActivity(), Authentication.class);
                startActivity(intent);
            }
        });

    }
}
