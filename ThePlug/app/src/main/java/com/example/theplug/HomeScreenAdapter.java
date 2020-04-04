package com.example.theplug;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class HomeScreenAdapter extends RecyclerView.Adapter<HomeScreenAdapter.ViewHolder> {

    private ArrayList itemList;

    public HomeScreenAdapter(ArrayList prodList) {
        itemList = prodList;
    }

    @NonNull
    @Override
    public HomeScreenAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.productrow, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeScreenAdapter.ViewHolder holder, int position) {
        holder.insertData.setText(itemList.get(position).toString());

    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView insertData;

        public ViewHolder(View itemView) {

            super(itemView);
            insertData = itemView.findViewById(R.id.productSearch);
        }
    }
}
