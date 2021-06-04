package com.krishapps.kalakarbuisness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;

import com.krishapps.kalakarbuisness.AuthenticationFragments.LoginFragment;

public class ArtistDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_details);

        Log.d("krishlog", "onCreate: artist details activity");
        getSupportActionBar().hide();

        if (savedInstanceState == null) {

            Fragment fragment = new LoginFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.authentication_fragment_container_view, fragment)
                    .setReorderingAllowed(true)
//                    .addToBackStack(null)
                    .commit();
        }
    }
}