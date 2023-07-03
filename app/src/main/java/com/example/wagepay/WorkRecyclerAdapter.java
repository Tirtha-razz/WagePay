package com.example.wagepay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WorkRecyclerAdapter extends RecyclerView.Adapter<WorkRecyclerAdapter.ViewHolder> {

    ArrayList<WorkRecyclerModel> arrWork;
    Context context;
    public WorkRecyclerAdapter(Context context, ArrayList<WorkRecyclerModel> arrWork) {
      this.context = context;
      this.arrWork = arrWork;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.work_recycler_card_design,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.work.setText(arrWork.get(position).work);
    }

    @Override
    public int getItemCount() {
        return arrWork.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView work;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            work = itemView.findViewById(R.id.work_name);
        }
    }
}
