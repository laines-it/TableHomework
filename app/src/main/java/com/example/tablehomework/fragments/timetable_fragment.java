package com.example.tablehomework.fragments;

import static java.lang.Math.abs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tablehomework.supports.Access;
import com.example.tablehomework.R;
import com.example.tablehomework.supports.Lesson;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.List;

public class timetable_fragment extends Fragment {
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private List<String> ver;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("timetable");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timetable, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Typeface tf_gill = getResources().getFont(R.font.gillsans);
        Typeface tf_goth = getResources().getFont(R.font.gothic);
        myRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String[] days = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница"};
                int i = 0;

                Calendar calendar = Calendar.getInstance();
                calendar.setFirstDayOfWeek(Calendar.MONDAY);
                int today = calendar.get(Calendar.DAY_OF_WEEK) - 2;
                Log.e("TODAY", String.valueOf(today));
                boolean weekend = (1>today || today>5);
                Log.e("WEEKEND", String.valueOf(weekend));
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                LinearLayout l = getView().findViewById(R.id.parent);
                l.removeAllViews();
                TextView gotoday = new TextView(getActivity());
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TextView this_day = new TextView(getActivity());
                    String day = days[i];

                    if (i+1 == today) {
                        gotoday = this_day;
                        SpannableString context = new SpannableString(day);
                        context.setSpan(new UnderlineSpan(), 0, day.length(), 0);
                        this_day.setText(context);
                        this_day.setId(View.generateViewId());
                        this_day.setTextColor(Color.BLACK);
                    } else {
                        this_day.setText(day);
                        this_day.setTextColor(Color.GRAY);
                    }

                    this_day.setTypeface(tf_goth);
                    this_day.setTextSize(dp2px(10));
                    l.addView(this_day);
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Lesson lesson = ds.getValue(Lesson.class);
                        if (!lesson.getSubject().equals("История (лекция)") || (((abs(Calendar.getInstance().getTimeInMillis() - 1662336000000L) / 24) % 14) < 7)) {
                            LinearLayout.LayoutParams forparent = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            LinearLayout les_parent = new LinearLayout(getActivity());
                            les_parent.setLayoutParams(forparent);
                            les_parent.setOrientation(LinearLayout.VERTICAL);
                            LinearLayout les_layout = new LinearLayout(getActivity());
                            les_layout.setLayoutParams(forparent);
                            les_layout.setWeightSum(10);
                            les_layout.setOrientation(LinearLayout.HORIZONTAL);
                            LinearLayout.LayoutParams small = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 4);
                            LinearLayout.LayoutParams big = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 2);


                            TextView timetv = new TextView(getActivity());
                            timetv.setLayoutParams(small);
                            timetv.setTextSize(dp2px(8));
                            timetv.setText(lesson.getTime());

                            TextView sjtv = new TextView(getActivity());
                            sjtv.setTextSize(dp2px(9));
                            sjtv.setLayoutParams(big);
                            sjtv.setText(lesson.getSubject());
                            sjtv.setGravity(Gravity.CENTER);

                            TextView roomtv = new TextView(getActivity());
                            roomtv.setLayoutParams(small);
                            roomtv.setTextSize(dp2px(8));
                            roomtv.setText(lesson.getRoom());
                            roomtv.setGravity(Gravity.CENTER);

                            TextView hwtv = new TextView(getActivity());
                            hwtv.setText(lesson.getHomework());
                            hwtv.setTypeface(tf_gill);
                            hwtv.setLayoutParams(big);
                            hwtv.setTextSize(dp2px(7));
                            hwtv.setGravity(Gravity.CENTER_VERTICAL);
                            //special for hwtv
                            LinearLayout hw_parent = new LinearLayout(getActivity());
                            hw_parent.setLayoutParams(forparent);
                            hw_parent.setOrientation(LinearLayout.HORIZONTAL);
                            hw_parent.setWeightSum(10);
                            TextView placeholder_l = new TextView(getActivity());
                            placeholder_l.setLayoutParams(small);

                            hw_parent.addView(placeholder_l);
                            hw_parent.addView(hwtv);
                            if (lesson.getSubject().equals("Алгебра и геометрия (сем)")) {
                                RelativeLayout forbutton = new RelativeLayout(getActivity());
                                forbutton.setLayoutParams(small);
                                ImageButton classroom_button = new ImageButton(getActivity());
                                classroom_button.setImageResource(R.drawable.google_classroom_logo);
                                LinearLayout.LayoutParams with_button = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dp2px(50));
                                classroom_button.setLayoutParams(with_button);
                                classroom_button.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                                forbutton.addView(classroom_button);
                                hw_parent.addView(forbutton);
                                classroom_button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Uri uri = Uri.parse("https://classroom.google.com/c/NTQ3NTIyODA0OTQ1?cjc=qumdoir");
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                if (lesson.getSubject().equals("Алгоритмы и языки (лекция)")) {
                                    RelativeLayout forbutton = new RelativeLayout(getActivity());
                                    forbutton.setLayoutParams(small);
                                    ImageButton classroom_button = new ImageButton(getActivity());
                                    classroom_button.setImageResource(R.drawable.moodle_logo);
                                    LinearLayout.LayoutParams with_button = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dp2px(50));
                                    classroom_button.setLayoutParams(with_button);
                                    classroom_button.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                                    forbutton.addView(classroom_button);
                                    hw_parent.addView(forbutton);
                                    classroom_button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Uri uri = Uri.parse("https://moodle.cs.msu.ru/course/view.php?id=18");
                                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                            startActivity(intent);
                                        }
                                    });
                                } else {
                                    if (lesson.getSubject().equals("Алгебра и геометрия (лекция)")) {
                                        RelativeLayout forbutton = new RelativeLayout(getActivity());
                                        forbutton.setLayoutParams(small);
                                        ImageButton classroom_button = new ImageButton(getActivity());
                                        classroom_button.setImageResource(R.drawable.abstract_logo);
                                        LinearLayout.LayoutParams with_button = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dp2px(50));
                                        classroom_button.setLayoutParams(with_button);
                                        classroom_button.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                                        forbutton.addView(classroom_button);
                                        hw_parent.addView(forbutton);
                                        classroom_button.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Uri uri = Uri.parse("https://m.cs.msu.ru/index.php/s/N6FkcmFbxQkS8z9?dir=undefined&path=%2F%D0%9F%D0%B0%D0%BD%D1%84%D0%B5%D1%80%D0%BE%D0%B2%D0%92%D0%A1%2F%D0%9E%D1%81%D0%B5%D0%BD%D0%BD%D0%B8%D0%B9%20%D1%81%D0%B5%D0%BC%D0%B5%D1%81%D1%82%D1%80%202022&openfile=651410");
                                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                startActivity(intent);
                                            }
                                        });
                                    } else {
                                        if (lesson.getSubject().equals("Математический анализ (сем)")) {
                                            RelativeLayout forbutton = new RelativeLayout(getActivity());
                                            forbutton.setLayoutParams(small);
                                            ImageButton classroom_button = new ImageButton(getActivity());
                                            classroom_button.setImageResource(R.drawable.china);
                                            LinearLayout.LayoutParams with_button = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dp2px(50));
                                            classroom_button.setLayoutParams(with_button);
                                            classroom_button.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                                            forbutton.addView(classroom_button);
                                            hw_parent.addView(forbutton);
                                            classroom_button.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Uri uri = Uri.parse("https://docs.yandex.ru/docs/view?url=ya-disk%3A%2F%2F%2Fdisk%2FKitaysky_Antidemidovich_-_1_Chast.pdf&name=Kitaysky_Antidemidovich_-_1_Chast.pdf&uid=1663756742&nosw=1");
                                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                    startActivity(intent);
                                                }
                                            });
                                        }else{
                                            if (lesson.getSubject().equals("История")) {
                                                RelativeLayout forbutton = new RelativeLayout(getActivity());
                                                forbutton.setLayoutParams(small);
                                                ImageButton classroom_button = new ImageButton(getActivity());
                                                classroom_button.setImageResource(R.drawable.docx_icon);
                                                LinearLayout.LayoutParams with_button = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dp2px(50));
                                                classroom_button.setLayoutParams(with_button);
                                                classroom_button.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                                                forbutton.addView(classroom_button);
                                                hw_parent.addView(forbutton);
                                                classroom_button.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        Uri uri = Uri.parse("https://docviewer.yandex.ru/view/1663756742/?*=PxK54p8y4YU%2FtKAwz7f6S0bPte97InVybCI6InlhLWRpc2stcHVibGljOi8vS2hFcUtHT2xJckJtVHVOT28xdTRXbUR0emJ6S092ZTBRUkUzRUlXSEZ2TmdZbjZ2RXRKb2hMKzZReUZVZjFrbXEvSjZicG1SeU9Kb25UM1ZvWG5EYWc9PSIsInRpdGxlIjoi0J7RgNC70L7Qsl%2FRg9GHLtC%2F0L7RgS4uZG9jeCIsIm5vaWZyYW1lIjpmYWxzZSwidWlkIjoiMTY2Mzc1Njc0MiIsInRzIjoxNjY2NTEwMjI2OTU4LCJ5dSI6IjkyOTMxNjE4NzE2NTAwMDA0MzYifQ%3D%3D");
                                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                        startActivity(intent);
                                                    }
                                                });
                                            }else{
                                                TextView placeholder_r = new TextView(getActivity());
                                                placeholder_r.setLayoutParams(small);
                                                hw_parent.addView(placeholder_r);
                                            }
                                        }
                                    }
                                }
                            }

                            les_layout.addView(timetv);
                            les_layout.addView(sjtv);
                            les_layout.addView(roomtv);
                            les_parent.addView(les_layout);
                            les_parent.addView(hw_parent);
                            View line = new View(getActivity());
                            LinearLayout.LayoutParams lines = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(1));
                            line.setBackgroundResource(R.color.black);
                            line.setLayoutParams(lines);
                            les_parent.addView(line);
                            l.addView(les_parent);

                        }
                    }
                        i++;
                    Log.e("this day id is", String.valueOf(this_day.getId()));
                }

            if(!weekend){
                NestedScrollView nsw = getActivity().findViewById(R.id.scrollView);
                nsw.post(new Runnable() {
                    @Override
                    public void run() {
                        nsw.smoothScrollTo(0,getActivity().findViewById(Integer.parseInt("1")).getTop(),1000);
                    }
                });
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