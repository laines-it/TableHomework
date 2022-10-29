package com.example.tablehomework.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class death_calendar_fragment extends Fragment {
    private DatabaseReference myRef;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("deaths");
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
        TextView subject = requireActivity().findViewById(R.id.subject_death);
        TextView event = requireActivity().findViewById(R.id.event_death);
        TextView this_month = requireActivity().findViewById(R.id.month);
        LinearLayout forlinks = requireActivity().findViewById(R.id.for_links);
        Typeface tf_gill = getResources().getFont(R.font.gillsans);
        Typeface tf_goth = getResources().getFont(R.font.gothic);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setUseThreeLetterAbbreviation(true);
        myRef.child("116").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    Log.e("ds is",ds.toString());
                    if(!Objects.requireNonNull(ds.getKey()).equals("0")){
                        Death death = ds.getValue(Death.class);
                        assert death != null;
                        if(death.getDate()>=(System.currentTimeMillis() - 82800000)) {
                            calendar.addEvent(new Event(Color.RED, (long) death.getDate(), ds.getKey()));
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
                forlinks.removeAllViews();
                if(events.size() == 0){
                    subject.setText("");
                    event.setText("");
                }else{
                    myRef.child("116").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Death death = snapshot.child(String.valueOf(events.get(0).getData())).getValue(Death.class);
                            assert death != null;
                            subject.setText(death.getSubject());
                            event.setText(death.getEvent());

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
//                myRef.child("116").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                            Death d = dataSnapshot.getValue(Death.class);
//                            if(month==12){
//                                String n = "Билеты";
//                                String l = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
//                                Link link = new Link(n,l);
//                                myRef.child("116").push();}
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
