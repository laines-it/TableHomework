package com.example.tablehomework.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.tablehomework.R;
import com.example.tablehomework.supports.Lesson;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import javax.xml.transform.sax.SAXResult;

public class edit_fragment extends Fragment {
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Spinner spinner = getView().findViewById(R.id.subject_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView current_homework = getView().findViewById(R.id.current_homework);
                myRef.child("timetable").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
                        int day = calendar.get(Calendar.DAY_OF_WEEK);
                        day = 0;
//                Log.d("TODAY", String.valueOf(day));
                        Log.d("TODAY", String.valueOf(day));
                        boolean finding = true;

                        //Not beautiful but effective
                        for(int i = day+1;i<=5;i++){
                            if(!finding){break;}
                            for(int lesson = 0; lesson<=3;lesson++) {
                                Log.d("I'm checking", String.valueOf(i) + lesson);
                                Log.e("Subject", String.valueOf(snapshot.child(String.valueOf(i)).child(String.valueOf(lesson)).child("subject").getValue()));
                                if (String.valueOf(snapshot.child(String.valueOf(i)).child(String.valueOf(lesson)).child("subject").getValue()).equals(spinner.getSelectedItem().toString())) {
                                    Log.e("IMPORTANT","U FOUND THIS!");
                                    StringBuilder hw = new StringBuilder();
                                    for (DataSnapshot ds : (snapshot.child(String.valueOf(i)).child("homework").getChildren()))
                                        hw.append((String)ds.getValue());
                                    current_homework.setText(String.valueOf(hw));
                                    Log.e("CH set text", String.valueOf(hw));
                                    finding = false;
                                    break;
                                }
                            }

                        }
                        if(finding) {
                            for (int i = 1; i <= day; i++) {

                                for(int lesson = 0; lesson<=3;lesson++) {
                                    Log.d("I'm checking", String.valueOf(i) + lesson);
                                    if (String.valueOf(snapshot.child(String.valueOf(i)).child(String.valueOf(lesson)).child("subject").getValue()).equals(spinner.getSelectedItem().toString())) {
                                        StringBuilder hw = new StringBuilder();
                                        for (Object s : Objects.requireNonNull(snapshot.child(String.valueOf(i)).child("homework").getValue(List.class)))
                                            hw.append(s);
                                        current_homework.setText(hw);
                                        finding = false;
                                        break;
                                    }
                                }
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
}