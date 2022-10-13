package com.example.tablehomework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

public class EnterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        Access access = new Access(this);
        StringBuilder key = new StringBuilder();
        for(String s : access.get("enter")){
            key.append(s);

        }
        Log.e("ACCESS TO", String.valueOf(key));
        if (key.toString().equals("No")){
            TextView t = findViewById(R.id.textView);
            t.setText("NO");
        }else {
            Intent intent = new Intent(EnterActivity.this, MainActivity.class);
            intent.putExtra("access", key.toString());
            startActivity(intent);
        }
    }
}