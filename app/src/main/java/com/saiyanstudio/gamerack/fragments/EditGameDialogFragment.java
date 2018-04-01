package com.saiyanstudio.gamerack.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.saiyanstudio.gamerack.R;
import com.saiyanstudio.gamerack.common.Constants;
import com.saiyanstudio.gamerack.handlers.DatabaseHandler;
import com.saiyanstudio.gamerack.messageevents.UpdateGameMessageEvent;
import com.saiyanstudio.gamerack.models.Game;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by deekshith on 26-11-2017.
 */

public class EditGameDialogFragment extends DialogFragment {

    @BindView(R.id.spinner_platform) Spinner spinnerGamePlatform;
    @BindView(R.id.spinner_store) Spinner spinnerGameStore;
    @BindView(R.id.spinner_status) Spinner spinnerGameStatus;

    @BindView(R.id.button_save) Button save;

    Game game = null;
    private DatabaseHandler db;

    public EditGameDialogFragment(){
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {

        View viewFragment = inflater.inflate(R.layout.dialog_edit_game, container);

        ButterKnife.bind(this, viewFragment);

        db = new DatabaseHandler(getActivity());

        Bundle bundle = getArguments();
        game = bundle.getParcelable(Constants.IntentKeys.editGame);

        configureSpinners();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.setPlatform(spinnerGamePlatform.getSelectedItem().toString());
                game.setStore(spinnerGameStore.getSelectedItem().toString());
                game.setStatus(spinnerGameStatus.getSelectedItem().toString());

                // Update in db
                db.updateGame(game);
                db.closeDB();

                // Send UpdateGameMessageEvent
                EventBus.getDefault().post(new UpdateGameMessageEvent(game));

                dismiss();
            }
        });

        return viewFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()){
            @Override
            public void onBackPressed() {
                dismiss();
            }
        };
    }

    //region Private Helpers

    private void configureSpinners(){

        String[] platforms = new String[] {
                Constants.Platforms.pc,
                Constants.Platforms.nintendo,
                Constants.Platforms.playStation,
                Constants.Platforms.xbox,
                Constants.Platforms.mobile
        };
        final String[] stores = new String[] {
                Constants.Stores.steam,
                Constants.Stores.gog,
                Constants.Stores.uPlay,
                Constants.Stores.origin,
                Constants.Stores.pirated,
                Constants.Stores.other,
                Constants.Stores.store
        };
        String[] status = new String[] {
                Constants.Status.toBePlayed,
                Constants.Status.currentlyPlaying,
                Constants.Status.completed
        };

        ArrayAdapter<String> platformAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, platforms);
        spinnerGamePlatform.setAdapter(platformAdapter);

        ArrayAdapter<String> storeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, stores);
        spinnerGameStore.setAdapter(storeAdapter);

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, status);
        spinnerGameStatus.setAdapter(statusAdapter);

        spinnerGamePlatform.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    spinnerGameStore.setSelection(0); // set selected item in store to steam
                } else if (position > 0) {
                    spinnerGameStore.setSelection(stores.length - 1); // set selected item in store to store
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Set selected items in dropdown based on values from game object
        spinnerGamePlatform.setSelection(platformAdapter.getPosition(game.getPlatform()));
        spinnerGameStore.setSelection(storeAdapter.getPosition(game.getStore()));
        spinnerGameStatus.setSelection(statusAdapter.getPosition(game.getStatus()));

    }

    //endregion

}
