package com.example.theplug;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;

public class HomeScreenAdapter extends RecyclerView.Adapter<HomeScreenAdapter.ViewHolder> implements Filterable {

    private ArrayList itemList;
    private ArrayList itemListFull;

    HomeScreenAdapter(ArrayList prodList) {
        this.itemList = prodList;
        this.itemListFull = new ArrayList(itemList);
    }

    @Override
    public HomeScreenAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
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


    public Filter getFilter() {
        return searchFilter;
    }

    public Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList filteredList = new ArrayList();
            if(constraint.toString().isEmpty()){
                filteredList.addAll(itemListFull);
            }else{
                for(Object item : itemListFull){
                    if(item.toString().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredList.add(item);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
                itemList.clear();
                itemList.addAll((Collection) results.values);
                notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView insertData;

        public ViewHolder(View itemView) {

            super(itemView);
            insertData = itemView.findViewById(R.id.productSearch);
        }
    }
}
