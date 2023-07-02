package com.example.wagepay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class WorkerRecyclerAdapter extends RecyclerView.Adapter<WorkerRecyclerAdapter.ViewHolder> {


    HomeFragment context;
    ArrayList<WorkerRecyclerModel> arrDetails;
    ArrayList<WorkerRecyclerModel> filteredList;
    WorkerRecyclerAdapter(HomeFragment context, ArrayList<WorkerRecyclerModel> arrDetails){
        this.context = context;
        this.arrDetails = arrDetails;
    }

    public void setFilteredList(ArrayList<WorkerRecyclerModel> filteredList){
        this.arrDetails = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.getContext()).inflate(R.layout.worker_rec_card_design, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.img.setImageResource(arrDetails.get(position).img);
        holder.txtName.setText(arrDetails.get(position).name);
        holder.txtPhone.setText(arrDetails.get(position).phone);
    }

    @Override
    public int getItemCount() {
        return arrDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPhone;
        ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.worker_name);
            txtPhone = itemView.findViewById(R.id.worker_contact);
            img = itemView.findViewById(R.id.worker_img);
        }
    }
}
