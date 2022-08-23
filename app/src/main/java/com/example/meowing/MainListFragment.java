package com.example.meowing;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.meowing.databinding.FragmentMainListBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainListFragment extends Fragment {

    FragmentMainListBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMainListBinding.inflate(getLayoutInflater());
        DatabaseReference catsReference = FirebaseDatabase.getInstance().getReference("Ospiti");
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
                binding.listViewID.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Cat cat = (Cat) adapterView.getItemAtPosition(i);
                        Bundle data = new Bundle();
                        data.putString("nome", cat.getNome());
                        data.putString("eta", cat.getEta());
                        data.putString("sesso", cat.getSesso());
                        data.putString("razza", cat.getRazza());
                        data.putString("image", cat.getImage());
                        data.putString("chiave", cat.getChiave());
                        CatProfileFragment catProfileFragment = new CatProfileFragment();
                        catProfileFragment.setArguments(data);
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, catProfileFragment);
                        fragmentTransaction.commit();

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return binding.getRoot();
    }
}