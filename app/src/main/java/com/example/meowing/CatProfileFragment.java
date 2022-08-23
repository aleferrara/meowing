package com.example.meowing;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.example.meowing.databinding.FragmentCatProfileBinding;
import com.example.meowing.databinding.FragmentMainListBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CatProfileFragment extends Fragment implements View.OnClickListener {

    FragmentCatProfileBinding binding;
    ImageButton addBtn;
    ImageButton removeBtn;
    String chiave;
    DatabaseReference listRef;

    public CatProfileFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCatProfileBinding.inflate(getLayoutInflater());

        addBtn = binding.addBtn;
        removeBtn = binding.removeBtn;
        addBtn.setOnClickListener(this);
        removeBtn.setOnClickListener(this);
        Bundle data = getArguments();

        String name = data.getString("nome");
        String eta = data.getString("eta");
        String sesso = data.getString("sesso");
        String razza = data.getString("razza");
        String image = data.getString("image");
        chiave = data.getString("chiave");
        Glide.with(this).load(image).into(binding.catProfileImage);
        binding.textNome.setText(name);
        binding.textEta.setText(eta);
        binding.textSesso.setText(sesso);
        binding.textRazza.setText(razza);


        listRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Lista");
        listRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(chiave).exists()) {
                    addBtn.setClickable(false);
                    addBtn.setImageDrawable(getActivity().getDrawable(R.drawable.ic_add_grey));
                    removeBtn.setClickable(true);
                    removeBtn.setImageDrawable(getActivity().getDrawable(R.drawable.ic_remove_red));
                } else {
                    addBtn.setClickable(true);
                    addBtn.setImageDrawable(getActivity().getDrawable(R.drawable.ic_add_green));
                    removeBtn.setClickable(false);
                    removeBtn.setImageDrawable(getActivity().getDrawable(R.drawable.ic_remove_grey));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addBtn:
                listRef.child(chiave).setValue(true);
                addBtn.setClickable(false);
                addBtn.setImageDrawable(getActivity().getDrawable(R.drawable.ic_add_grey));
                removeBtn.setClickable(true);
                removeBtn.setImageDrawable(getActivity().getDrawable(R.drawable.ic_remove_red));
                break;
            case R.id.removeBtn:
                listRef.child(chiave).removeValue();
                addBtn.setClickable(true);
                addBtn.setImageDrawable(getActivity().getDrawable(R.drawable.ic_add_green));
                removeBtn.setClickable(false);
                removeBtn.setImageDrawable(getActivity().getDrawable(R.drawable.ic_remove_grey));
                break;
        }
    }

}