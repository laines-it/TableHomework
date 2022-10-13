package com.example.tablehomework;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Access {
    private Context context;

    public Access(Context context) {
        this.context = context;
    }
    public List<String> get(String path){
        List<String> pass = new ArrayList<>();
        try {
            try{InputStream is = context.getAssets().open(path);


            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;

            while ((line = reader.readLine()) != null)
                pass.add(line);
            }catch (FileNotFoundException e){
                pass.add("No");
                e.printStackTrace();
                return pass;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pass;
    }
}
