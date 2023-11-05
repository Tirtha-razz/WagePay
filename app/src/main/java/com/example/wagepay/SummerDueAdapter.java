package com.example.wagepay;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class SummerDueAdapter extends FirebaseRecyclerAdapter<SummaryDuePaymentModel, SummerDueAdapter.WorkerViewHolder> {

    public SummerDueAdapter(@NonNull FirebaseRecyclerOptions<SummaryDuePaymentModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull WorkerViewHolder holder, int position, @NonNull SummaryDuePaymentModel model) {
        holder.bind(model);
    }

    @NonNull
    @Override
    public WorkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sum_due_payment_design, parent, false);
        return new WorkerViewHolder(view);
    }

    public static class WorkerViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView wageDueTextView;

        public WorkerViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            wageDueTextView = itemView.findViewById(R.id.wageDueTextView);
        }

        public void bind(SummaryDuePaymentModel model) {
            nameTextView.setText(model.getWName());
            wageDueTextView.setText(String.valueOf(model.getWageDue()));
        }
    }
}
