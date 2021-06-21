package com.krishapps.kalakarbuisness;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.krishapps.kalakarbuisness.CustomClasses.Service;
import com.krishapps.kalakarbuisness.adapters.ServiceMediaAdapter;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import id.zelory.compressor.Compressor;

import static com.krishapps.kalakarbuisness.MainActivity.artist;
import static com.krishapps.kalakarbuisness.MainActivity.serviceRegistration;

public class EditService extends AppCompatActivity {

    EditText editServFor_editText, editServRate_editText;
    Button editServDone_button, editAddMedia_button;
    RecyclerView editServMedia_recyclerView;
    FirebaseFirestore firestore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    Service service;
    DocumentReference documentReference;
    ServiceMediaAdapter adapter;
    ArrayList<Uri> recyclerUris, uris;
    File compressedImageFile;
    int initialMediaSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_service);

        // hide the action bar
            getSupportActionBar().hide();

        // change the color of status bar
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.pink_light));

        // collect ui elements
            editServFor_editText = findViewById(R.id.editServFor_editText);
            editServRate_editText = findViewById(R.id.editServRate_editText);
            editServDone_button = findViewById(R.id.editServDone_button);
            editServMedia_recyclerView = findViewById(R.id.editServMedia_recyclerView);
            editAddMedia_button = findViewById(R.id.editAddMedia_button);

        // collect service
            service = (Service) getIntent().getSerializableExtra("service");

        // collect firebase elements
            firestore = FirebaseFirestore.getInstance();
            firebaseStorage = FirebaseStorage.getInstance();
            documentReference = firestore.collection("artists").document(artist.getArtistID());
            storageReference = firebaseStorage.getReference().child("artists/" + artist.getArtistID() + "/services/" + service.getServiceID() + "/");
            recyclerUris = new ArrayList<>();

        // update ui according to the service's data
            editServFor_editText.setText(service.getServiceFor());
            editServRate_editText.setText(service.getServiceRate());
            collectServiceMedia();

        // when clicked on add media, take user to gallery to choose the media
            editAddMedia_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 1002);
                }
            });

        // update the data in firebase according to the current data when clicked on done button
            editServDone_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateServiceDetailsOnFirebase();
                    switchToProfileFragment();
                }
            });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1002){
            if(resultCode == Activity.RESULT_OK){
                Uri uri = data.getData();
                recyclerUris.add(0, uri);
                adapter.addItem(uri);
            }
        }
    }

    public void updateServiceMediaOnFirebase(){
        for(Uri uri : recyclerUris){
            String currentUri = uri.toString();
            if(currentUri.contains("https")){
                break;
            }else{
                int index = uris.indexOf(uri);
                StorageReference mediaRef = storageReference.child("media" + index + ".jpg");

                // compress image and get the uri
                    File ppFile = new File(getPath(uri));
                    Log.d("krishlog", "uploadImageToFirebase: file is " + ppFile.toString());
                    try {
                        compressedImageFile = new Compressor(getApplicationContext())
                                .setMaxWidth(400)
                                .setMaxHeight(400)
                                .setQuality(50)
                                .compressToFile(ppFile);
                        Log.d("krishlog", "uploadImageToFirebase: com is " + compressedImageFile.toString());
                    } catch (IOException e) {
                        Log.d("krishlog", "uploadImageToFirebase: error is " + e.getMessage());
                    }
                    Uri compressedUri = Uri.fromFile(compressedImageFile);

                // put image in firebase
                    mediaRef.putFile(compressedUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d("krishlog", "onSuccess: image uploaded");
                            switchToProfileFragment();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            }
        }
    }

    public void switchToProfileFragment(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public String getPath(Uri uri){
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

    public void updateServiceDetailsOnFirebase(){
        // reference to the service in firestore
            DocumentReference servRef = documentReference.collection("services").document(service.getServiceID());

        // make the hash map of the data
            HashMap<String, Object> serviceData = new HashMap<>();
                serviceData.put("serviceFor", editServFor_editText.getText().toString());
                serviceData.put("serviceRate", editServRate_editText.getText().toString());

        // update firebase (upload the hash map)
            servRef.set(serviceData, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d("krishlog", "onSuccess: service details edited");
                    updateServiceMediaOnFirebase();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Log.d("krishlog", "onSuccess: kuch toh gadbad hai daya; " + e.getMessage());
                }
            });
    }

//    public void updateRecyclerView(Uri uri){
////        Log.d("krishlog", "setupRecyclerView: the uri's are " + uris.toString());
//
//        recyclerUris.add(0, uri);
//
//        if(recyclerUris.size() == 1){
//            adapter = new ServiceMediaAdapter(recyclerUris);
//            editServMedia_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//            editServMedia_recyclerView.setAdapter(adapter);
//        }else{
//            adapter.localDataSet = recyclerUris;
//            adapter.notifyItemInserted(0);
//        }
//    }

    public void setRecyclerView(Uri uri){
        recyclerUris.add(0, uri);

        if(recyclerUris.size() == initialMediaSize){
            adapter = new ServiceMediaAdapter(recyclerUris);
            editServMedia_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            editServMedia_recyclerView.setAdapter(adapter);
        }
    }

    public void collectServiceMedia(){
        uris = new ArrayList<>();
        Log.d("krishlog", "collectServiceMedia: came here");
        storageReference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                initialMediaSize = listResult.getItems().size();

                for(StorageReference item : listResult.getItems()){
                    item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            uris.add(uri);
                            Log.d("krishlog", "onSuccess: the uri is " + uri.toString());
                            setRecyclerView(uri);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Log.d("krishlog", "onFailure: the error is " + e.getMessage());
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.d("krishlog", "onFailure: the error is " + e.getMessage());
            }
        });
    }
}