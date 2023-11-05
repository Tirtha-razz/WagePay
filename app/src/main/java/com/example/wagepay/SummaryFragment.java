  package com.example.wagepay;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


  public class SummaryFragment extends Fragment {

    CardView advBtn, dueBtn, hisBtn;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_summary, container, false);

        advBtn = view.findViewById(R.id.advancePaidDetails);
        dueBtn = view.findViewById(R.id.duePaymentDetails);
        hisBtn = view.findViewById(R.id.paymentHistory);

        advBtn.setOnClickListener(v -> {

        });

        dueBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SumDuePayment.class);

            // Start the new activity
            startActivity(intent);
        });

        hisBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SumTotalPayments.class);

            // Start the new activity
            startActivity(intent);
        });

        return view;
    }
}