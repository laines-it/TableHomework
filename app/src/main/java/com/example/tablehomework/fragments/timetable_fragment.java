package com.example.tablehomework.fragments;

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

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tablehomework.R;
import com.example.tablehomework.supports.Lesson;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class timetable_fragment extends Fragment {
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
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
        myRef.child("timetable").addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String[] days = {"Понедельник","Вторник","Среда","Четверг","Пятница"};
                int i = 0;
                Calendar calendar = Calendar.getInstance();
                calendar.setFirstDayOfWeek(Calendar.MONDAY);
                int today = calendar.get(Calendar.DAY_OF_WEEK) - 2;
                calendar.set(Calendar.DAY_OF_MONTH,1);
                Log.e("FIRST DAY WAS", String.valueOf(calendar.getTime()));
                LinearLayout l = getView().findViewById(R.id.parent);
                l.removeAllViews();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    TextView this_day = new TextView(getActivity());
                    String day = days[i];

                    if(i == today){
                        SpannableString context = new SpannableString(day);
                        context.setSpan(new UnderlineSpan(),0,day.length(),0);
                        this_day.setText(context);
                        this_day.setTextColor(Color.BLACK);
                    }else{
                        this_day.setText(day);
                        this_day.setTextColor(Color.GRAY);
                    }

                    this_day.setTypeface(tf_goth);
                    this_day.setTextSize(dp2px(10));
                    l.addView(this_day);
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        LinearLayout.LayoutParams forparent = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        LinearLayout les_parent = new LinearLayout(getActivity());
                        les_parent.setLayoutParams(forparent);
                        les_parent.setOrientation(LinearLayout.VERTICAL);
                        LinearLayout les_layout = new LinearLayout(getActivity());
                        les_layout.setLayoutParams(forparent);
                        les_layout.setWeightSum(10);
                        les_layout.setOrientation(LinearLayout.HORIZONTAL);
                        LinearLayout.LayoutParams small = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,4);
                        LinearLayout.LayoutParams big = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,2);
                        Lesson lesson = ds.getValue(Lesson.class);

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
                        if(lesson.getSubject().equals("Алгебра и геометрия (сем)")){
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
                        }else{
                            TextView placeholder_r = new TextView(getActivity());
                            placeholder_r.setLayoutParams(small);
                            hw_parent.addView(placeholder_r);
                        }
                        les_layout.addView(timetv);
                        les_layout.addView(sjtv);
                        les_layout.addView(roomtv);
                        les_parent.addView(les_layout);
                        les_parent.addView(hw_parent);
                        View line = new View(getActivity());
                        LinearLayout.LayoutParams lines = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,dp2px(1));
                        line.setBackgroundResource(R.color.black);
                        line.setLayoutParams(lines);
                        les_parent.addView(line);
                        l.addView(les_parent);
                    }
                    i++;
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