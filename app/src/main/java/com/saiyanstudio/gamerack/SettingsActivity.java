package com.saiyanstudio.gamerack;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v13.app.FragmentCompat;
import android.support.v4.app.ActivityCompat;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;


import com.saiyanstudio.gamerack.common.Constants;
import com.saiyanstudio.gamerack.handlers.DatabaseHandler;
import com.saiyanstudio.gamerack.services.BackupService;
import com.saiyanstudio.gamerack.services.RestoreService;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class SettingsActivity extends AppCompatPreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");

        // load settings fragment
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public static class MainPreferenceFragment extends PreferenceFragment {

        private DatabaseHandler db;

        // Request Codes
        final int REQUEST_CODE_PICK_FILE = 1;

        // Storage Permissions
        final int REQUEST_EXTERNAL_STORAGE_FOR_BACKUP = 1;
        final int REQUEST_EXTERNAL_STORAGE_FOR_RESTORE = 2;

        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        private String backupFileName;
        private String restoreFileName;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);

            db = new DatabaseHandler(getActivity());

            // Backup EditText change listener
            EditTextPreference backupPref = (EditTextPreference) findPreference(getString(R.string.prefs_key_backup));
            backupPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm").format(Calendar.getInstance().getTime());
                    EditText editText = ((EditTextPreference) preference).getEditText();
                    editText.setText("backup_" + timeStamp + ".json");
                    editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                    editText.setSelection(editText.getText().length());
                    return true;
                }
            });
            backupPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                    backupFileName = ((EditTextPreference) preference).getEditText().getText().toString();

                    // Verify Storage Permissions
                    // Check if we have write permission
                    int permission = ActivityCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);

                    if (permission != PackageManager.PERMISSION_GRANTED) {
                        // We don't have permission so prompt the user
                        FragmentCompat.requestPermissions(
                                MainPreferenceFragment.this,
                                PERMISSIONS_STORAGE,
                                REQUEST_EXTERNAL_STORAGE_FOR_BACKUP
                        );
                    } else {
                        callBackupService();
                    }

                    return true;
                }
            });

            // Restore Preference change listener
            Preference restorePref = findPreference(getString(R.string.prefs_key_restore));
            restorePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("application/json");
                    startActivityForResult(intent, REQUEST_CODE_PICK_FILE);

                    return true;
                }
            });


            // Remove Ads preference click listener
            Preference removeAdsPref = findPreference(getString(R.string.prefs_key_remove_ads));
            removeAdsPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    return false;
                }
            });


            // Rate the app preference click listener
            Preference rateTheAppPref = findPreference(getString(R.string.prefs_key_rate_app));
            rateTheAppPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                    Intent goToPlayStore = new Intent(Intent.ACTION_VIEW, uri);
                    try {
                        startActivity(goToPlayStore);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
                    }
                    return true;
                }
            });

            // Feedback preference click listener
            Preference feedbackPref = findPreference(getString(R.string.prefs_key_send_feedback));
            feedbackPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    sendFeedback(getActivity());
                    return true;
                }
            });
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQUEST_CODE_PICK_FILE
                    && resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();

                if (uri != null) {
                    restoreFileName = uri.getPath();
                    callRestoreService(restoreFileName);
                } else {
                    Toast.makeText(getActivity(), "Somethings wrong with the file", Toast.LENGTH_LONG).show();
                }

            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

            switch (requestCode) {
                case REQUEST_EXTERNAL_STORAGE_FOR_BACKUP:
                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        callBackupService();
                    else
                        Toast.makeText(getActivity(), "Can't backup without storage permission duh!", Toast.LENGTH_SHORT).show();
                    break;

                case REQUEST_EXTERNAL_STORAGE_FOR_RESTORE:
                    break;
            }

            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        public void callBackupService() {
            Toast.makeText(getActivity(), "Backing up", Toast.LENGTH_LONG).show();
            Intent serviceIntent = new Intent(getActivity(), BackupService.class);
            serviceIntent.putExtra(Constants.IntentKeys.backupFileName, backupFileName);
            getActivity().startService(serviceIntent);
        }

        public void callRestoreService(String restoreFileName) {
            Toast.makeText(getActivity(), "Restoring", Toast.LENGTH_LONG).show();
            Intent serviceIntent = new Intent(getActivity(), RestoreService.class);
            serviceIntent.putExtra(Constants.IntentKeys.restoreFileName, restoreFileName);
            getActivity().startService(serviceIntent);
        }

    }

    public static void sendFeedback(Context context) {
        String body = null;
        try {
            body = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            body = "\n\n-----------------------------\nPlease don't remove this information\n Device OS: Android \n Device OS version: " +
                    Build.VERSION.RELEASE + "\n App Version: " + body + "\n Device Brand: " + Build.BRAND +
                    "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER;
        } catch (PackageManager.NameNotFoundException e) {
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{ "feedback.saiyanstudio@gmail.com" });
        intent.putExtra(Intent.EXTRA_SUBJECT, "Query on Game Rack");
        intent.putExtra(Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.choose_email_client)));
    }
}
