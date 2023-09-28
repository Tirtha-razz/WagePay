package com.example.wagepay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WorkRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_SAVE_BUTTON = 1;

    private ArrayList<WorkRecyclerModel> arrWork;
    private Context context;
    private OnSaveClickListener onSaveClickListener;

    public interface OnSaveClickListener {
        void onSaveClick();
    }

    public WorkRecyclerAdapter(Context context, ArrayList<WorkRecyclerModel> arrWork) {
        this.context = context;
        this.arrWork = arrWork;
    }

    public void setOnSaveClickListener(OnSaveClickListener listener) {
        onSaveClickListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == VIEW_TYPE_ITEM) {
            View view = inflater.inflate(R.layout.work_recycler_card_design, parent, false);
            return new ViewHolder(view);
        } else if (viewType == VIEW_TYPE_SAVE_BUTTON) {
            View view = inflater.inflate(R.layout.button_layout, parent, false);
            return new SaveButtonViewHolder(view);
        }

        throw new IllegalArgumentException("Invalid view type");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder itemHolder = (ViewHolder) holder;
            WorkRecyclerModel item = arrWork.get(position);
            itemHolder.work.setText(item.work);
        } else if (holder instanceof SaveButtonViewHolder) {
            SaveButtonViewHolder saveButtonHolder = (SaveButtonViewHolder) holder;
            saveButtonHolder.btnSave.setOnClickListener(v -> {
                if (onSaveClickListener != null) {
                    onSaveClickListener.onSaveClick();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        // Add 1 to the item count for the "Save" button item
        return arrWork.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        // Determine which view type to use based on the position
        if (position == arrWork.size()) {
            return VIEW_TYPE_SAVE_BUTTON;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView work;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            work = itemView.findViewById(R.id.work_name);
        }
    }

    public class SaveButtonViewHolder extends RecyclerView.ViewHolder {
        Button btnSave;

        public SaveButtonViewHolder(@NonNull View itemView) {
            super(itemView);
            btnSave = itemView.findViewById(R.id.buttonAdd);
        }
    }
}
