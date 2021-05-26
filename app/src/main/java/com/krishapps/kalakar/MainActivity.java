package com.krishapps.kalakar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("krishlog", "onCreate: main activitiy");

        Button logOut_button = findViewById(R.id.logOut_button);

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
    }
}