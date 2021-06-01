package com.krishapps.kalakar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

public class AccountFragment  extends Fragment {
    public AccountFragment(){
        super(R.layout.account_fragment);
    }

    Button logOut_button;
    TextView user_name_textView, user_userName_textView, userMail_textView, userPhNum_textView;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    String userID;
    DocumentReference documentReference;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Log.d("krishlog", "onViewCreated: reached here in this fragment");

        // collect the ui elements
            logOut_button = view.findViewById(R.id.logOut_button_original);
            user_name_textView = view.findViewById(R.id.userName_textView);
            user_userName_textView = view.findViewById(R.id.userUserName_textView);
            userMail_textView = view.findViewById(R.id.userMail_textView);
            userPhNum_textView = view.findViewById(R.id.userPhNum_textView);

        // collect the data of signed user
            firebaseAuth = FirebaseAuth.getInstance();
            firestore = FirebaseFirestore.getInstance();

            userID = firebaseAuth.getCurrentUser().getUid();

            documentReference = firestore.collection("users").document(userID);
            ListenerRegistration registration = documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                    if(error!=null){
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        userPhNum_textView.setText(documentSnapshot.getString("phoneNumber"));
                        user_name_textView.setText(documentSnapshot.getString("fullName"));
                        user_userName_textView.setText(documentSnapshot.getString("userName"));
                        userMail_textView.setVisibility(View.INVISIBLE);
                    }
                }
            });



        // logout the user when clicked on log out button
            logOut_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialAlertDialogBuilder(getContext(), R.style.AlertDialogTheme)
                            .setTitle("Are you sure you want to log out?")
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    registration.remove();
                                    FirebaseAuth.getInstance().signOut();
                                    Intent intent = new Intent(getActivity(), Authentication.class);
                                    startActivity(intent);
                                }
                            }).show();
                }
            });

    }
}
