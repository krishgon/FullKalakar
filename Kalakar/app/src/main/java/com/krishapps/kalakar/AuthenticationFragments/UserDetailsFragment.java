package com.krishapps.kalakar.AuthenticationFragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.krishapps.kalakar.MainActivity;
import com.krishapps.kalakar.R;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import id.zelory.compressor.Compressor;

public class UserDetailsFragment extends Fragment {
    public UserDetailsFragment() {
        super(R.layout.user_details_fragment);
    }

    String fullName, userName, userID, phoneNumber;
    TextInputLayout userName_textInputLayout;
    TextInputLayout name_textInputLayout;
    EditText userName_editText;
    EditText name_editText;
    ImageView userPP_chooseImageView;
    FirebaseFirestore firestore;
    StorageReference storageReference;
    File compressedImageFile;
    Uri uri;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // collect ui elements
            userName_textInputLayout = view.findViewById(R.id.userName_outlinedTextField);
            name_textInputLayout = view.findViewById(R.id.name_outlinedTextField);
            userName_editText = userName_textInputLayout.getEditText();
            name_editText = name_textInputLayout.getEditText();
            Button submit_button = view.findViewById(R.id.submit_button);
            userPP_chooseImageView = view.findViewById(R.id.userPP_chooseImageView);

        //TODO: do something to not repeat the code below for disabling the animation of text field

        // disable the animation of label/hint of material design's text field
            userName_textInputLayout.setHint(null);
            userName_editText.setHint("username");
            name_textInputLayout.setHint(null);
            name_editText.setHint("full name");

        // collect firebase elements
            userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            phoneNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
            firestore = FirebaseFirestore.getInstance();
            storageReference = FirebaseStorage.getInstance().getReference().child("users/" + userID + "/profile.jpg");

        // register user in firebase when clicked on submit button
            submit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(areUserDetailsValid()){
                        uploadDetailsToFirebase();
                    }
                }
            });

        // set text watchers on edit text for errors to be cleared dynamically
            name_editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.toString().isEmpty() == false){
                        name_textInputLayout.setError(null);
                    }else {
                        name_textInputLayout.setError("Required");
                    }
                }
            });
            userName_editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.toString().isEmpty() == false){
                        userName_textInputLayout.setError(null);
                    }else {
                        userName_textInputLayout.setError("Required");
                    }
                }
            });

        // let the user choose his pp when clicked on pp image view
            userPP_chooseImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, 1001);
                }
            });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1001){
            if (resultCode== Activity.RESULT_OK){
                uri = data.getData();
                Log.d("krishlog", "onActivityResult: the uri is: " + uri);
                userPP_chooseImageView.setImageURI(uri);
            }
        }
    }

    public boolean areUserDetailsValid(){
        String mName = name_editText.getText().toString();
        String mUserName = userName_editText.getText().toString();

        name_textInputLayout.setError(null);
        userName_textInputLayout.setError(null);

        if((mName.isEmpty()==false) && (mUserName.isEmpty()==false)){
            Log.d("krishlog", "areUserDetailsValid: everything is fine");
            return true;
        }

        if(mName.isEmpty()){
            name_textInputLayout.setError("Required");
            Log.d("krishlog", "areUserDetailsValid: name not typed");
        }

        if(mUserName.isEmpty()){
            userName_textInputLayout.setError("Required");
            Log.d("krishlog", "areUserDetailsValid: userName not typed");
        }
        return false;
    }

    public void goToMainActivity(){
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
    }

    public void uploadDetailsToFirebase(){
        fullName = name_editText.getText().toString();
        userName = userName_editText.getText().toString().trim();
        Log.d("krishlog", "onClick: the number probably is " + FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());

        DocumentReference documentReference = firestore.collection("users").document(userID);

        HashMap<String, Object> user = new HashMap<>();
        user.put("fullName", fullName);
        user.put("phoneNumber", phoneNumber);
        user.put("userName", userName);

        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("krishlog", "onSuccess: user profile is completed");
                uploadPpToFirebase();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("krishlog", "onFailure: " + e.getMessage());
            }
        });
    }

    public void uploadPpToFirebase(){
        if(userPP_chooseImageView.getDrawable() != getResources().getDrawable(R.drawable.ic_baseline_person_24, null)){
            // compress image and get the uri
                File ppFile = new File(getPath(uri));
                Log.d("krishlog", "uploadImageToFirebase: file is " + ppFile.toString());

                try {
                    compressedImageFile = new Compressor(getContext())
                            .setMaxWidth(250)
                            .setMaxHeight(250)
                            .setQuality(50)
                            .compressToFile(ppFile);
                    Log.d("krishlog", "uploadImageToFirebase: com is " + compressedImageFile.toString());
                } catch (IOException e) {
                    Log.d("krishlog", "uploadImageToFirebase: error is " + e.getMessage());
                }
                Uri compressedUri = Uri.fromFile(compressedImageFile);

            // put image in firebase storage
                storageReference.putFile(compressedUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("krishlog", "onSuccess: image uploaded");
                        goToMainActivity();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        }else{
            goToMainActivity();
        }
    }

    public String getPath(Uri uri){
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

}

// TODO: add a progress bar to inform the user that some background process in running
