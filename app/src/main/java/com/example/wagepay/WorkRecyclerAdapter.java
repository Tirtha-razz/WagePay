package com.example.wagepay;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class WorkRecyclerAdapter extends FirebaseRecyclerAdapter<WorkRecyclerModel,WorkRecyclerAdapter.myViewHolder>{

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public WorkRecyclerAdapter(@NonNull FirebaseRecyclerOptions<WorkRecyclerModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull WorkRecyclerModel model) {
        holder.categoryName.setText(model.getCategoryName());

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_recycler_card_design,parent,false);
        return new myViewHolder(view);

    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView categoryName;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = (TextView)itemView.findViewById(R.id.work_name);
        }
    }
}