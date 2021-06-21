package com.krishapps.kalakarbuisness.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.protobuf.ApiOrBuilder;
import com.krishapps.kalakarbuisness.CustomClasses.Service;
import com.krishapps.kalakarbuisness.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ServiceMediaAdapter extends RecyclerView.Adapter<ServiceMediaAdapter.ViewHolder> {

    public ArrayList<Uri> localDataSet;
    ImageView serviceMedia_imageView;
    Button deleteButton;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
        }
    }

    public ServiceMediaAdapter(ArrayList<Uri> dataSet){
        this.localDataSet = new ArrayList<Uri>(dataSet);
    }

    @NonNull
    @Override
    public ServiceMediaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_media_card, parent, false);

        deleteButton = view.findViewById(R.id.deleteMedia_button);
        serviceMedia_imageView = view.findViewById(R.id.serviceMedia_imageView);

        return new ServiceMediaAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceMediaAdapter.ViewHolder holder, int position) {
        Picasso.get().load(localDataSet.get(position)).into(serviceMedia_imageView);
        Uri mUri = localDataSet.get(position);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(mUri);
            }
        });
    }

    public void deleteItem(Uri uri){
        int position = localDataSet.indexOf(uri);

        Log.d("krishlog", "deleteItem: delete button of " + position + " was clicked");

        localDataSet.remove(position);
        notifyItemRemoved(position);
        Log.d("krishlog", "onActivityResult: the dataset of adapter is " + localDataSet.toString());
    }

    public void addItem(Uri uri){
        localDataSet.add(0, uri);
        notifyItemInserted(0);
        Log.d("krishlog", "onActivityResult: the dataset of adapter is " + localDataSet.toString());
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}