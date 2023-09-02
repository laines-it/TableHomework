package com.example.tablehomework.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.tablehomework.R;
import com.example.tablehomework.supports.Death;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class delete_death_fragment extends DialogFragment {
    long time;
    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_delete_death, null);
        Button delete = v.findViewById(R.id.deletethedeath);
        Spinner spinner = v.findViewById(R.id.spinner_deletable);
        String group = getActivity().getApplicationContext().getSharedPreferences("com.example.tablehomework",Context.MODE_PRIVATE).getString("enter","denied");
        spinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                                                    getArguments().getStringArrayList("DEATHLIST")));
        assert getArguments() != null;
        spinner.setSelection(0);
        delete.setOnClickListener(view -> {
                delete.setClickable(false);
                time = getArguments().getLong("DATETODELETE");
                FirebaseDatabase.getInstance().getReference()
                        .child("deaths").child(group).child(String.valueOf(time))
                        .child(getArguments().getStringArrayList("KEYSLIST").get(spinner.getSelectedItemPosition())).removeValue();
                Toast.makeText(getActivity(),"Смерть удалена. Ну и слава Богу",Toast.LENGTH_LONG).show();
                dismiss();
        });
        return v;
    }
}

