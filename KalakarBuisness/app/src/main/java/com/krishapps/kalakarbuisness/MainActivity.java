package com.krishapps.kalakarbuisness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.krishapps.kalakarbuisness.CustomClasses.Artist;
import com.krishapps.kalakarbuisness.CustomClasses.Service;
import com.krishapps.kalakarbuisness.MainFragments.AccountFragment;
import com.krishapps.kalakarbuisness.MainFragments.DmFragment;
import com.krishapps.kalakarbuisness.MainFragments.ProfileFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Fragment profFrag, accFrag, dmFrag;
    private FragmentManager fragmentManager;
    public static Artist artist;
    public static ListenerRegistration registration;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fireStore;
    StorageReference storageReference;
    DocumentReference documentReference;
    CollectionReference serviceCollRef;
    String artistID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // do initial rituals
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

        // hide the action bar
            getSupportActionBar().hide();

        // change the color of status bar
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.pink_light));

        // collect firebase pieces
            firebaseAuth = FirebaseAuth.getInstance();
            fireStore = FirebaseFirestore.getInstance();
            storageReference = FirebaseStorage.getInstance().getReference();
            artistID = firebaseAuth.getCurrentUser().getUid();


//            // collect the data of signed artist
//                 registration = documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
//                        if(error!=null){
//                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                        else{
//                            Log.d("krishlog", "onEvent: the data is: " + documentSnapshot.getData().toString());
//
//                            artist = new Artist(artistID, documentSnapshot.getString("fullName"), documentSnapshot.getString("phoneNumber"));
//                            artist.setUserName(documentSnapshot.getString("userName"));
//                            artist.setEmail(documentSnapshot.getString("email"));
//                            artist.setCity(documentSnapshot.getString("city"));
//                            artist.setSkill(documentSnapshot.getString("skill"));
//
//                            Log.d("krishlog", "onEvent: the name of the artist is " + artist.getName());
////                            Log.d("krishlog", "onEvent: the service probably is " + artist.getServices().toString());
//
//
//                        }
//                    }
//                });

        serviceCollRef = fireStore.collection("artists").document(artistID).collection("services");
        documentReference = serviceCollRef.getParent();

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot ds = task.getResult();
                    artist = new Artist(artistID, ds.getString("fullName"), ds.getString("phoneNumber"));
                    artist.setUserName(ds.getString("userName"));
                    artist.setEmail(ds.getString("email"));
                    artist.setCity(ds.getString("city"));
                    artist.setSkill(ds.getString("skill"));

                    setArtistServices(savedInstanceState);
                }else{
                    Log.d("krishlog", "onComplete: " + task.getException());
                }
            }
        });



        // collect the fragment manager
            fragmentManager = getSupportFragmentManager();

        // collect the fragments
            profFrag = new ProfileFragment();
            accFrag = new AccountFragment();
            dmFrag = new DmFragment();



        // set up bottom navigation view
            BottomNavigationView bottomNavigationView = findViewById(R.id.mainBottom_navigation);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.profile_page:
                            Log.d("krishlog", "onNavigationItemSelected: home clicked");
                            switchFragment(profFrag);
                            return true;

                        case R.id.accountPage:
                            Log.d("krishlog", "onNavigationItemSelected: account clicked");
                            switchFragment(accFrag);
                            return true;

                        case R.id.DmPage:
                            Log.d("krishlog", "onNavigationItemSelected: DM clicked");
                            switchFragment(dmFrag);
                            return true;

                        default:
                            return false;
                    }
                }
            });
    }

    public void setArtistServices(Bundle savedInstanceState){
        serviceCollRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<DocumentSnapshot> serviceDocs = task.getResult().getDocuments();
                    ArrayList<Service> services = new ArrayList<>();
                    for(int i=0; i < serviceDocs.size(); i++){
                        DocumentSnapshot ds = serviceDocs.get(i);
                        Log.d("krishlog", "onComplete: the service for is: " + ds.getString("serviceFor"));
                        Service s = new Service(ds.getString("serviceFor"), ds.getString("serviceRate"));
                        services.add(s);
                    }
                    artist.setServices(services);

                    // set the current fragment to home fragment
                    if(savedInstanceState == null){
                        fragmentManager.beginTransaction()
                                .add(R.id.main_fragmentContainerView, profFrag)
                                .setReorderingAllowed(true)
                                .commit();
                    }
                }else{
                    Log.d("krishlog", "onComplete: " + task.getException());
                }
            }
        });
    }

    public void switchFragment(Fragment fragment){
        fragmentManager.beginTransaction()
                .replace(R.id.main_fragmentContainerView, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }
}