package com.krishapps.kalakar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("krishlog", "onCreate: main activitiy");

        //TODO: set the logic to open home page when there user has signed in and open authentication page when user has not signed in
//        getSupportActionBar().hide();
//
//        if (savedInstanceState == null) {
//
//            Fragment fragment = new LoginFragment();
//
//            Log.d("krishlog", "onCreate: reached here");
//
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction()
//                    .add(R.id.fragment_container_view, fragment)
//                    .setReorderingAllowed(true)
////                    .addToBackStack(null)
//                    .commit();
//        }
    }
}