package com.krishapps.kalakar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.gson.Gson;
import com.krishapps.kalakar.adapters.ArtistCardAdapter;
import com.krishapps.kalakar.adapters.ServiceCardAdapter;
import com.krishapps.kalakar.customClasses.Artist;

public class ArtistProfile extends AppCompatActivity {

    RecyclerView services_recyclerView;
    Artist artist;
    TextView artistProfileName_textView, artistProfileUserName_textView, servicesTitle_textView;
    Chip artistProfileCity_chip, artistProfileSkill_chip, artistProfileCustomersServed_chip;
    RatingBar artistProfile_ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_profile);

        Log.d("krishlog", "onCreate: Artist profile activity");

        // hide the action bar
            getSupportActionBar().hide();

        // change the color of status bar
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.pink_light));

        // collect ui elements
            artistProfileName_textView = findViewById(R.id.artistProfileName_textView);
            artistProfileUserName_textView = findViewById(R.id.artistProfileUserName_textView);
            artistProfileCity_chip = findViewById(R.id.artistProfileCity_chip);
            artistProfile_ratingBar = findViewById(R.id.artistProfile_ratingBar);
            artistProfileSkill_chip = findViewById(R.id.artistProfileSkill_chip);
            artistProfileCustomersServed_chip = findViewById(R.id.artistProfileCustomersServed_chip);
            servicesTitle_textView = findViewById(R.id.servicesTitle_textView);
            services_recyclerView = findViewById(R.id.artistServices_recyclerView);

        // collect artist details
            Gson gson = new Gson();
            artist = gson.fromJson(getIntent().getStringExtra("artist_json"), Artist.class);

        // set up artist's profile data
            artistProfileName_textView.setText(artist.getName());
            artistProfileUserName_textView.setText(artist.getUserName());
            artistProfileCity_chip.setText(artist.getCity());
            artistProfile_ratingBar.setRating(artist.getRating());
            artistProfileSkill_chip.setText(artist.getSkill());
            artistProfileCustomersServed_chip.setText("Served " + artist.getCustomerServed() + " customers");
            servicesTitle_textView.setText(artist.getName() + "'s Services");


        // set up the recycler view
            services_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            ServiceCardAdapter adapter = new ServiceCardAdapter(artist.getServices());
            services_recyclerView.setAdapter(adapter);
    }
}

//TODO: set starting price dynamically with algorithm