package com.example.wagepay;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class SumTotalPaymentsAdapter extends FirebaseRecyclerAdapter<SumTotalPaymentsModel, SumTotalPaymentsAdapter.WorkerViewHolder> {

    public SumTotalPaymentsAdapter(@NonNull FirebaseRecyclerOptions<SumTotalPaymentsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull WorkerViewHolder holder, int position, @NonNull SumTotalPaymentsModel model) {
        DatabaseReference workerPaymentsRef = getRef(position).child("Payments");
        calculateTotalPayments(workerPaymentsRef, holder);
    }

    private void calculateTotalPayments(DatabaseReference paymentsRef, WorkerViewHolder holder) {
        paymentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalPayment = 0;

                for (DataSnapshot paymentSnapshot : dataSnapshot.getChildren()) {
                    int amount = Integer.parseInt(paymentSnapshot.child("amount").getValue(String.class));
                    totalPayment += amount;
                }

                holder.bindTotalPayment(totalPayment);

                // Fetch the worker's name from the parent node and set it
                DatabaseReference parentRef = paymentsRef.getParent(); // Get the parent reference
                parentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot parentSnapshot) {
                        String workerName = parentSnapshot.child("wName").getValue(String.class);
                        holder.bindWorkerName(workerName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle any errors
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });
    }




    @NonNull
    @Override
    public WorkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sum_due_payment_design, parent, false);
        return new WorkerViewHolder(view);
    }

    public static class WorkerViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView totalPaymentTextView;

        public WorkerViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            totalPaymentTextView = itemView.findViewById(R.id.wageDueTextView);
        }

        public void bindWorkerName(String workerName) {
            nameTextView.setText(workerName);
        }

        public void bindTotalPayment(int totalPayment) {
            totalPaymentTextView.setText("Rs. " + totalPayment);
        }
    }
}
