package com.example.wagepay;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class WorkerRecyclerAdapter extends FirebaseRecyclerAdapter<WorkerRecyclerModel,WorkerRecyclerAdapter.myViewHolder>{

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     */
    public WorkerRecyclerAdapter(@NonNull FirebaseRecyclerOptions<WorkerRecyclerModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull WorkerRecyclerModel model) {
        holder.wName.setText(model.getwName());
        holder.wNumber.setText(model.getwNumber());

        Glide.with(holder.img.getContext())
                .load(model.getwImage())
                .placeholder(R.drawable.profile_pic)
                .circleCrop()
                .error(R.drawable.profile_icon)
                .into(holder.img);

        // Set an OnClickListener for the item view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the next activity and pass data
                Context context = holder.itemView.getContext();
                Intent intent = new Intent(context, WorkerDetailsActivity.class);
                intent.putExtra("workerImage", model.getwImage());
                intent.putExtra("workerName", model.getwName()); // Pass data to the next activity
                intent.putExtra("workerAddress", model.getwAddress());
                intent.putExtra("workerNumber", model.getwNumber());

                // Add more data if needed
                context.startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.worker_rec_card_design,parent,false);
        return new myViewHolder(view);

    }

    static class myViewHolder extends RecyclerView.ViewHolder{
        CircleImageView img;

        TextView wName, wNumber;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img = (CircleImageView)itemView.findViewById(R.id.worker_img);
            wName = itemView.findViewById(R.id.worker_name);
            wNumber = itemView.findViewById(R.id.worker_contact);
        }
    }
}