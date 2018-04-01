package com.saiyanstudio.gamerack.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.saiyanstudio.gamerack.common.Constants;
import com.saiyanstudio.gamerack.handlers.DatabaseHandler;
import com.saiyanstudio.gamerack.models.Game;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.io.Writer;
import java.util.List;

/**
 * Created by deekshith on 28-11-2017.
 */

public class BackupService extends IntentService {

    private Handler mHandler;;
    private DatabaseHandler db;
    private List<Game> gameList;
    private String fileName;

    public BackupService() {
        super("BackupService");
        mHandler = new Handler();
        db = new DatabaseHandler(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        fileName = intent.getStringExtra(Constants.IntentKeys.backupFileName);

        gameList = db.getAllGames();
        db.closeDB();

        String gameListString = new Gson().toJson(gameList);

        if(isExternalStorageReadable() && isExternalStorageWritable()) {
            try {

                File backupFolder = new File(Environment.getExternalStorageDirectory() + "/Game Rack/backups");

                // if folders doesn't exists, then create it
                if (!backupFolder.exists()) {
                    backupFolder.mkdirs();
                }

                File backupFile = new File(backupFolder.getAbsolutePath().toString(), fileName);

                // if file doesn't exists, then create it
                if (!backupFile.exists()) {
                    backupFile.createNewFile();
                }

                Writer output = null;
                File file = new File(backupFile.getAbsolutePath().toString());
                output = new BufferedWriter(new FileWriter(file));
                output.write(gameListString);
                output.close();

                mHandler.post(new DisplayToast(this, "Backup successful"));

            } catch (Exception e) {
                mHandler.post(new DisplayToast(this, e.getMessage()));
                //mHandler.post(new DisplayToast(this, "Something went wrong"));
            }
        } else {
            mHandler.post(new DisplayToast(this, "Can't backup without storage permission duh!"));
        }



    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    private class DisplayToast implements Runnable{
        String mText;
        Context context;

        public DisplayToast(Context context, String text){
            this.context = context;
            this.mText = text;
        }

        public void run(){
            Toast.makeText(context, mText, Toast.LENGTH_SHORT).show();
        }
    }

}
