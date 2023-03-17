package com.example.tablehomework;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tablehomework.MainActivity;
import com.example.tablehomework.R;
import com.example.tablehomework.supports.Lesson;
import com.example.tablehomework.supports.LinkFromLesson;
import com.example.tablehomework.supports.MovableFloatingActionButton;
import com.example.tablehomework.supports.PlaceInTimetable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Freerooms extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private SharedPreferences preferences;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_timetable);
        database = FirebaseDatabase.getInstance();
        preferences = getApplicationContext().getSharedPreferences("com.example.tablehomework", Context.MODE_PRIVATE);
        myRef = database.getReference().child("rooms");

        Typeface tf_gill = getResources().getFont(R.font.gillsans);
        Typeface tf_goth = getResources().getFont(R.font.gothic);
        myRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String[] days = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница","Суббота"};

                Calendar calendar = Calendar.getInstance();
                calendar.setFirstDayOfWeek(Calendar.MONDAY);
                int today = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                boolean weekend = (1>today || today>6);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                LinearLayout l = findViewById(R.id.parent);
                l.removeAllViews();
                //to watch how text in edittext changes
                int j = 0;
                int i = 0;
                TextView gotoday = new TextView(getActivity());
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TextView this_day = new TextView(getActivity());
                    String day = days[i];
                    if (i+1 == today) {
                        gotoday = this_day;
                        SpannableString context = new SpannableString(day);
                        context.setSpan(new UnderlineSpan(), 0, day.length(), 0);
                        this_day.setText(context);
                        this_day.setId(Integer.parseInt("1"));
                        this_day.setTextColor(Color.BLACK);
                    } else {
                        this_day.setText(day);
                        this_day.setTextColor(Color.GRAY);
                    }

                    this_day.setTypeface(tf_goth);
                    this_day.setTextSize(dp2px(10));
                    l.addView(this_day);
                    j = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
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
                        LinearLayout.LayoutParams lines = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(1));

                        TextView timetv = new TextView(getActivity());
                        timetv.setLayoutParams(small);
                        timetv.setTextSize(dp2px(8));
                        timetv.setText(ds.child("time").getValue(String.class));

                        LinearLayout forfloor = new LinearLayout(getActivity());
                        forfloor.setLayoutParams(big);
                        forfloor.setOrientation(LinearLayout.VERTICAL);
                        LinearLayout.LayoutParams r = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        TextView five = new TextView(getActivity());
                        five.setTextSize(dp2px(9));
                        five.setLayoutParams(r);
                        five.setText(ds.child("0").getValue(String.class));
                        forfloor.addView(five);
                        View line1 = new View(getActivity());
                        line1.setBackgroundResource(R.color.black);
                        line1.setLayoutParams(lines);
                        forfloor.addView(line1);
                        TextView six = new TextView(getActivity());
                        six.setTextSize(dp2px(9));
                        six.setLayoutParams(r);
                        six.setText(ds.child("1").getValue(String.class));
                        forfloor.addView(six);
                        View line2 = new View(getActivity());
                        line2.setBackgroundResource(R.color.black);
                        line2.setLayoutParams(lines);
                        forfloor.addView(line2);
                        TextView seven = new TextView(getActivity());
                        seven.setTextSize(dp2px(9));
                        seven.setLayoutParams(r);
                        seven.setText(ds.child("2").getValue(String.class));
                        forfloor.addView(seven);

                        TextView roomtv = new TextView(getActivity());
                        roomtv.setLayoutParams(small);
                        roomtv.setTextSize(dp2px(8));

                        les_layout.addView(timetv);
                        les_layout.addView(forfloor);
                        les_layout.addView(roomtv);
                        les_parent.addView(les_layout);

                        View line = new View(getActivity());
                        line.setBackgroundResource(R.color.black);
                        line.setLayoutParams(lines);
                        les_parent.addView(line);
                        l.addView(les_parent);
                        j++;
                    }
                    i++;

                }

                if(!weekend){
                    NestedScrollView nsw = findViewById(R.id.scrollView);
                    nsw.post(new Runnable() {
                        @Override
                        public void run() {
                            nsw.smoothScrollTo(0,findViewById(Integer.parseInt("1")).getTop(),1000);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        MovableFloatingActionButton mfab = findViewById(R.id.accept_edit);
        mfab.setVisibility(View.VISIBLE);
        mfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_v = new Intent(Freerooms.this, MainActivity.class);
                startActivity(intent_v);
            }
        });
    }
    private Context getActivity(){
        return getApplicationContext();
    }
    private int dp2px(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

}