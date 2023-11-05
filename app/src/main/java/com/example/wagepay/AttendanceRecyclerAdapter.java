package com.example.wagepay;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AttendanceRecyclerAdapter extends FirebaseRecyclerAdapter<AttendanceRecyclerModel, AttendanceRecyclerAdapter.ItemViewHolder> {
    String phoneNo;
    private Map<String, String> attendanceStatusMap;

    public AttendanceRecyclerAdapter(@NonNull FirebaseRecyclerOptions<AttendanceRecyclerModel> options, String phoneNo) {
        super(options);
        this.phoneNo = phoneNo;
        this.attendanceStatusMap = new HashMap<>();
    }

    public String getAttendanceStatus(String workerId) {
        // Retrieve the attendance status for a specific worker
        return attendanceStatusMap.get(workerId);
    }
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendence_card_design, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull AttendanceRecyclerModel model) {
        holder.txtName.setText(model.getwName());

        Glide.with(holder.img.getContext())
                .load(model.getwImage())
                .placeholder(R.drawable.profile_pic)
                .circleCrop()
                .error(R.drawable.profile_icon)
                .into(holder.img);
        // Get the worker's ID from the model based on the position
        String workerId = model.getWorkerId();

        // Set the checkbox state based on the attendanceStatusMap
        if (attendanceStatusMap.containsKey(workerId)) {
            String attendanceStatus = attendanceStatusMap.get(workerId);
            holder.checkbox.setChecked("present".equals(attendanceStatus));
        } else {
            holder.checkbox.setChecked(false); // Default to "absent"
        }

        // Handle changes in the checkbox state
        holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String attendanceStatus = isChecked ? "present" : "absent";
            attendanceStatusMap.put(workerId, attendanceStatus);
        });

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public CompoundButton checkbox;
        TextView txtName;
        CircleImageView img;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.worker_name);
            img = itemView.findViewById(R.id.worker_img);
            checkbox = itemView.findViewById(R.id.checkbox);
        }
    }

// Add a method to get the attendance status map
    public Map<String, String> getAttendanceStatusMap() {
        return attendanceStatusMap;
    }


    private String getCurrentDate() {
        // Get the current date and time
        Calendar calendar = Calendar.getInstance();

        // Define the date format you want (e.g., "yyyy-MM-dd HH:mm:ss")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        // Format the current date
        String formattedDate = dateFormat.format(calendar.getTime());

        return formattedDate;
    }

}
