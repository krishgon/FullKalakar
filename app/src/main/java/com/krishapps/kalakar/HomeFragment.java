package com.krishapps.kalakar;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

public class HomeFragment extends Fragment {

    public HomeFragment(){
        super(R.layout.home_fragment);
    }

    TextInputLayout search_layout;
    EditText search_editText;
    RecyclerView artists_recyclerView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // collect ui elements
            search_layout = view.findViewById(R.id.search_outlinedTextField);
            search_editText = search_layout.getEditText();
            artists_recyclerView = view.findViewById(R.id.artists_recyclerView);

        // disable search box hint animation and set hint programmatically
            search_layout.setHint(null);
            search_editText.setHint(Html.fromHtml("<small>"+ getString(R.string.searchHint) + "</small>"));

        // initalise a debug database
            Artist[] mArtists;

            Artist a1 = new Artist("Soumya Agrawal", "sumasu21", "Gondia", 4.0f, "Casio player");
            Artist a2 = new Artist("Raghav Agrawal", "ragnag23", "Nagpur", 4.5f, "Guitarist");
            Artist a3 = new Artist("Anushree", "anu3431", "Gondia", 4.3f, "Cook");

            mArtists = new Artist[]{a1, a2, a3};

        // setup the recycler view
            artists_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            ArtistCardAdapter adapter = new ArtistCardAdapter(mArtists);
            artists_recyclerView.setAdapter(adapter);
    }
}
