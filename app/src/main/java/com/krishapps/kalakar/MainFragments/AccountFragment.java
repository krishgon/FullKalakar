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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.krishapps.kalakar.Authentication;
import com.krishapps.kalakar.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class AccountFragment  extends Fragment {
    public AccountFragment(){
        super(R.layout.account_fragment);
    }

    Button logOut_button, deleteAccount_button, editProfile_button;
    TextView user_name_textView, user_userName_textView, userMail_textView, userPhNum_textView;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fireStore;
    String userID;
    DocumentReference documentReference;
    ImageView user_pp;
    StorageReference storageReference, fileReference;
    Uri currentPP_uri;


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
            editProfile_button = view.findViewById(R.id.editProfile_button);

        // collect firebase pieces
            firebaseAuth = FirebaseAuth.getInstance();
            fireStore = FirebaseFirestore.getInstance();
            storageReference = FirebaseStorage.getInstance().getReference();
            userID = firebaseAuth.getCurrentUser().getUid();
            documentReference = fireStore.collection("users").document(userID);

        // collect the data of signed user
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

        // load the profile picture automatically
            StorageReference profileRef = storageReference.child("users/" + firebaseAuth.getCurrentUser().getUid() + "/profile.jpg");
            profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    currentPP_uri = uri;
                    Picasso.get().load(uri).into(user_pp);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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

        // send user to edit page when clicked on edit profile
            editProfile_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToEditPage();
                }
            });
    }

    private void goToEditPage(){
        // prepare the user data to pass
            Bundle userData = new Bundle();
                userData.putString("user's name", user_name_textView.getText().toString());
                userData.putString("user's pp", currentPP_uri.toString());
                userData.putString("user's user name", user_userName_textView.getText().toString());
                userData.putString("user's phone number", userPhNum_textView.getText().toString());

        // make the fragment view full screen
            BottomNavigationView navigationView = getActivity().findViewById(R.id.bottom_navigation);
            navigationView.setVisibility(View.GONE);

        // switch to edit profile fragment
            Fragment fragment = new EditProfileFragment();
            fragment.setArguments(userData);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment_container_view, fragment)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
    }

}

//TODO: Replace all the deprecated methods with the latest methods in every java file
//TODO: Show a dialog to user, asking to crop the selected photo when choosing the profile pic