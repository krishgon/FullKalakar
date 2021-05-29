package com.krishapps.kalakar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

public class ArtistCardAdapter extends RecyclerView.Adapter<ArtistCardAdapter.ViewHolder> {

    private Artist[] localDataSet;
    private TextView artistName_textView, artistUserName_textView;
    private Chip city_chip, skill_chip;
    private RatingBar artist_ratingBar;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
        }
    }

    public ArtistCardAdapter(Artist[] dataSet){
        this.localDataSet = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.artist_card, parent, false);

        artistName_textView = view.findViewById(R.id.artistName_textView);
        artistUserName_textView = view.findViewById(R.id.artistUserName_textView);
        city_chip = view.findViewById(R.id.city_chip);
        skill_chip = view.findViewById(R.id.skill_chip);
        artist_ratingBar = view.findViewById(R.id.artist_ratingBar);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistCardAdapter.ViewHolder holder, int position) {
        artistName_textView.setText(localDataSet[position].getName());
        artistUserName_textView.setText(localDataSet[position].getUserName());
        city_chip.setText(localDataSet[position].getCity());
        skill_chip.setText(localDataSet[position].getSkill());
        artist_ratingBar.setRating(localDataSet[position].getRating());
    }

    @Override
    public int getItemCount() {
        return localDataSet.length;
    }
}
