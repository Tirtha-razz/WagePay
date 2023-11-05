package com.example.wagepay;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class PaymentAdapter extends FirebaseRecyclerAdapter<PaymentModel, PaymentAdapter.PaymentViewHolder> {

    public PaymentAdapter(@NonNull FirebaseRecyclerOptions<PaymentModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PaymentViewHolder holder, int position, @NonNull PaymentModel model) {
        holder.bind(model);
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_item_layout, parent, false);
        return new PaymentViewHolder(view);
    }

    public static class PaymentViewHolder extends RecyclerView.ViewHolder {
        private TextView amountTextView;
        private TextView dateTextView;

        public PaymentViewHolder(View itemView) {
            super(itemView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }

        public void bind(PaymentModel model) {
            amountTextView.setText("Amount: Rs." + model.getAmount());
            dateTextView.setText("Date: " + model.getDate());
        }
    }
}
