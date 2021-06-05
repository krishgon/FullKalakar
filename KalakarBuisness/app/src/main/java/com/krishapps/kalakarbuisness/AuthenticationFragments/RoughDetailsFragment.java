package com.krishapps.kalakarbuisness.AuthenticationFragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.krishapps.kalakarbuisness.CustomClasses.Artist;
import com.krishapps.kalakarbuisness.R;

import org.jetbrains.annotations.NotNull;

public class RoughDetailsFragment extends Fragment {
    public RoughDetailsFragment(){
        super(R.layout.rough_details_fragment);
    }

    EditText artistName_editText, artistEmail_editText;
    Button submit_button;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        // collect ui elements
            artistName_editText = view.findViewById(R.id.artistName_editText);
            artistEmail_editText = view.findViewById(R.id.artistEmail_editText);
            submit_button = view.findViewById(R.id.roughDetails_submitButton);

        // when submit clicked, go to payment screen
            submit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // collect variables for artist object
                        String name = artistName_editText.getText().toString();
                        String email = artistEmail_editText.getText().toString();
                        String artistID = FirebaseAuth.getInstance().getUid();
                        String artistPhoneNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

                    // prepare the artist object
                        Artist currentOne = new Artist(artistID, name, artistPhoneNumber);
                        if(email.isEmpty() == false){
                            currentOne.setEmail(email);
                        }

                    // prepare the bundle to pass
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("CURRENT_ARTIST", currentOne);

                    // put the bundle in the transaction
                        Fragment fragment = new PaymentFragment();
                        fragment.setArguments(bundle);

                    // switch fragment
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.authentication_fragment_container_view, fragment)
                                .commit();
                }
            });
    }
}
