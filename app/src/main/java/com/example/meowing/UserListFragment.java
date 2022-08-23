package com.example.meowing;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.meowing.databinding.FragmentCatProfileBinding;
import com.example.meowing.databinding.FragmentUserListBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserListFragment extends Fragment {

    FragmentUserListBinding binding;
    private DatabaseReference catsReference;

    public UserListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentUserListBinding.inflate(getLayoutInflater());

        ArrayList<Cat> catArrayList = new ArrayList<>();
        DatabaseReference listRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Lista");

        listRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){

                    String key = ds.getKey();
                    Log.i("chiave", key);
                    catsReference = FirebaseDatabase.getInstance().getReference().child("Ospiti").child(key);
                    catsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String nome = snapshot.child("nome").getValue().toString();
                            String eta = snapshot.child("eta").getValue(String.class);
                            String sesso = snapshot.child("sesso").getValue(String.class);
                            String razza = snapshot.child("razza").getValue(String.class);
                            String imageUrl = snapshot.child("imageUrl").getValue(String.class);
                            String chiave = snapshot.getKey();
                            Cat cat = new Cat(nome, eta, sesso, razza, imageUrl, chiave);
                            String nometest = cat.getNome();
                            Log.i("testnome", nometest);
                            catArrayList.add(cat);
                            String asize = String.valueOf(catArrayList.size());
                            Log.i("arraysize", asize);

                            if (isAdded()) {
                                ListAdapter listAdapter = new ListAdapter(getActivity(), catArrayList);
                                binding.pvtList.setAdapter(listAdapter);
                                binding.pvtList.deferNotifyDataSetChanged();
                                binding.pvtList.setClickable(true);
                                binding.pvtList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                    }
                                });
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return binding.getRoot();

    }
}