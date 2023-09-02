package com.example.tablehomework

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.tablehomework.R
import android.widget.LinearLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.tablehomework.supports.Chat
import java.util.*

class ChatActivity : AppCompatActivity() {
    private val ref = FirebaseDatabase.getInstance().reference.child("social").child("chats")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        val grandparent = findViewById<LinearLayout>(R.id.chats_parent)
        val store = FirebaseFirestore.getInstance()
        val preferences =
            applicationContext.getSharedPreferences("com.example.tablehomework", MODE_PRIVATE)
        Log.e("OK", "Let's do it")
        store.collection("chats").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (chat in task.result) {
                    val height = resources.displayMetrics.heightPixels
                    val parent = LinearLayout(this@ChatActivity)
                    val lp =
                        LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height / 10)
                    parent.weightSum = 70f
                    parent.orientation = LinearLayout.HORIZONTAL
                    parent.layoutParams = lp
                    val icon = ImageView(this@ChatActivity)
                    val foricon = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 60f
                    )
                    foricon.setMargins(dp2px(5), dp2px(10), dp2px(5), dp2px(10))
                    icon.setImageResource(R.drawable.china)
                    icon.layoutParams = foricon
                    icon.scaleType = ImageView.ScaleType.CENTER_INSIDE
                    val t = TextView(this@ChatActivity)
                    val fortitle = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,
                        10F
                    )
                    t.layoutParams = fortitle
                    t.text = chat.id
                    t.setTypeface(resources.getFont(R.font.gothic))
                    parent.addView(icon)
                    parent.addView(t)
                    grandparent.addView(parent)
                    val line = View(this@ChatActivity)
                    val lines = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, dp2px(1)
                    )
                    line.setBackgroundResource(R.color.black)
                    line.layoutParams = lines
                    grandparent.addView(line)
                    Log.e(chat.id + "=>", chat.data["lastmessage"].toString())
                }
            } else {
                Log.e("Error ", "with getting docs: ", task.exception)
            }
        }
        val button = findViewById<Button>(R.id.add_new_chat)
        button.setOnClickListener {
            val id = Random().nextInt()
            val chat = Chat(
                "New chat",
                preferences.getString("enter", "everyone"),
                id
            )
            ref.child(id.toString()).setValue(chat)
        }
    }

    private fun dp2px(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
}