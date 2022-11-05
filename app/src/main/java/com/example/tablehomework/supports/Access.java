package com.example.tablehomework.supports;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Access {
    private final Context context;

    public Access(Context context) {
        this.context = context;
    }

    public boolean exist(String path){
        return new File("/data/data/com.example.tablehomework/files/"+path).exists();
    }
    public String get(String path){
        String line;
        Log.e("Im READING",path);
        File f = new File("/data/data/com.example.tablehomework/files/"+path);
        if (path.equals("version.txt")){rewrite(path,"1.21");}

        if(f.setReadable(true)){
            try {
                BufferedReader reader = new BufferedReader(new FileReader(f));
                line = reader.readLine();
                Log.e("LINE BLYAT",line);
                return line;
            } catch (IOException e) {
                line = "denied";
                    e.printStackTrace();
                    return line;
            }
        }
//        try {
//            try{InputStream is = context.getAssets().open(path);
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//                if((line = reader.readLine()) != null){
//                    return line;
//                }
//            }catch (FileNotFoundException e){
//                line = "denied";
//                e.printStackTrace();
//                return line;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
           return null;
    }
    public void rewrite(String path,String data) {
        try {
//
//            outputStreamWriter.write(data);
//            outputStreamWriter.close();

            File f = new File("/data/data/com.example.tablehomework/files/"+path);

            if(!f.setWritable(true)) {
                BufferedWriter br = new BufferedWriter(new FileWriter(f));
                br.write(data);
                Log.e("Im GOING TO WRITE", data);
                br.flush();
                br.close();
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
