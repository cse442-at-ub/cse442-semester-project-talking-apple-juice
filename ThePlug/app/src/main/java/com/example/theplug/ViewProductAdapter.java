package com.example.theplug;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewProductAdapter extends RecyclerView.Adapter<ViewProductAdapter.ViewHolder> {

    private ArrayList prodList;


    public ViewProductAdapter(ArrayList soldList) {
        prodList = soldList;
    }

    @Override
    public ViewProductAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.soldrow, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewProductAdapter.ViewHolder holder, int position) {
        holder.insertData.setText(prodList.get(position).toString());

    }

    @Override
    public int getItemCount() {
        return prodList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView insertData;

        public ViewHolder(View itemView) {
            super(itemView);
            insertData = itemView.findViewById(R.id.soldView);
        }
    }
}
