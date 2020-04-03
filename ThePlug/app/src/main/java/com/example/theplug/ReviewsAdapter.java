package com.example.theplug;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private ArrayList prodList;

    public ReviewsAdapter(ArrayList reviewList) {
                prodList=reviewList;
    }

    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviewrow, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.ViewHolder holder, int position) {
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
            insertData = itemView.findViewById(R.id.reviewView);
        }
    }
}
