package com.example.meowing;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.meowing.databinding.FragmentFirstBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirstFragment extends Fragment {

    FragmentFirstBinding binding;
    private DatabaseReference catsReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        return inflater.inflate(R.layout.fragment_first, container, false);

        binding = FragmentFirstBinding.inflate(getLayoutInflater());

        catsReference = FirebaseDatabase.getInstance().getReference("Ospiti");
        ArrayList<Cat> catArrayList = new ArrayList<>();

        catsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {

                    String nome = ds.child("nome").getValue(String.class);
                    String eta = ds.child("eta").getValue(String.class);
                    String sesso = ds.child("sesso").getValue(String.class);
                    String razza = ds.child("razza").getValue(String.class);
                    String imageUrl = ds.child("imageUrl").getValue(String.class);
                    String chiave = ds.getKey();
                    Cat cat = new Cat(nome, eta, sesso, razza, imageUrl, chiave);
                    catArrayList.add(cat);

                }
                ListAdapter listAdapter = new ListAdapter(getContext(), catArrayList);
                binding.listViewID.setAdapter(listAdapter);
                binding.listViewID.deferNotifyDataSetChanged();
                binding.listViewID.setClickable(true);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return binding.getRoot();
    }
}