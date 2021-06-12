package com.krishapps.kalakar.MainFragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.krishapps.kalakar.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.security.AlgorithmConstraints;
import java.util.HashMap;

import id.zelory.compressor.Compressor;

public class EditProfileFragment extends Fragment {
    public EditProfileFragment(){
        super(R.layout.edit_profile_fragment);
    }

    ImageView closeButton_imageView, okButton_imageView, editPP_imageView;
    Button changePP_button;
    TextInputEditText editName_textInputEditText;
    Uri uri;
    String name;
    ProgressBar progressBar;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fireStore;
    DocumentReference documentReference;
    String userID;
    File compressedImageFile;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        // collect ui elements
            editName_textInputEditText = view.findViewById(R.id.editName_textInputEditText);
            closeButton_imageView = view.findViewById(R.id.closeButton_imageView);
            okButton_imageView = view.findViewById(R.id.okButton_imageView);
            editPP_imageView = view.findViewById(R.id.editPP_imageView);
            changePP_button = view.findViewById(R.id.changePP_button);
            progressBar = view.findViewById(R.id.progressBar);

        //  collect user's data
            uri = Uri.parse(requireArguments().getString("user's pp"));
            name = requireArguments().getString("user's name");

        // update the ui according to the user data
            Picasso.get().load(uri).into(editPP_imageView);
            editName_textInputEditText.setText(name);

        // collect firebase elements
            firebaseAuth = FirebaseAuth.getInstance();
            storageReference = FirebaseStorage.getInstance().getReference().child("users/" + firebaseAuth.getCurrentUser().getUid() + "/profile.jpg");;
            fireStore = FirebaseFirestore.getInstance();
            userID = firebaseAuth.getCurrentUser().getUid();
            Log.d("krishlog", "onViewCreated: user id created");
            documentReference = fireStore.collection("users").document(userID);

        // let the user choose profile pic when clicked on change button
            changePP_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // open gallery
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, 1000);
                }
            });

        // when right button clicked
            okButton_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        updateFirebase();
                        uploadImageToFirebase(uri);
                    }
            });

        // when cross clicked
            closeButton_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchToAccountFrag();
                }
            });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1000){
            if (resultCode== Activity.RESULT_OK){
                uri = data.getData();
                Log.d("krishlog", "onActivityResult: the uri is: " + uri);
                editPP_imageView.setImageURI(uri);
            }
        }
    }

    public void uploadImageToFirebase(Uri imageUri) {
        // compress image and get the uri
            File ppFile = new File(getPath(imageUri));
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
                    switchToAccountFrag();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
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

    private void updateFirebase(){
        // collect new user data
            String name = editName_textInputEditText.getText().toString();

        // make the hash map of the data
            HashMap<String, Object> user = new HashMap<>();
                user.put("fullName", name);

        // update firebase (upload the hash map in firebase)
            documentReference.set(user, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d("krishlog", "onSuccess: profile name edited");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Log.d("krishlog", "onFailure: " + e.getMessage());
                }
            });
    }

    public void switchToAccountFrag(){
        // make the fragment view of original size
            BottomNavigationView navigationView = getActivity().findViewById(R.id.bottom_navigation);
            navigationView.setVisibility(View.VISIBLE);

        // switch to account fragment
            Fragment fragment = new AccountFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment_container_view, fragment)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
    }

}
