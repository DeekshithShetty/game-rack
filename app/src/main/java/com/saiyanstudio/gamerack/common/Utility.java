package com.saiyanstudio.gamerack.common;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.saiyanstudio.gamerack.R;
import com.saiyanstudio.gamerack.models.Info;

import java.net.InetAddress;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



/**
 * Created by deekshith on 12-11-2017.
 */

public class Utility {

    public static void showSnackBar(Context context, View view, String text) {
        if(context != null && view != null && view.getParent() != null) {
            Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.background_snackbar));
            snackbar.show();
        }
    }

    public static List<String> getNamePropertyList(List<Info> list) {
        List<String> result = new ArrayList<String>();
        for (Info info : list) {
            result.add(info.getName());
        }
        return result;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void showSoftKeyboard(Activity activity) {
        ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static String epochTimeToDate(long epochTime) {
        if(epochTime > 0){
            Date date = new Date(epochTime * 1000);
            DateFormat format = new SimpleDateFormat("MMM d, yyyy");
            return format.format(date);
        } else {
            return "";
        }
    }

    public static String epochTimeToYear(long epochTime) {
        if(epochTime > 0){
            Date date = new Date(epochTime * 1000);
            DateFormat format = new SimpleDateFormat("yyyy");
            return format.format(date);
        } else {
            return "";
        }
    }

    public static String secondsToHours(int seconds) {
        double hours = seconds / 3600;
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(hours);
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }

    }
}