package com.krishapps.kalakar.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.gson.Gson;
import com.krishapps.kalakar.ArtistProfile;
import com.krishapps.kalakar.Authentication;
import com.krishapps.kalakar.MainActivity;
import com.krishapps.kalakar.customClasses.Artist;
import com.krishapps.kalakar.R;

import static androidx.core.content.ContextCompat.startActivity;

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

        Context context = holder.itemView.getContext();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("krishlog", "onClick: the card of " + localDataSet[position].getName() + " is clicked");
                Intent intent = new Intent(context, ArtistProfile.class);
                Gson gson = new Gson();
                String artistJson = gson.toJson(localDataSet[position]);
                intent.putExtra("artist_json", artistJson);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return localDataSet.length;
    }
}
