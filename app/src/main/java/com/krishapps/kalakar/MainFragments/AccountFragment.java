package com.krishapps.kalakar.MainFragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.krishapps.kalakar.Authentication;
import com.krishapps.kalakar.R;

public class AccountFragment  extends Fragment {
    public AccountFragment(){
        super(R.layout.account_fragment);
    }

    Button logOut_button, deleteAccount_button;
    TextView user_name_textView, user_userName_textView, userMail_textView, userPhNum_textView;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fireStore;
    String userID;
    DocumentReference documentReference;
    ImageView user_pp;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Log.d("krishlog", "onViewCreated: reached here in this fragment");

        // collect the ui elements
            deleteAccount_button = view.findViewById(R.id.deleteAccount_button);
            logOut_button = view.findViewById(R.id.logOut_button_original);
            user_name_textView = view.findViewById(R.id.userName_textView);
            user_userName_textView = view.findViewById(R.id.userUserName_textView);
            userMail_textView = view.findViewById(R.id.userMail_textView);
            userPhNum_textView = view.findViewById(R.id.userPhNum_textView);
            user_pp = view.findViewById(R.id.user_pp);

        // collect the data of signed user
            firebaseAuth = FirebaseAuth.getInstance();
            fireStore = FirebaseFirestore.getInstance();

            userID = firebaseAuth.getCurrentUser().getUid();

            documentReference = fireStore.collection("users").document(userID);
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

        // change profile image when clicked on the 'delete account button' ~ just for initial testing purpose
            user_pp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // open gallery
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, 1000);
                }
            });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1000){
            if (resultCode== Activity.RESULT_OK){
                Uri imageUri = data.getData();
                user_pp.setImageURI(imageUri);
            }
        }
    }
}

//TODO: Replace all the deprecated methods with the latest methods in every java file