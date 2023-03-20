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
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class new_death_calendar_fragment extends Fragment {
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
        Spinner s_subject = requireActivity().findViewById(R.id.spinner_death);
        Spinner s_event = requireActivity().findViewById(R.id.spinner_event);
        TextView this_month = requireActivity().findViewById(R.id.month);
        Button add = requireActivity().findViewById(R.id.button_add);
        Button delete = requireActivity().findViewById(R.id.button_delete);
        LinearLayout parent = requireActivity().findViewById(R.id.parent_deaths);
        Typeface tf_gill = getResources().getFont(R.font.gillsans);
        Typeface tf_goth = getResources().getFont(R.font.gothic);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setUseThreeLetterAbbreviation(true);
        myRef.child(getActivity().getApplicationContext()
                .getSharedPreferences("com.example.tablehomework", Context.MODE_PRIVATE)
                .getString("enter","denied"))
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                calendar.removeAllEvents();
                for(DataSnapshot ds: snapshot.getChildren()){
                    if(!Objects.requireNonNull(ds.getKey()).equals("0")){
                        Death death = ds.getValue(Death.class);
                        assert death != null;
                        if(death.getDate()>=(System.currentTimeMillis() - 82800000)) {
                            int c = R.color.green;
                            switch(death.getEvent()){
                                case("Экзамен"): c = R.color.bord; break;
                                case("Зачёт"): c = R.color.bord; break;
                                case("Коллоквиум"): c = R.color.red; break;
                                case("Контрольная работа"): c = R.color.orange; break;
                                case("Самостоятельная работа"): c = R.color.purple_500; break;
                                case("Пересдача"): c = R.color.black; break;
                            }
                            calendar.addEvent(new Event(c, (long) death.getDate(), ds.getKey()));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        calendar.setListener(new CompactCalendarView.CompactCalendarViewListener(){
            @Override
            public void onDayClick(Date dateClicked){
                List<Event> events = calendar.getEvents(dateClicked);

                if(events.size() == 0){
                    s_subject.setSelection(0);
                    s_event.setSelection(0);
                    s_subject.setVisibility(View.VISIBLE);
                    s_event.setVisibility(View.VISIBLE);
                    s_subject.setEnabled(true);
                    s_event.setEnabled(true);
                    add.setText("Добавить");
                    add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            myRef.child(preferences.getString("enter","denied")).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Death death = new Death();
                                    long time = dateClicked.getTime();
                                    death.setDate(time);
                                    death.setSubject(String.valueOf(s_subject.getSelectedItem()));
                                    death.setEvent(String.valueOf(s_event.getSelectedItem()));
                                    database.getReference("deaths/" + preferences.getString("enter","denied") + "/" + time).setValue(death);
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });
                }else{
                    s_subject.setEnabled(false);
                    s_event.setEnabled(false);
                    s_subject.setVisibility(View.INVISIBLE);
                    s_event.setVisibility(View.INVISIBLE);
                    add.setText("Добавить");
                    delete.setText("Удалить");
                    delete.setClickable(true);
                    delete.setVisibility(View.VISIBLE);
                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            database.getReference("deaths/" + preferences.getString("enter","denied") + "/" + dateClicked.getTime()).removeValue();
                        }
                    });
                    myRef.child(preferences.getString("enter","denied")).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(Event e : events){
                                Death death = snapshot.child(String.valueOf(e.getData())).getValue(Death.class);
                                assert death != null;
                                LinearLayout fordeath = new LinearLayout(getActivity());
                                LinearLayout.LayoutParams fd = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                TextView subject = new TextView(getActivity());
                                TextView event = new TextView(getActivity());
                                subject.setText(death.getSubject());
                                event.setText(death.getEvent());
                                subject.setLayoutParams(fd);
                                event.setLayoutParams(fd);
                                subject.setTextSize(dp2px(20));
                                event.setText(dp2px(20));
                                subject.setTypeface(tf_goth);
                                event.setTypeface(tf_goth);
                                fordeath.addView(subject);
                                fordeath.addView(event);


                                LinearLayout forlinks = new LinearLayout(getActivity());
                                if(!(death.getLinks() == null)){
                                    TextView useful = new TextView(getActivity());
                                    useful.setText("Полезные ссылки");
                                    useful.setTextSize(dp2px(7));
                                    useful.setTypeface(tf_goth);
                                    forlinks.addView(useful);
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
                                        forlinks.addView(parent);
                                    }
                                }
                                fordeath.addView(forlinks);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                this_month.setText(dateFormat.format(firstDayOfNewMonth));
            }
        });
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int m, int dayOfMonth) {
//                int month = m + 1;
//                Log.e("Selected day is",String.valueOf(month) + "/" + String.valueOf(dayOfMonth));
//                LinearLayout links = getActivity().findViewById(R.id.for_links);
//                myReueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                            Death d = dataSnapshot.getValue(Death.class);
//                            if(month==12){
//                                String n = "Билеты";
//                                String l = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
//                                Link link = new Link(n,l);
//                                myRe
//                            if (d.getDate().equals(String.valueOf(month) + "/" + String.valueOf(dayOfMonth))) {
//                                subject.setText(d.getSubject());
//                                event.setText(d.getEvent());
//                                for(Link l : d.getLinks()){
//                                event.setText(l.getLink());}
//                                links.setVisibility(View.VISIBLE);
//                            }else{
//                                links.setVisibility(View.INVISIBLE);
//                                subject.setText("Ничего нет");
//                                event.setText("Отдыхай (ботай)");
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                    }
//                });
//            }
//        });
    }

    private int dp2px(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
}
