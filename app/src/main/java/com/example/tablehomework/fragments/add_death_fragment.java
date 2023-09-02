package com.example.tablehomework.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.loader.content.Loader;

import com.example.tablehomework.R;
import com.example.tablehomework.supports.Death;
import com.example.tablehomework.supports.LinkFromLesson;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class add_death_fragment extends DialogFragment {
    long time;
    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_add_death, null);
        Button add = v.findViewById(R.id.addnewdeath);
        Spinner s_subject = v.findViewById(R.id.spinner_death);
        Spinner s_death = v.findViewById(R.id.spinner_event);
        String group = getActivity().getApplicationContext().getSharedPreferences("com.example.tablehomework",Context.MODE_PRIVATE).getString("enter","denied");
        FirebaseDatabase.getInstance().getReference().child("testtable").child(group)
                .child("link").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                //Fill spinner
                ArrayList<String> arrayList = new ArrayList<>();
                for (DataSnapshot snap : ds.getChildren()) {
                    arrayList.add(snap.getKey());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayList);
                s_subject.setAdapter(arrayAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        time = getArguments().getLong("DATE",0);
        assert getArguments() != null;
        add.setOnClickListener(view -> {
            if(!String.valueOf(s_death.getSelectedItem()).equals("Смерть")){
                add.setClickable(false);
                Death death = new Death();
                death.setDate(time);
                death.setSubject(String.valueOf(s_subject.getSelectedItem()));
                death.setEvent(String.valueOf(s_death.getSelectedItem()));
                FirebaseDatabase.getInstance().getReference("deaths/" + group + "/" + time).push().setValue(death);
                Toast.makeText(getActivity(),"Смерть добавлена. Сожалеем",Toast.LENGTH_LONG).show();
                dismiss();
            }else{
                Toast.makeText(getActivity(),"Error: Не выбрана Смерть", Toast.LENGTH_LONG).show();
                dismiss();
            }
        });
        return v;
    }
}

