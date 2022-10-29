package com.example.tablehomework.supports;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Access {
    private final Context context;

    public Access(Context context) {
        this.context = context;
    }

    public String get(String path){
        String line;
        try {
            try{InputStream is = context.getAssets().open(path);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                if((line = reader.readLine()) != null){
                    return line;
                }
            }catch (FileNotFoundException e){
                line = "No";
                e.printStackTrace();
                return line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
