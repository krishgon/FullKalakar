package com.krishapps.kalakar;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

public class HomeFragment extends Fragment {

    public HomeFragment(){
        super(R.layout.home_fragment);
    }

    TextInputLayout search_layout;
    EditText search_editText;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        search_layout = view.findViewById(R.id.search_outlinedTextField);
        search_editText = search_layout.getEditText();

        search_layout.setHint(null);
        search_editText.setHint(Html.fromHtml("<small>"+ getString(R.string.searchHint) + "</small>"));
    }
}
