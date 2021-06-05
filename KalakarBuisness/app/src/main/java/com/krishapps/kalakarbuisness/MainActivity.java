package com.krishapps.kalakarbuisness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.krishapps.kalakarbuisness.CustomClasses.Artist;
import com.krishapps.kalakarbuisness.MainFragments.AccountFragment;
import com.krishapps.kalakarbuisness.MainFragments.DmFragment;
import com.krishapps.kalakarbuisness.MainFragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    private Fragment profFrag, accFrag, dmFrag;
    private FragmentManager fragmentManager;
    public static Artist artist;

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

        // collect the artist object which was passed
            Intent intent = getIntent();
            artist = (Artist) intent.getSerializableExtra("artist");

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

        // set the current fragment to home fragment
            if(savedInstanceState == null){
                fragmentManager.beginTransaction()
                        .add(R.id.main_fragmentContainerView, profFrag)
                        .setReorderingAllowed(true)
                        .commit();
            }
    }

    public void switchFragment(Fragment fragment){
        fragmentManager.beginTransaction()
                .replace(R.id.main_fragmentContainerView, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }
}