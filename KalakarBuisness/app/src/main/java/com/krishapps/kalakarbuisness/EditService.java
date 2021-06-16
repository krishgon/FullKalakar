package com.krishapps.kalakarbuisness;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.krishapps.kalakarbuisness.CustomClasses.Service;
import com.krishapps.kalakarbuisness.adapters.ServiceMediaAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

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
    ArrayList<Uri> uris;

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1002){
            if(resultCode == Activity.RESULT_OK){
                Uri uri = data.getData();
                uris.add(0, uri);
                Log.d("krishlog", "onActivityResult: the list is:- " + uris.toString());
                updateRecyclerView();
            }
        }
    }

    public void updateRecyclerView(){
        Log.d("krishlog", "setupRecyclerView: the uri's are " + uris.toString());

        if(uris.size() == 1){
            adapter = new ServiceMediaAdapter(uris);
            editServMedia_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            editServMedia_recyclerView.setAdapter(adapter);
        }else{
            adapter.localDataSet = uris;
            adapter.notifyItemInserted(0);
        }
    }

    public void collectServiceMedia(){
        uris = new ArrayList<>();
        Log.d("krishlog", "collectServiceMedia: came here");
        storageReference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for(StorageReference item : listResult.getItems()){
                    item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            uris.add(0, uri);
                            Log.d("krishlog", "onSuccess: the uri is " + uri.toString());
                            updateRecyclerView();
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