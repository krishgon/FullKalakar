package com.krishapps.kalakarbuisness.MainFragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.krishapps.kalakarbuisness.CustomClasses.Service;
import com.krishapps.kalakarbuisness.R;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import static com.krishapps.kalakarbuisness.MainActivity.artist;

public class AddServiceFragment extends Fragment {
    public AddServiceFragment(){
        super(R.layout.add_service_fragment);
    }

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    EditText serviceFor_editText, serviceRate_editText;
    Button done_button;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        // collect UI elements
            serviceFor_editText = view.findViewById(R.id.serviceFor_editText);
            serviceRate_editText = view.findViewById(R.id.serviceRate_editText);
            done_button = view.findViewById(R.id.done_button_service);

        // collect firebase elements
            firebaseAuth = FirebaseAuth.getInstance();
            firestore = FirebaseFirestore.getInstance();

        // when clicked on done button, register the service for the artist
            done_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Service service = new Service(serviceFor_editText.getText().toString(), serviceRate_editText.getText().toString());

                    String artistID = firebaseAuth.getUid();

                    DocumentReference documentReference = firestore.collection("artists").document(artistID).collection("services").document();
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

    public void switchToProfilePage(){
        Fragment fragment = new ProfileFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragmentContainerView, fragment)
                .setReorderingAllowed(true)
                .commit();
    }

}
