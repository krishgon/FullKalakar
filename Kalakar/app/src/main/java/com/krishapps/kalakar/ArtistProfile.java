package com.krishapps.kalakar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;
import com.krishapps.kalakar.adapters.ArtistCardAdapter;
import com.krishapps.kalakar.adapters.ServiceCardAdapter;
import com.krishapps.kalakar.customClasses.Artist;
import com.krishapps.kalakar.customClasses.Service;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ArtistProfile extends AppCompatActivity {

    RecyclerView services_recyclerView;
    Artist artist;
    TextView artistProfileName_textView, artistProfileUserName_textView, servicesTitle_textView;
    Chip artistProfileCity_chip, artistProfileSkill_chip, artistProfileCustomersServed_chip;
    RatingBar artistProfile_ratingBar;
    FirebaseFirestore firestore;

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

        // collect firebase elements
            firestore = FirebaseFirestore.getInstance();

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
            artistProfileCustomersServed_chip.setText("Served 15 customers");
            servicesTitle_textView.setText(artist.getName() + "'s Services");


        // collect artist's services
            CollectionReference servicesRef = firestore.collection("artists").document(artist.getDocumentID()).collection("services");
            servicesRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        ArrayList<Service> artistsServices = new ArrayList<>();
                        List<DocumentSnapshot> documentSnapshotList = task.getResult().getDocuments();
                        for(int i=0; i < documentSnapshotList.size(); i++){
                            DocumentSnapshot documentSnapshot = documentSnapshotList.get(i);
                            Service mService = new Service(documentSnapshot.getString("serviceFor"), documentSnapshot.getString("serviceRate"));
                            artistsServices.add(mService);
                        }
                        artist.setServices(artistsServices);

                        // set up the recycler view
                            services_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            ServiceCardAdapter adapter = new ServiceCardAdapter(artist.getServices());
                            services_recyclerView.setAdapter(adapter);

                    }else{
                        Log.d("krishlog", "onComplete: daya the gadbad is " + task.getException());
                    }
                }
            });
    }
}

//TODO: set starting price dynamically with algorithm