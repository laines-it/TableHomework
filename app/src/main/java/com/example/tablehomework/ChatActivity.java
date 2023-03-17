package com.example.tablehomework;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tablehomework.supports.Chat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.C;

import java.util.Random;

public class ChatActivity extends AppCompatActivity {
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("social").child("chats");
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        LinearLayout grandparent = findViewById(R.id.chats_parent);
        FirebaseFirestore store = FirebaseFirestore.getInstance();
        preferences = getApplicationContext().getSharedPreferences("com.example.tablehomework",MODE_PRIVATE);
        Log.e("OK","Let's do it");
        store.collection("chats").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
              @Override
              public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot chat : task.getResult()){
                        int height = getResources().getDisplayMetrics().heightPixels;
                        LinearLayout parent = new LinearLayout(ChatActivity.this);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height/10);
                        parent.setWeightSum(70);
                        parent.setOrientation(LinearLayout.HORIZONTAL);
                        parent.setLayoutParams(lp);
                        ImageView icon = new ImageView(ChatActivity.this);
                        LinearLayout.LayoutParams foricon = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT,60);
                        foricon.setMargins(dp2px(5),dp2px(10),dp2px(5),dp2px(10));
                        icon.setImageResource(R.drawable.china);
                        icon.setLayoutParams(foricon);
                        icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        TextView t = new TextView(ChatActivity.this);
                        LinearLayout.LayoutParams fortitle = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT,10);
                        t.setLayoutParams(fortitle);
                        t.setText(chat.getId());
                        t.setTypeface(getResources().getFont(R.font.gothic));
                        parent.addView(icon);
                        parent.addView(t);
                        grandparent.addView(parent);
                        View line = new View(ChatActivity.this);
                        LinearLayout.LayoutParams lines = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT, dp2px(1));
                        line.setBackgroundResource(R.color.black);
                        line.setLayoutParams(lines);
                        grandparent.addView(line);
                        Log.e(chat.getId() + "=>", String.valueOf(chat.getData().get("lastmessage")));
                    }
                }else{
                    Log.e("Error ","with getting docs: ",task.getException());
                }
              }
          });
        Button button = findViewById(R.id.add_new_chat);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = new Random().nextInt();
                Chat chat = new Chat("New chat",
                        preferences.getString("enter","everyone"),
                        id);
                ref.child(String.valueOf(id)).setValue(chat);
            }
        });
    }
    private int dp2px(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
}