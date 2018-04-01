package com.saiyanstudio.gamerack.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.saiyanstudio.gamerack.common.Constants;
import com.saiyanstudio.gamerack.handlers.DatabaseHandler;
import com.saiyanstudio.gamerack.messageevents.GetAllGameMessageEvent;
import com.saiyanstudio.gamerack.models.Game;

import org.greenrobot.eventbus.EventBus;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by deekshith on 30-11-2017.
 */

public class RestoreService extends IntentService {

    private Handler mHandler;;
    private DatabaseHandler db;
    private List<Game> gameList;
    private String fileName;

    public RestoreService() {
        super("RestoreService");
        mHandler = new Handler();
        db = new DatabaseHandler(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        fileName = intent.getStringExtra(Constants.IntentKeys.restoreFileName);

        if(isExternalStorageReadable()) {

            try{

                Gson gson = new Gson();
                JsonReader jsonReader = new JsonReader(new FileReader(fileName));
                Type listType = new TypeToken< ArrayList<Game> >(){}.getType();
                ArrayList<Game> response = gson.fromJson(jsonReader, listType);

                if(response != null && response.size() > 0 && response.get(0).getId() > 0) {
                    db.purgeAllDataFromDb();

                    for (Game game: response) {
                        db.addGame(game);
                    }

                    mHandler.post(new DisplayToast(this, "Restore successful"));

                    Intent restartIntent = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(getBaseContext().getPackageName());
                    restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(restartIntent);

                } else {
                    mHandler.post(new DisplayToast(this, "Didn't find any data for restore"));
                }

            } catch (Exception e) {
                mHandler.post(new DisplayToast(this, e.getMessage()));
            }

        } else {
            mHandler.post(new DisplayToast(this, "Can't restore without storage permission duh!"));

        }

    }

    /* Checks if external storage is available to at least read */
    private boolean isExternalStorageReadable() {
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
