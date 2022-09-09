package com.example.tablehomework.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.tablehomework.R;
import com.example.tablehomework.supports.Homework;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class dz_fragment extends Fragment {
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_dz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myRef.child("homework").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Homework> hw_list = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    Homework hw = dataSnapshot.getValue(Homework.class);
//                    TableLayout tl = getView().findViewById(R.id.parent_matrix);
//                    TextView date_tw = new TextView(getActivity());
//                    date_tw.setText(hw.getDate());
//                    TextView task_tw = new TextView(getActivity());
//                    task_tw.setText(hw.getTask());
//                    TextView subject_tw = new TextView(getActivity());
//                    subject_tw.setText(hw.getSubject());
//                    TableRow tableRow = new TableRow(getActivity());
//                    date_tw.setTextSize(dp2px(7));
//                    subject_tw.setTextSize(dp2px(7));
//                    task_tw.setTextSize(dp2px(7));
//                    tableRow.addView(date_tw);
//                    tableRow.addView(subject_tw);
//                    tableRow.addView(task_tw);
//                    tl.addView(tableRow);
//                    tl.addView(new TextView(getActivity()));
//                    hw_list.add(hw);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private int dp2px(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
}