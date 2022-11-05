package com.example.tablehomework;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tablehomework.supports.Version;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;


public class Update extends AppCompatActivity {
    Intent intent;
    DownloadManager mgr;
    TextView textpatch;
    TextView nump;
    LinearLayout parent;
    Button download;
    String link2download;
    ActivityResultLauncher<Intent> unknownAppSourceDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        parent = findViewById(R.id.for_note);

        TextView av = findViewById(R.id.av);
        TextView up = findViewById(R.id.up);
        textpatch = findViewById(R.id.text_patch);
        download = findViewById(R.id.down_butt);
        nump = findViewById(R.id.num);
        int height = getResources().getDisplayMetrics().heightPixels;
        int width = getResources().getDisplayMetrics().widthPixels;
        ObjectAnimator animator = ObjectAnimator.ofFloat(av,"translationY",-height / 3);
        animator.setDuration(2000);
        ObjectAnimator anipa = ObjectAnimator.ofFloat(textpatch,"translationX", -dp2px(300)-dp2px(16));
        anipa.setDuration(2000);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(up,"translationY", (float) (-height / 2.5));
        animator2.setDuration(2000);

        ObjectAnimator anin = ObjectAnimator.ofFloat(nump,"translationX", dp2px(200)+width*2/3);
        anin.setDuration(2000);
        ObjectAnimator aninote = ObjectAnimator.ofFloat(parent,"translationX", dp2px(700)+dp2px(16));
        aninote.setDuration(2000);
        aninote.start();
        ObjectAnimator anibutt = ObjectAnimator.ofFloat(download,"translationY", -dp2px(1000)-height/4);
        anibutt.setDuration(3300);
        anibutt.start();
        anin.start();
        animator.start();
        animator2.start();
        anipa.start();
        unknownAppSourceDialog = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        startActivity(intent);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("version");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Version version = snapshot.getValue(Version.class);
                link2download = version.getLink();
                nump.setText(version.getPatch().toString());
                parent.removeAllViews();
                for(String s: version.getNote()){
                    TextView note = new TextView(getApplicationContext());
                    note.setText(" - " + s);
                    note.setTextSize(dp2px(7));
                    parent.addView(note);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        download.setClickable(true);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                download.setClickable(false);
                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(link2download);

                Log.e("name of file",storageReference.getPath());
                File localFile = null;
                try {
                    localFile = File.createTempFile("patch","apk",getExternalCacheDir());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                TextView loading = findViewById(R.id.loading);
                loading.setText("Загрузка...");
                loading.setVisibility(View.VISIBLE);
                File finalLocalFile = localFile;
                storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        loading.setText("Успешно загружено");
                        intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        Uri apkURI = FileProvider.getUriForFile(
                                getApplicationContext(),
                                getApplicationContext()
                                        .getPackageName() + ".provider", finalLocalFile);
                        intent.setDataAndType(apkURI, "application/vnd.android.package-archive");

                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        Log.d("Lofting", "About to install new .apk");

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                                && !getPackageManager().canRequestPackageInstalls()) {
                            Intent unknownAppSourceIntent = new Intent()
                                    .setAction(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
                                    .setData(Uri.parse(String.format("package:%s", getPackageName())));
                            unknownAppSourceDialog.launch(unknownAppSourceIntent);
                        } else {
                            // App already have the permission to install so launch the APK installation.
                            startActivity(intent);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e("HELLO BLYAT","exception");
                        exception.printStackTrace();
                    }
                });
            }
        });

    }


    private int dp2px(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}