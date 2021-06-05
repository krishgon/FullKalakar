package com.krishapps.kalakarbuisness.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.krishapps.kalakarbuisness.CustomClasses.Service;
import com.krishapps.kalakarbuisness.R;

public class ServiceCardAdapter extends RecyclerView.Adapter<ServiceCardAdapter.ViewHolder> {

    Service[] localDataSet;
    TextView serviceFor_textView, serviceRate_textView;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
        }
    }

    public ServiceCardAdapter(Service[] dataSet){
        this.localDataSet = dataSet;
    }

    @NonNull
    @Override
    public ServiceCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_card, parent, false);

        serviceFor_textView = view.findViewById(R.id.serviceFor_textView);
        serviceRate_textView = view.findViewById(R.id.serviceRate_textView);

        return new ServiceCardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceCardAdapter.ViewHolder holder, int position) {
        serviceFor_textView.setText(localDataSet[position].getServiceFor());
        serviceRate_textView.setText(localDataSet[position].getServiceRate());

        Context context = holder.itemView.getContext();


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("krishlog", "onClick: the card of " + localDataSet[position].getServiceFor() + " is clicked");
            }
        });
    }

    @Override
    public int getItemCount() {
        return localDataSet.length;
    }
}
