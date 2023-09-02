package com.example.tablehomework.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tablehomework.R;
import com.example.tablehomework.supports.Death;
import com.example.tablehomework.supports.Link;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class death_calendar_fragment extends Fragment {
    private DatabaseReference myRef;
    private SharedPreferences preferences;
    private FirebaseDatabase database;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("deaths");
        preferences = getActivity()
                .getApplicationContext().getSharedPreferences("com.example.tablehomework",Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CompactCalendarView calendar = requireActivity().findViewById(R.id.cal);
        TextView this_month = requireActivity().findViewById(R.id.month);
        Button add = requireActivity().findViewById(R.id.button_add);
        Button delete = requireActivity().findViewById(R.id.button_delete);
        LinearLayout parent_deaths = requireActivity().findViewById(R.id.parent_deaths);
        Typeface tf_gill = getResources().getFont(R.font.gillsans);
        Typeface tf_goth = getResources().getFont(R.font.gothic);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setUseThreeLetterAbbreviation(true);
        myRef.child(preferences.getString("enter","denied"))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        calendar.removeAllEvents();
                        for(DataSnapshot ds: snapshot.getChildren()){
                            if(!Objects.requireNonNull(ds.getKey()).equals("0")){
                                for(DataSnapshot death_snapshot : ds.getChildren()){
                                    Death death = death_snapshot.getValue(Death.class);
                                    assert death != null;
                                    if(death.getDate()>=(System.currentTimeMillis() - 82800000)) {
                                        int color = Color.RED;
                                        switch(death.getEvent()){
                                            case "Экзамен":
                                                color = Color.RED;
                                                break;
                                            case "Зачёт":
                                                color = Color.RED;
                                                break;
                                            case "Коллоквиум":
                                                color = Color.MAGENTA;
                                                break;
                                            case "Контрольная работа":
                                                color = Color.MAGENTA;
                                                break;
                                            case "Самостоятельная работа":
                                                color = Color.BLUE;
                                                break;
                                            case "Пересдача":
                                                color = Color.BLACK;
                                                break;
                                        }
                                        calendar.addEvent(new Event(color,(long) death.getDate(), ds.getKey()));
                                    }
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
        calendar.setListener(new CompactCalendarView.CompactCalendarViewListener(){
            @Override
            public void onDayClick(Date dateClicked){
                List<Event> events = calendar.getEvents(dateClicked);
                parent_deaths.removeAllViews();
                add.setClickable(true);
                add.setVisibility(View.VISIBLE);
                add.setText("Добавить");
                add.setOnClickListener(view -> {
                    DialogFragment d = new add_death_fragment();
                    Bundle a = new Bundle();
                    a.putLong("DATE",dateClicked.getTime());
                    d.setArguments(a);
                    d.show(getActivity().getSupportFragmentManager(),"what");
                });
                if(events.size() > 0){
                    Log.e("THIS DAY WE HAVE ", String.valueOf(events.size()));
                    myRef.child(preferences.getString("enter","denied"))
                         .child(String.valueOf(dateClicked.getTime())).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<String> death_list = new ArrayList<>();
                            ArrayList<String> keys_to_deaths = new ArrayList<>();
                            for(DataSnapshot ds : snapshot.getChildren()){
                                Death death = ds.getValue(Death.class);
                                assert death != null;
                                keys_to_deaths.add(ds.getKey());
                                death_list.add(death.getSubject());
                                TextView s_subject = new TextView(getActivity());
                                s_subject.setText(death.getSubject());
                                s_subject.setTextSize(dp2px(7));
                                s_subject.setTypeface(tf_goth);
                                parent_deaths.addView(s_subject);
                                TextView s_death = new TextView(getActivity());
                                s_death.setText(death.getEvent());
                                s_death.setTextSize(dp2px(7));
                                s_death.setTypeface(tf_goth);
                                parent_deaths.addView(s_death);
                                if(!(death.getLinks() == null)){
                                    TextView useful = new TextView(getActivity());
                                    useful.setText("Полезные ссылки");
                                    useful.setTextSize(dp2px(6));
                                    useful.setTypeface(tf_goth);
                                    parent_deaths.addView(useful);
                                    for(Link link : death.getLinks()){
                                        LinearLayout parent = new LinearLayout(getActivity());
                                        parent.setOrientation(LinearLayout.HORIZONTAL);
                                        TextView namelink = new TextView(getActivity());
                                        namelink.setTextSize(dp2px(7));
                                        namelink.setTypeface(tf_gill);
                                        namelink.setText(link.getName());
                                        parent.addView(namelink);

                                        ImageButton button = new ImageButton(getActivity());
                                        button.setImageResource(android.R.drawable.ic_menu_view);
                                        button.setOnClickListener(view1 -> {
                                            Uri uri_vk = Uri.parse(link.getLink());
                                            Intent intent_v = new Intent(Intent.ACTION_VIEW, uri_vk);
                                            startActivity(intent_v);
                                        });
                                        parent.addView(button);
                                        parent_deaths.addView(parent);
                                    }
                                }
                                View line = new View(getActivity());
                                LinearLayout.LayoutParams lines = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(1));
                                line.setBackgroundResource(R.color.black);
                                line.setLayoutParams(lines);
                                parent_deaths.addView(line);
                            }
                            delete.setVisibility(View.VISIBLE);
                            delete.setClickable(true);
                            delete.setText("Удалить");
                            delete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DialogFragment d = new delete_death_fragment();
                                    Bundle a = new Bundle();
                                    a.putLong("DATETODELETE",dateClicked.getTime());
                                    a.putStringArrayList("DEATHLIST", death_list);
                                    a.putStringArrayList("KEYSLIST", keys_to_deaths);
                                    d.setArguments(a);
                                    d.show(getActivity().getSupportFragmentManager(),"what");
                                    delete.setClickable(false);
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else{
                    delete.setVisibility(View.INVISIBLE);
                    delete.setClickable(false);
                }

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                this_month.setText(dateFormat.format(firstDayOfNewMonth));
            }
        });
    }

    private int dp2px(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
}