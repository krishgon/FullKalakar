package com.krishapps.kalakar.MainFragments;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.krishapps.kalakar.customClasses.Artist;
import com.krishapps.kalakar.adapters.ArtistCardAdapter;
import com.krishapps.kalakar.R;
import com.krishapps.kalakar.customClasses.Service;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    public HomeFragment(){
        super(R.layout.home_fragment);
    }

    TextInputLayout search_layout;
    EditText search_editText;
    RecyclerView artists_recyclerView;
    FirebaseFirestore firestore;
    CollectionReference artistsCollectionReference;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // collect ui elements
            search_layout = view.findViewById(R.id.search_outlinedTextField);
            search_editText = search_layout.getEditText();
            artists_recyclerView = view.findViewById(R.id.artists_recyclerView);

        // disable search box hint animation and set hint programmatically
            search_layout.setHint(null);
            search_editText.setHint(Html.fromHtml("<small>"+ getString(R.string.searchHint) + "</small>"));

        // collect firebase elements
            firestore = FirebaseFirestore.getInstance();

        // initalise a debug database
            ArrayList<Artist> mArtists = new ArrayList<>();

//            Artist a1 = new Artist("Soumya Agrawal", "sumasu21", "Gondia", 4.0f, "Casio player");
//            Artist a2 = new Artist("Raghav Agrawal", "ragnag23", "Nagpur", 4.5f, "Guitarist");
//            Artist a3 = new Artist("Anushree", "anu3431", "Gondia", 4.3f, "Cook");
//
//            Service s1 = new Service("playing casio", "at 100 rupees per 30 mins");
//            Service s2 = new Service("playing guitar", "at 10 rupees per min");
//            Service s3 = new Service("being a cook", "at 500 rupees per meal");
//            Service s4 = new Service("painting on a wall", "at 1000 rupees per wall");
//
//            a1.setServices(new Service[]{s1, s2});
//            a2.setServices(new Service[]{s2});
//            a3.setServices(new Service[]{s3, s4});
//            mArtists = new Artist[]{a1, a2, a3};
            artistsCollectionReference = firestore.collection("artists");
            artistsCollectionReference.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();
                                for(int i=0; i < documentSnapshots.size(); i++){
                                    DocumentSnapshot documentSnapshot = documentSnapshots.get(i);
                                    Artist mArtist = new Artist(documentSnapshot.getString("fullName"), documentSnapshot.getString("userName"), documentSnapshot.getString("city"), 4.5f, documentSnapshot.getString("skill"), documentSnapshot.getId());
                                    Log.d("krishlog", "onComplete: the artist name is " + mArtist.getName());
                                    mArtists.add(mArtist);

                                    // setup the recycler view
                                        artists_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                        ArtistCardAdapter adapter = new ArtistCardAdapter(mArtists);
                                        artists_recyclerView.setAdapter(adapter);

                                }
                            }else{
                                Log.d("krishlog", "onComplete: daya gadbad ye hai:- " + task.getException());
                            }
                        }
                    });
    }
}
