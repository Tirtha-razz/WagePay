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
    private OnItemClickListener onItemClickListener; // Define an interface for item click callbacks
    private int selectedItemPosition = RecyclerView.NO_POSITION;


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     */
    public WorkRecyclerAdapter(@NonNull FirebaseRecyclerOptions<WorkRecyclerModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull WorkRecyclerModel model) {
        holder.categoryName.setText(model.getCategoryName());

        // Check if the item is selected and apply the border
        if (position == selectedItemPosition) {
            // Apply the border or highlight to the selected item
            holder.itemView.setBackgroundResource(R.drawable.selected_item_background); // Change this to your desired background
        } else {
            // Reset the background for non-selected items
            holder.itemView.setBackgroundResource(0); // Remove the background
        }

        holder.itemView.setOnClickListener(v -> {
            int previousSelectedItem = selectedItemPosition;
            selectedItemPosition = holder.getAdapterPosition();
            notifyItemChanged(previousSelectedItem); // Reset the previous selection
            notifyItemChanged(selectedItemPosition); // Update the new selection

            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(getRef(holder.getAdapterPosition()).getKey(), model);
            }
        });
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_recycler_card_design,parent,false);
        return new myViewHolder(view);

    }

    static class myViewHolder extends RecyclerView.ViewHolder{
        TextView categoryName;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.work_name);
        }
    }

    // Define an interface to handle item clicks
    public interface OnItemClickListener {
        void onItemClick(String categoryId, WorkRecyclerModel model);
    }
    // Method to set the item click listener externally
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}