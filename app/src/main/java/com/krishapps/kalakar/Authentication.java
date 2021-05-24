package com.krishapps.kalakar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;

public class Authentication extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        Log.d("krishlog", "onCreate: authentication activity");
        getSupportActionBar().hide();

        if (savedInstanceState == null) {

            Fragment fragment = new LoginFragment();

            Log.d("krishlog", "onCreate: reached here");

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.authentication_fragment_container_view, fragment)
                    .setReorderingAllowed(true)
//                    .addToBackStack(null)
                    .commit();
        }
    }
}