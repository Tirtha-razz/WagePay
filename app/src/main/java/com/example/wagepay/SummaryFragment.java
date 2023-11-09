  package com.example.wagepay;

  import android.content.Intent;
  import android.os.Bundle;
  import android.view.LayoutInflater;
  import android.view.View;
  import android.view.ViewGroup;

  import androidx.cardview.widget.CardView;
  import androidx.fragment.app.Fragment;


  public class SummaryFragment extends Fragment {

      CardView dueBtn;
      CardView hisBtn;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_summary, container, false);
        dueBtn = view.findViewById(R.id.duePaymentDetails);
        hisBtn = view.findViewById(R.id.paymentHistory);


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