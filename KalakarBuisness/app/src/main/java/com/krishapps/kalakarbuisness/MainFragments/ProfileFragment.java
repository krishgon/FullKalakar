package com.krishapps.kalakarbuisness.MainFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

//    Artist artist;
    RecyclerView services_recyclerView;
    TextView artistProfileName_textView, artistProfileUserName_textView, servicesTitle_textView;
    Chip artistProfileCity_chip, artistProfileSkill_chip, artistProfileCustomersServed_chip;
    RatingBar artistProfile_ratingBar;

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

        // prepare artist (just for debugging purpose)
            artist.setRating(4.5f);
            artist.setCustomerServed(15);

            // put services into artist
                Service s1 = new Service("playing casio", "at 100 rupees per 30 mins");
                Service s2 = new Service("playing guitar", "at 10 rupees per min");
                Service s3 = new Service("being a cook", "at 500 rupees per meal");
                Service s4 = new Service("painting on a wall", "at 1000 rupees per wall");

                artist.setServices(new Service[]{s1,s2,s3});

        Log.d("krishlog", "onViewCreated: artist prepared");
        Boolean bool = artist==null;
        Log.d("krishlog", "onViewCreated: " + bool.toString());

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
            ServiceCardAdapter serviceCardAdapter = new ServiceCardAdapter(new Service[]{s1,s2,s3});
            services_recyclerView.setAdapter(serviceCardAdapter);
    }
}
