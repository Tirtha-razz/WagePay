package com.example.wagepay;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AttendanceRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_SAVE_BUTTON = 1;

    private AttendanceFragment context;
    private ArrayList<AttendanceRecyclerModel> arrAttendance;
    private OnSaveClickListener onSaveClickListener;

    public interface OnSaveClickListener {
        void onSaveClick();
    }

    public AttendanceRecyclerAdapter(AttendanceFragment context, ArrayList<AttendanceRecyclerModel> arrAttendance) {
        this.context = context;
        this.arrAttendance = arrAttendance;
    }

    public void setOnSaveClickListener(OnSaveClickListener listener) {
        onSaveClickListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context.getContext());

        if (viewType == VIEW_TYPE_ITEM) {
            View view = inflater.inflate(R.layout.attendence_card_design, parent, false);
            return new ViewHolder(view);
        } else if (viewType == VIEW_TYPE_SAVE_BUTTON) {
            View view = inflater.inflate(R.layout.save_button_layout, parent, false);
            return new SaveButtonViewHolder(view);
        }

        throw new IllegalArgumentException("Invalid view type");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder itemHolder = (ViewHolder) holder;
            AttendanceRecyclerModel item = arrAttendance.get(position);
            itemHolder.img.setImageResource(item.img);
            itemHolder.txtName.setText(item.name);
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
        return arrAttendance.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        // Determine which view type to use based on the position
        if (position == arrAttendance.size()) {
            return VIEW_TYPE_SAVE_BUTTON;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.worker_name);
            img = itemView.findViewById(R.id.worker_img);
        }
    }

    public class SaveButtonViewHolder extends RecyclerView.ViewHolder {
        Button btnSave;

        public SaveButtonViewHolder(@NonNull View itemView) {
            super(itemView);
            btnSave = itemView.findViewById(R.id.btnSave);
        }
    }
}
