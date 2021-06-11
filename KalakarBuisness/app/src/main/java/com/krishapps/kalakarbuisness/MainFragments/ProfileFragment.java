package com.krishapps.kalakarbuisness.MainFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.krishapps.kalakarbuisness.CustomClasses.Artist;
import com.krishapps.kalakarbuisness.CustomClasses.Service;
import com.krishapps.kalakarbuisness.MainActivity;
import com.krishapps.kalakarbuisness.R;
import com.krishapps.kalakarbuisness.adapters.ServiceCardAdapter;

import org.jetbrains.annotations.NotNull;

import static com.krishapps.kalakarbuisness.MainActivity.artist;

public class ProfileFragment extends Fragment {
    public ProfileFragment(){
        super(R.layout.profile_fragment);
    }

    RecyclerView services_recyclerView;
    TextView artistProfileName_textView, artistProfileUserName_textView, servicesTitle_textView;
    Chip artistProfileCity_chip, artistProfileSkill_chip, artistProfileCustomersServed_chip;
    RatingBar artistProfile_ratingBar;
    Button addService_button;
    ServiceCardAdapter serviceCardAdapter;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        Log.d("krishlog", "onCreate: Artist profile fragment");

        // collect ui elements
            artistProfileName_textView = view.findViewById(R.id.artistProfileName_textView);
            artistProfileUserName_textView = view.findViewById(R.id.artistProfileUserName_textView);
            artistProfileCity_chip = view.findViewById(R.id.artistProfileCity_chip);
            artistProfile_ratingBar = view.findViewById(R.id.artistProfile_ratingBar);
            artistProfileSkill_chip = view.findViewById(R.id.artistProfileSkill_chip);
            artistProfileCustomersServed_chip = view.findViewById(R.id.artistProfileCustomersServed_chip);
            servicesTitle_textView = view.findViewById(R.id.servicesTitle_textView);
            services_recyclerView = view.findViewById(R.id.artistServices_recyclerView);
            addService_button = view.findViewById(R.id.addService_button);



        // bind data into ui elements
            artistProfileName_textView.setText(artist.getName());
            artistProfileUserName_textView.setText(artist.getUserName());
            artistProfileCity_chip.setText(artist.getCity());
            artistProfile_ratingBar.setRating(4.0f);
            artistProfileSkill_chip.setText(artist.getSkill());
            artistProfileCustomersServed_chip.setText("Served 15 customers");
            servicesTitle_textView.setText(artist.getName() + "'s Services");

        // setup the service recycler view
            services_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            if(artist.getServices() != null){
                serviceCardAdapter = new ServiceCardAdapter(artist.getServices());
            }
            services_recyclerView.setAdapter(serviceCardAdapter);

        // when clicked on add service, go to add service screen
            addService_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("krishlog", "onClick: you clicked on the button");

                    Fragment fragment = new AddServiceFragment();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_fragmentContainerView, fragment)
                            .setReorderingAllowed(true)
                            .addToBackStack(null)
                            .commit();
                }
            });

    }
}
