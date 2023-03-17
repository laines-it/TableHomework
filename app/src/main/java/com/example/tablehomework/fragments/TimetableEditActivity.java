package com.example.tablehomework.fragments;

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

public class TimetableEditActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private SharedPreferences preferences;
    protected ArrayList<PlaceInTimetable> edited = new ArrayList<>();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_timetable);
        database = FirebaseDatabase.getInstance();
        preferences = getApplicationContext().getSharedPreferences("com.example.tablehomework", Context.MODE_PRIVATE);
        myRef = database.getReference().child("testtable").child(preferences.getString("enter","default"));
        if(preferences.getString("remove_splash","null").equals("null")){
            DialogFragment df = new splash_fragment();
            df.show(getSupportFragmentManager(),"what");
        }

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
                for (DataSnapshot dataSnapshot : snapshot.child("timetable").getChildren()) {
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
                        Lesson lesson = ds.getValue(Lesson.class);
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


                        EditText timetv = new EditText(getActivity());
                        timetv.setLayoutParams(small);
                        timetv.setTextSize(dp2px(8));
                        timetv.setText(lesson.getTime());


                        TextView sjtv = new TextView(getActivity());
                        sjtv.setTextSize(dp2px(9));
                        sjtv.setLayoutParams(big);
                        sjtv.setText(lesson.getSubject());
                        sjtv.setGravity(Gravity.CENTER);

                        EditText roomtv = new EditText(getActivity());
                        roomtv.setLayoutParams(small);
                        roomtv.setTextSize(dp2px(8));
                        roomtv.setText(lesson.getRoom());
                        roomtv.setGravity(Gravity.CENTER);

                        int finalI = i+1;
                        int finalJ = j;
                        timetv.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void afterTextChanged(Editable editable){
                                lesson.setTime(editable.toString());
                                addEdited(lesson, finalI, finalJ);
                            }
                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                        });
                        roomtv.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void afterTextChanged(Editable editable){
                                lesson.setRoom(editable.toString());
                                addEdited(lesson, finalI, finalJ);
                            }
                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                        });

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
                        LinkFromLesson link = snapshot.child("link").child(lesson.getSubject()).getValue(LinkFromLesson.class);
                        if(link != null && !link.getLink().equals("")) {
                            LinearLayout forbutton = new LinearLayout(getActivity());
                            forbutton.setOrientation(LinearLayout.HORIZONTAL);
                            forbutton.setLayoutParams(small);
                            ImageButton image_button = new ImageButton(getActivity());
                            try {
                                Picasso.get().load(link.getImage()).into(image_button);
                            } catch (Exception e) {
                                Log.e("CANNOT LOAD IMAGE", link.getImage());
                                image_button.setImageResource(R.drawable.empty);
                            }
                            forbutton.setWeightSum(2);
                            LinearLayout.LayoutParams with_button   = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(50),1);
                            image_button.setLayoutParams(with_button);
                            image_button.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

                            ImageButton change = new ImageButton(getActivity());
                            change.setImageResource(android.R.drawable.ic_menu_edit);
                            change.setOnClickListener(view -> {
                                DialogFragment d = new add_link_dialog();
                                Bundle a = new Bundle();
                                a.putString("SUBJECTNAME",lesson.getSubject());
                                d.setArguments(a);
                                d.show(getSupportFragmentManager(),"what");
                            });
                            change.setLayoutParams(with_button);

                            forbutton.addView(image_button);
                            forbutton.addView(change);
                            hw_parent.addView(forbutton);
                            image_button.setOnClickListener(view1 -> {
                                Uri uri = Uri.parse(link.getLink());
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                try{
                                    startActivity(intent);
                                } catch (ActivityNotFoundException e) {
                                    Toast.makeText(getActivity(),"Invalid link",Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            ImageButton image_button = new ImageButton(getActivity());
                            image_button.setImageResource(android.R.drawable.ic_input_add);
                            image_button.setOnClickListener(view -> {
                                DialogFragment d = new add_link_dialog();
                                Bundle a = new Bundle();
                                a.putString("SUBJECTNAME",lesson.getSubject());
                                d.setArguments(a);
                                d.show(getSupportFragmentManager(),"what");
                            });
                            image_button.setLayoutParams(small);
                            hw_parent.addView(image_button);
                        }
                        if (System.currentTimeMillis() > lesson.getWhen()){hwtv.setText("");}
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
                SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences("com.example.tablehomework", Context.MODE_PRIVATE);
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference ref = db.getReference().child("testtable").child(preferences.getString("enter","default")).child("timetable");
                for(PlaceInTimetable p : edited){
                    String path = "testtable/" + preferences.getString("enter","default") + "/timetable/" + p.getDay() + "/" + p.getNumber() + "/";
                    db.getReference(path).setValue(p.getLesson());
                }
                Intent intent_v = new Intent(TimetableEditActivity.this, MainActivity.class);
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
    private void addEdited(Lesson lesson, int d, int n){
        PlaceInTimetable p = new PlaceInTimetable(lesson,d,n);
        boolean none = true;
        if(!edited.isEmpty()){
            for(PlaceInTimetable pit : edited) {
                if (pit.getDay() == d && pit.getNumber() == n) {
                    none = false;
                    break;
                }
            }
        }
        if(none){edited.add(p);}
    }
}