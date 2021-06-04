package com.krishapps.kalakarbuisness.AuthenticationFragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.krishapps.kalakarbuisness.Artist;
import com.krishapps.kalakarbuisness.R;

import org.jetbrains.annotations.NotNull;

public class PaymentFragment extends Fragment {
    public PaymentFragment(){
        super(R.layout.payment_fragment);
    }

    Artist currentOne;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        // collect the artist object
            currentOne = (Artist) requireArguments().getSerializable("CURRENT_ARTIST");

        // collect ui elements
            TextView hi_textView = view.findViewById(R.id.hi_textView);
            Button pay_button = view.findViewById(R.id.pay_button);

        // set the message
            hi_textView.setText("Hi " + currentOne.getName());

        // when pay button clicked take the user to important details section (in commercial version, this button should go to whatever payment gateway)
            pay_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // prepare the bundle to pass
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("CURRENT_ARTIST", currentOne);

                    // put the bundle in the transaction
                        Fragment fragment = new ImportantDetailsFragment();
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
