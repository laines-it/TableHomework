package com.example.tablehomework.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tablehomework.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class edit_fragment extends Fragment {
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private SharedPreferences preferences;
    private String group;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        database = FirebaseDatabase.getInstance();
        preferences = getActivity().getApplicationContext().getSharedPreferences("com.example.tablehomework", Context.MODE_PRIVATE);
        group = preferences.getString("enter","denied");
        myRef = database.getReference().child("testtable").child(group).child("timetable");
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView current_homework = requireView().findViewById(R.id.current_homework);
        Spinner spinner = requireView().findViewById(R.id.subject_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String[] path = {null,null};
                        Calendar calendar = Calendar.getInstance();
                        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
                        int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                        boolean finding = true;
                        long addday = System.currentTimeMillis();
                        int lesson = 0;
                        //Not beautiful but effective
                        for (int i = day + 1; (i <= 5) && finding; i++) {
                            addday += 86400000;
                            for (lesson = 0; lesson <= snapshot.child(String.valueOf(i)).getChildrenCount(); lesson++) {
                                if (String.valueOf(snapshot.child(String.valueOf(i)).child(String.valueOf(lesson)).child("subject").getValue()).equals(spinner.getSelectedItem().toString())) {
                                    StringBuilder hw = new StringBuilder();
                                    DataSnapshot snapshot_hw = snapshot.child(String.valueOf(i)).child(String.valueOf(lesson)).child("homework");
                                    hw.append((String) snapshot_hw.getValue());
                                    current_homework.setText(String.valueOf(hw));
                                    path[0] = "testtable/" + group + "/timetable/" + i + "/" + lesson + "/homework/";
                                    path[1] = "testtable/" + group + "/timetable/" + i + "/" + lesson + "/when/";
                                    finding = false;
                                    break;
                                }
                            }

                        }
                        for (int i = 0; (i <= day) && finding; i++) {
                            addday += 86400000;
                                for (lesson = 0; lesson <= snapshot.child(String.valueOf(i)).getChildrenCount(); lesson++) {
                                    String my_subject = spinner.getSelectedItem().toString();
                                    if (String.valueOf(snapshot.child(String.valueOf(i)).child(String.valueOf(lesson)).child("subject").getValue()).equals(my_subject)) {
                                        addday += 86400000L * (7 - day);
                                        path[0] = "testtable/" + group + "/timetable/" + i + "/" + lesson + "/homework/";
                                        path[1] = "testtable/" + group + "/timetable/" + i + "/" + lesson + "/when/";
                                        DataSnapshot snapshot_hw = snapshot.child(String.valueOf(i)).child(String.valueOf(lesson)).child("homework");
                                        current_homework.setText((String) snapshot_hw.getValue());
                                        finding = false;
                                        break;
                                    }
                                }

                        }

                        //f
                        Button edit = requireView().findViewById(R.id.edit_radio);
                        Button delete = requireView().findViewById(R.id.delete_radio);
                        Button edit_button = requireView().findViewById(R.id.edit_button);
                        Button delete_button = requireView().findViewById(R.id.delete_button);
                        Button clear_button = requireView().findViewById(R.id.clear_button);
                        EditText text_for_edit = requireView().findViewById(R.id.edit_text);
                        edit.setOnClickListener(view15 -> {
                            edit.setClickable(false);
                            delete.setClickable(true);
                            delete.setBackgroundResource(R.color.red);
                            edit.setBackgroundResource(R.color.grey);
                            if(current_homework.getText().equals("")){
                                text_for_edit.setHint("введите дз");
                            }else{
                                text_for_edit.setHint(current_homework.getText());
                                text_for_edit.setText(current_homework.getText());
                            }

                            clear_button.setClickable(true);
                            clear_button.setVisibility(View.VISIBLE);
                            text_for_edit.setVisibility(View.VISIBLE);
                            edit_button.setClickable(true);
                            edit_button.setVisibility(View.VISIBLE);
                            @SuppressLint("CutPasteId") Button delete1 = requireView().findViewById(R.id.delete_button);
                            delete1.setClickable(false);
                            delete1.setVisibility(View.INVISIBLE);

                        });
                        long finalAddday = addday;
                        edit_button.setOnClickListener(view14 -> {
                            database.getReference(path[0]).setValue(String.valueOf(text_for_edit.getText()));
                            database.getReference(path[1]).setValue(finalAddday);
                            Toast.makeText(getContext(),"Домашнее задание сохранено", Toast.LENGTH_LONG).show();
                        });
                        clear_button.setOnClickListener(view13 -> text_for_edit.setText(""));

                        delete.setOnClickListener(view12 -> {
                            edit.setClickable(true);
                            delete.setClickable(false);
                            delete.setBackgroundResource(R.color.grey);
                            edit.setBackgroundResource(R.color.green);
                            @SuppressLint("CutPasteId") EditText text_for_edit1 = requireView().findViewById(R.id.edit_text);
                            text_for_edit1.setVisibility(View.INVISIBLE);
                            edit_button.setVisibility(View.INVISIBLE);
                            edit_button.setClickable(false);

                            clear_button.setClickable(false);
                            clear_button.setVisibility(View.INVISIBLE);
                            delete_button.setVisibility(View.VISIBLE);
                            delete_button.setClickable(true);
                            String finalPath1 = path[0];
                            delete_button.setOnClickListener(view1 -> {
                                database.getReference(finalPath1).setValue("");
                                Toast.makeText(getContext(),"Домашнее задание удалено", Toast.LENGTH_LONG).show();
                            });
                        });
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