package com.example.tablehomework.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.loader.content.Loader;

import com.example.tablehomework.MainActivity;
import com.example.tablehomework.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class splash_fragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_splash, null);
        getDialog().setTitle("Загрузочный экран");
        Button yes = v.findViewById(R.id.yes_splash);
        Button no = v.findViewById(R.id.no_splash);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getApplicationContext().getSharedPreferences("com.example.tablehomework", Context.MODE_PRIVATE)
                        .edit().putString("remove_splash","giga").apply();
                Toast.makeText(getActivity(),"Изображение оставлено", Toast.LENGTH_LONG).show();
                dismiss();

            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getApplicationContext().getSharedPreferences("com.example.tablehomework", Context.MODE_PRIVATE)
                        .edit().putString("remove_splash","none").apply();
                Toast.makeText(getActivity(),"Изображение убрано", Toast.LENGTH_LONG).show();
                FirebaseDatabase.getInstance().getReference().child("stats").child("remove_panf").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                            FirebaseDatabase.getInstance().getReference("stats/remove_panf/")
                                    .setValue(snapshot.getValue(Integer.class) + 1);
                        }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
                dismiss();
            }
        });
        return v;
    }
}

