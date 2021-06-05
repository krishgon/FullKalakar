package com.krishapps.kalakarbuisness.MainFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.krishapps.kalakarbuisness.Authentication;
import com.krishapps.kalakarbuisness.R;

import org.jetbrains.annotations.NotNull;
import static com.krishapps.kalakarbuisness.MainActivity.artist;
import static com.krishapps.kalakarbuisness.MainActivity.registration;

public class AccountFragment extends Fragment {
    public AccountFragment(){
        super(R.layout.account_fragment);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        Button logOut_button = view.findViewById(R.id.logOut_button);

        logOut_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registration.remove();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), Authentication.class);
                startActivity(intent);
            }
        });
    }
}
