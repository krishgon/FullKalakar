package com.krishapps.kalakar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("krishlog", "onCreate: main activitiy");
        getSupportActionBar().hide();

        Button logOut_button = findViewById(R.id.logOut_button);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //TODO: set the logic to open home page when there user has signed in and open authentication page when user has not signed in

        // code to logout the user when clicked on log out button
        logOut_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(MainActivity.this, Authentication.class);
                startActivity(intent);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.page_1:
                        Log.d("krishlog", "onNavigationItemSelected: home clicked");
                        return true;

                    case R.id.page_2:
                        Log.d("krishlog", "onNavigationItemSelected: account clicked");
                        return true;

                    case R.id.page_3:
                        Log.d("krishlog", "onNavigationItemSelected: DM clicked");
                        return true;

                    default:
                        return false;
                }
            }
        });


        bottomNavigationView.setSelectedItemId(R.id.page_1);


    }
}