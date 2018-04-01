package com.saiyanstudio.gamerack.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyanstudio.gamerack.R;
import com.saiyanstudio.gamerack.common.Constants;
import com.saiyanstudio.gamerack.common.Utility;
import com.saiyanstudio.gamerack.handlers.DatabaseHandler;
import com.saiyanstudio.gamerack.models.Game;
import com.saiyanstudio.gamerack.models.Info;
import com.saiyanstudio.gamerack.retrofit.RetrofitClient;
import com.saiyanstudio.gamerack.services.IGDBBackgroundService;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by deekshith on 13-11-2017.
 */

public class GameInfoDialogFragment extends DialogFragment {

    @BindView(R.id.scrollView_game_info) ScrollView scrollViewGameInfo;
    @BindView(R.id.layout_progress_bar) FrameLayout progressBarLayout;
    @BindView(R.id.layout_footer) RelativeLayout dialogButtonsLayout;

    @BindView(R.id.imageView_game) ImageView gameImage;
    @BindView(R.id.textView_name) TextView gameName;
    @BindView(R.id.textView_developer) TextView gameDeveloper;
    @BindView(R.id.textView_release_date) TextView gameReleaseDate;
    @BindView(R.id.textView_description) TextView gameDescription;
    @BindView(R.id.spinner_platform) Spinner spinnerGamePlatform;
    @BindView(R.id.spinner_store) Spinner spinnerGameStore;
    @BindView(R.id.spinner_status) Spinner spinnerGameStatus;

    @BindView(R.id.button_add) Button add;
    @BindView(R.id.button_dismiss) Button dismiss;

    Game game = null;
    private DatabaseHandler db;

    public GameInfoDialogFragment(){
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {

        View viewFragment = inflater.inflate(R.layout.dialog_game_info, container);

        ButterKnife.bind(this, viewFragment);

        db = new DatabaseHandler(getActivity());

        configureSpinners();

        Bundle bundle = getArguments();
        game = bundle.getParcelable(Constants.IntentKeys.addGame);

        callApisAndBindData(game.getId());

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                game.setPlatform(spinnerGamePlatform.getSelectedItem().toString());
                game.setStore(spinnerGameStore.getSelectedItem().toString());
                game.setStatus(spinnerGameStatus.getSelectedItem().toString());

                Utility.showSnackBar(getActivity(), getActivity().findViewById(R.id.recycler_view), getString(R.string.msg_will_be_added_to_library));

                Intent serviceIntent = new Intent(getActivity(), IGDBBackgroundService.class);
                serviceIntent.putExtra(Constants.IntentKeys.game, game);
                getActivity().startService(serviceIntent);

                dismiss();

            }
        });

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    }

    private void prepareGameData(){

        Game existingGame = db.getGame(game.getId());
        db.closeDB();

        if(existingGame != null){
            add.setText("IN LIBRARY");
            add.setEnabled(false);
            add.setTextColor(getActivity().getResources().getColor(R.color.text_color_button_disabled));
        }

        if(game.getCover() != null && game.getCover().getCloudinaryId() != null && !game.getCover().getCloudinaryId().isEmpty()) {
            String imageUrl = String.format(Constants.IGDBApi.coverBigImageBaseUrl, game.getCover().getCloudinaryId());
            Picasso.with(getActivity())
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_image_grey_24dp)
                    .into(gameImage);
        }

        if(game.getName() != null && !game.getName().isEmpty()) {
            gameName.setText(game.getName());
        } else {
            gameName.setText(Constants.Generic.notAvailable);
        }

        if(!game.getDevelopersInfoString().isEmpty()) {
            gameDeveloper.setText(game.getDevelopersInfoString());
        } else {
            gameDeveloper.setText(Constants.Generic.notAvailable);
        }

        if(game.getFirstReleaseDate() > 0) {
            gameReleaseDate.setText(Utility.epochTimeToYear(game.getFirstReleaseDate()));
        } else {
            gameReleaseDate.setText(Constants.Generic.notAvailable);
        }

        if(game.getSummary() != null && !game.getSummary().isEmpty()) {
            gameDescription.setText(game.getSummary().split("\n")[0]); // set only one paragraph as description
        } else {
            gameDescription.setText(Constants.Generic.notAvailable);
        }

        progressBarLayout.setVisibility(View.GONE);

        // Set the info layouts to visible
        scrollViewGameInfo.setVisibility(View.VISIBLE);

        ViewGroup.LayoutParams layoutParams = dialogButtonsLayout.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;;
        dialogButtonsLayout.setLayoutParams(layoutParams);
    }

    private void callApisAndBindData(int gameId) {

        Call<List<Game>> getGameCall
                = RetrofitClient.getIgdbApiService(getActivity()).getGameInfoById(gameId);

        getGameCall.enqueue(new Callback<List<Game>>() {
            @Override
            public void onResponse(Call<List<Game>> call, Response<List<Game>> response) {

                if(response.body().size() > 0) {
                    game = response.body().get(0);
                    callDevelopersInfoApi(game.getDevelopers());

                } else {
                    dismiss();
                    Toast.makeText(getActivity(), "No game", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Game>> call, Throwable t) {
                dismiss();
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callDevelopersInfoApi(List<Integer> developers){

        if(developers != null && developers.size() > 0) {

            Call<List<Info>> getDevelopersInfoCall
                    = RetrofitClient.getIgdbApiService(getActivity()).getDeveloperInfoByIds(developers.get(0).toString());

            getDevelopersInfoCall.enqueue(new Callback<List<Info>>() {
                @Override
                public void onResponse(Call<List<Info>> call, Response<List<Info>> response) {
                    if(response.body().size() > 0) {
                        game.setDevelopersInfo(response.body());
                    }
                    prepareGameData();
                }

                @Override
                public void onFailure(Call<List<Info>> call, Throwable t) {
                    dismiss();
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            prepareGameData();
        }
    }

    //endregion

}