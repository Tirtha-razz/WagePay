package com.example.wagepay;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    ArrayList<WorkerRecyclerModel> arrDetails = new ArrayList<>();
    WorkerRecyclerAdapter adapter;
    SearchView searchView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        //for searching
        searchView = view.findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });


        //for recycler view of worker list
        RecyclerView recyclerView = view.findViewById(R.id.worker_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //clearing array
        arrDetails.clear();

        arrDetails.add(new WorkerRecyclerModel(R.drawable.profile_pic,"Eden Hazard","9816086195"));
        arrDetails.add(new WorkerRecyclerModel(R.drawable.profile_pic,"Neymar JR","9816086195"));
        arrDetails.add(new WorkerRecyclerModel(R.drawable.profile_pic,"Enzo Farnandez","9816086195"));
        arrDetails.add(new WorkerRecyclerModel(R.drawable.profile_pic,"Recee James","9816086195"));
        arrDetails.add(new WorkerRecyclerModel(R.drawable.profile_pic,"Levi Cowill","9816086195"));
        arrDetails.add(new WorkerRecyclerModel(R.drawable.profile_pic,"Ben Chilwell","9816086195"));
        arrDetails.add(new WorkerRecyclerModel(R.drawable.profile_pic,"Armando Broja","9816086195"));
        arrDetails.add(new WorkerRecyclerModel(R.drawable.profile_pic,"Noni Madueke","9816086195"));
        arrDetails.add(new WorkerRecyclerModel(R.drawable.profile_pic,"Christopher Nkunku","9816086195"));
        arrDetails.add(new WorkerRecyclerModel(R.drawable.profile_pic,"Mykhaylo Mudryk","9816086195"));

        adapter = new WorkerRecyclerAdapter(this,arrDetails);
        recyclerView.setAdapter(adapter);



        return view;
    }

    private void filterList(String text) {
        ArrayList<WorkerRecyclerModel> filteredList = new ArrayList<>();

        for (WorkerRecyclerModel item : arrDetails){
            if (item.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }

        }
        if(filteredList.isEmpty()){
            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        }
        else {
            adapter.setFilteredList(filteredList);
        }
    }

}