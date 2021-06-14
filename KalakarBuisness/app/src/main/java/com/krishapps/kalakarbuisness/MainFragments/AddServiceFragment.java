package com.krishapps.kalakarbuisness.MainFragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.krishapps.kalakarbuisness.CustomClasses.Service;
import com.krishapps.kalakarbuisness.R;
import com.krishapps.kalakarbuisness.adapters.ServiceMediaAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import static com.krishapps.kalakarbuisness.MainActivity.artist;

public class AddServiceFragment extends Fragment {
    public AddServiceFragment(){
        super(R.layout.add_service_fragment);
    }

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    EditText serviceFor_editText, serviceRate_editText;
    RecyclerView serviceMedia_recyclerView;
    Button done_button, addMedia_button;
    Uri uri;
    ServiceMediaAdapter serviceMediaAdapter;
    ArrayList<Uri> mediaUris;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        // collect UI elements
            serviceFor_editText = view.findViewById(R.id.serviceFor_editText);
            serviceRate_editText = view.findViewById(R.id.serviceRate_editText);
            done_button = view.findViewById(R.id.done_button_service);
            addMedia_button = view.findViewById(R.id.addMedia_button);
            serviceMedia_recyclerView = view.findViewById(R.id.serviceMedia_recyclerView);

        // collect firebase elements
            firebaseAuth = FirebaseAuth.getInstance();
            firestore = FirebaseFirestore.getInstance();

        // when clicked on add media, let the user choose the media
            addMedia_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, 1000);
                }
            });

        // setup media recyclerView
            mediaUris = new ArrayList<>();
            serviceMedia_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        // when clicked on done button, register the service for the artist
            done_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Service service = new Service(serviceFor_editText.getText().toString(), serviceRate_editText.getText().toString());

                    String artistID = firebaseAuth.getUid();

                    DocumentReference documentReference = firestore.collection("artists").document(artistID).collection("services").document();
                    service.setServiceID(documentReference.getId());

                    HashMap<String, Object> data = new HashMap<>();
                        data.put("serviceFor", service.getServiceFor());
                        data.put("serviceRate", service.getServiceRate());


                    // register the service locally as well
                        artist.addServiceToArtist(service);

                    documentReference.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            switchToProfilePage();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(getContext(), "Daya, kuch toh gadbad hai", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1000){
            if (resultCode== Activity.RESULT_OK){
                uri = data.getData();
                Log.d("krishlog", "onActivityResult: the uri is: " + uri);
                mediaUris.add(uri);
                Log.d("krishlog", "onActivityResult: the size of list is " + mediaUris.size());
                if(mediaUris.size() == 1){
                    serviceMediaAdapter = new ServiceMediaAdapter(mediaUris);
                    serviceMedia_recyclerView.setAdapter(serviceMediaAdapter);
                }else{
                    serviceMediaAdapter.localDataSet = mediaUris;
                    serviceMediaAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    public void switchToProfilePage(){
        Fragment fragment = new ProfileFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragmentContainerView, fragment)
                .setReorderingAllowed(true)
                .commit();
    }

}
