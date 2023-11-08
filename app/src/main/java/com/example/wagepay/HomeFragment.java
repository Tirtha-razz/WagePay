package com.example.wagepay;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class HomeFragment extends Fragment {
    WorkerRecyclerAdapter workerRecyclerAdapter;
    SearchView searchView;

    FirebaseAuth firebaseAuth;

    FirebaseUser currentUser;
    String phoneNo;
    String selectedCategoryId;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Access the selectedCategoryId from SharedPreferences in your MainActivity
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        selectedCategoryId = sharedPreferences.getString("selectedCategoryId", null);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


//        //for searching
//        searchView = view.findViewById(R.id.searchView);
//        searchView.clearFocus();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                filterList(newText);
//                return true;
//            }
//        });

        // Retrieve the user's phone number as the userId
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            phoneNo = currentUser.getPhoneNumber();
        }

        // Check if a category ID has been selected
        if (selectedCategoryId != null) {

            // Debug print statement
            Log.d("DEBUG", "Selected Category ID: " + selectedCategoryId);

            // Query workers associated with the selected category
            FirebaseRecyclerOptions<WorkerRecyclerModel> options =
                    new FirebaseRecyclerOptions.Builder<WorkerRecyclerModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference("Users").child(phoneNo).child("Workers").orderByChild("categoryId").equalTo(selectedCategoryId), WorkerRecyclerModel.class)
                            .build();
            workerRecyclerAdapter = new WorkerRecyclerAdapter(options, phoneNo, selectedCategoryId);
        } else {
            // Debug print statement
            Log.d("DEBUG", "No selected category. Displaying all workers.");

            // Default query for all workers
            FirebaseRecyclerOptions<WorkerRecyclerModel> options =
                    new FirebaseRecyclerOptions.Builder<WorkerRecyclerModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference("Users").child(phoneNo).child("Workers"), WorkerRecyclerModel.class)
                            .build();
            workerRecyclerAdapter = new WorkerRecyclerAdapter(options, phoneNo, selectedCategoryId);
        }

        RecyclerView recyclerView = view.findViewById(R.id.worker_recyclerView);
        CustomLinearLayoutManager layoutManager = new CustomLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(workerRecyclerAdapter);

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        workerRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        workerRecyclerAdapter.stopListening();
    }
    //    private void filterList(String text) {
//        ArrayList<WorkerRecyclerModel> filteredList = new ArrayList<>();
//
//        for (WorkerRecyclerModel item : arrDetails){
//            if (item.getName().toLowerCase().contains(text.toLowerCase())){
//                filteredList.add(item);
//            }
//
//        }
//        if(filteredList.isEmpty()){
//            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
//        }
//        else {
//            adapter.setFilteredList(filteredList);
//        }
//    }

    // This method updates the data in the fragment based on the selected category
    public void updateDataBasedOnCategory(String selectedCategoryId) {
        if (selectedCategoryId != null) {
            FirebaseRecyclerOptions<WorkerRecyclerModel> options =
                    new FirebaseRecyclerOptions.Builder<WorkerRecyclerModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference("Users").child(phoneNo).child("Workers").orderByChild("categoryId").equalTo(selectedCategoryId), WorkerRecyclerModel.class)
                            .build();
            workerRecyclerAdapter = new WorkerRecyclerAdapter(options, phoneNo, selectedCategoryId);
        } else {
            FirebaseRecyclerOptions<WorkerRecyclerModel> options =
                    new FirebaseRecyclerOptions.Builder<WorkerRecyclerModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference("Users").child(phoneNo).child("Workers"), WorkerRecyclerModel.class)
                            .build();
            workerRecyclerAdapter = new WorkerRecyclerAdapter(options, phoneNo, selectedCategoryId);
        }

        RecyclerView recyclerView = requireView().findViewById(R.id.worker_recyclerView);
        recyclerView.setAdapter(workerRecyclerAdapter);
        workerRecyclerAdapter.startListening();
    }

}