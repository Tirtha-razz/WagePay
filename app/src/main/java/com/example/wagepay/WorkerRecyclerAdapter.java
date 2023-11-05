package com.example.wagepay;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class WorkerRecyclerAdapter extends FirebaseRecyclerAdapter<WorkerRecyclerModel,WorkerRecyclerAdapter.myViewHolder>{

    private String phoneNo; // Add a member variable to store the phone number
    private String selectedCategoryId; // Add a member variable to store the selected category ID
    public WorkerRecyclerAdapter(FirebaseRecyclerOptions<WorkerRecyclerModel> options, String phoneNo, String selectedCategoryId) {
        super(options);
        this.phoneNo = phoneNo; // Store the phone number passed to the constructor
        this.selectedCategoryId = selectedCategoryId;
    }

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     */
    public WorkerRecyclerAdapter(@NonNull FirebaseRecyclerOptions<WorkerRecyclerModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, final int position, @NonNull WorkerRecyclerModel model) {
        holder.wName.setText(model.getwName());
        holder.wNumber.setText(model.getwNumber());

        Glide.with(holder.img.getContext())
                .load(model.getwImage())
                .placeholder(R.drawable.profile_pic)
                .circleCrop()
                .error(R.drawable.profile_icon)
                .into(holder.img);


        // this is for editing worker details
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup))
                        .setExpanded(true, 1230)
                        .create();

                dialogPlus.show();

                View view = dialogPlus.getHolderView();

                EditText name = view.findViewById(R.id.new_name);
                EditText address = view.findViewById(R.id.new_address);
                EditText number = view.findViewById(R.id.new_number);
                EditText wageRate = view.findViewById(R.id.new_wageRate);

                Button btnUpdate = view.findViewById(R.id.btnUpdate);

                name.setText((model.getwName()));
                address.setText((model.getwAddress()));
                number.setText((model.getwNumber()));
                wageRate.setText((model.getwWageRate()));

                dialogPlus.show();

                btnUpdate.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Map<String,Object> map = new HashMap<>();
                        map.put("wName",name.getText().toString());
                        map.put("wAddress",address.getText().toString());
                        map.put("wNumber",number.getText().toString());
                        map.put("wWageRate",wageRate.getText().toString());

                        FirebaseDatabase.getInstance().getReference("Users").child(phoneNo).child("Workers")
                                .child(Objects.requireNonNull(getRef(position).getKey())).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.wName.getContext(), "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(holder.wName.getContext(), "Error While Updating Data.", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });

            }
        });

        // this is for deleting worker

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.wName.getContext());
                builder.setTitle("Delete Permanently !");
                builder.setMessage("Deleted data can't be recovered.");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference("Users").child(phoneNo).child("Workers")
                                .child(Objects.requireNonNull(getRef(position).getKey())).removeValue();

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(holder.wName.getContext(), "Data Deletion Cancelled.", Toast.LENGTH_SHORT).show();

                    }
                });
                builder.show();
            }
        });

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
                intent.putExtra("workerId",model.getWorkerId());
                intent.putExtra("wageRate",model.getwWageRate());


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

        Button btnEdit, btnDelete;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img = (CircleImageView)itemView.findViewById(R.id.worker_img);
            wName = itemView.findViewById(R.id.worker_name);
            wNumber = itemView.findViewById(R.id.worker_contact);

            btnEdit = (Button)itemView.findViewById(R.id.btnEdit);
            btnDelete = (Button)itemView.findViewById(R.id.btnDelete);
        }
    }
}