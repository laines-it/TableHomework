package com.example.tablehomework.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.DialogFragment;

import com.example.tablehomework.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class enter_dialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.dialog_enter, null);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("version").child("count");
        LinearLayoutCompat parent = v.findViewById(R.id.parent_group);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    Button button = new Button(getActivity());
                    button.setText(ds.getKey());
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences("com.example.tablehomework", Context.MODE_PRIVATE);
                            preferences.edit().putString("enter",ds.getKey()).apply();
                            Log.e("ds.key",ds.getKey());
                            Log.e("there",preferences.getString("enter","default"));
                            ref.child(ds.getKey()).setValue(ds.getValue(Integer.class)  + 1);
                            dismiss();
                        }
                    });
                    parent.addView(button);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ERROR", String.valueOf(error));
            }
        });
        return v;
    }
}

