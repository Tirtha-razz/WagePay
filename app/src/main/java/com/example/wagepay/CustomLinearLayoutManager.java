package com.example.wagepay;

import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CustomLinearLayoutManager extends LinearLayoutManager {

    private static final String TAG = "CustomLinearLayoutManager";

    public CustomLinearLayoutManager(Context context, int horizontal, boolean reverseLayout) {
        super(context, horizontal, reverseLayout);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG, "Inconsistency detected");
        }
    }
}
