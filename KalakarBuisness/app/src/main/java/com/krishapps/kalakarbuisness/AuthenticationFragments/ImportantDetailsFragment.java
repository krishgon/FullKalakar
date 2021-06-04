package com.krishapps.kalakarbuisness.AuthenticationFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.krishapps.kalakarbuisness.Artist;
import com.krishapps.kalakarbuisness.R;

import org.jetbrains.annotations.NotNull;

public class ImportantDetailsFragment extends Fragment {
    public ImportantDetailsFragment(){
        super(R.layout.important_details_fragment);
    }

    Artist currentOne;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        // collect ui elements
            EditText userName_editText = view.findViewById(R.id.userName_editText);
            EditText city_editText = view.findViewById(R.id.city_editText);
            EditText skill_editText = view.findViewById(R.id.skill_editText);
            Button done_button = view.findViewById(R.id.done_button);

        // collect current artist
            currentOne = (Artist) requireArguments().getSerializable("CURRENT_ARTIST");

        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "ja log check krle", Toast.LENGTH_SHORT).show();

                // prepare the artist object
                    currentOne.setUserName(userName_editText.getText().toString());
                    currentOne.setCity(city_editText.getText().toString());
                    currentOne.setSkill(skill_editText.getText().toString());

                // log the details
                    Log.d("krishlog", "onClick:" + currentOne.getName() + "(" + currentOne.getUserName() + ") is a " + currentOne.getSkill() + " and lives in " + currentOne.getCity() + ". His email is " + currentOne.getEmail() + ".");


            }
        });    
    }
}
