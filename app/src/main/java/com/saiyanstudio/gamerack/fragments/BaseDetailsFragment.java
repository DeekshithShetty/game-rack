package com.saiyanstudio.gamerack.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.saiyanstudio.gamerack.FullscreenImageActivity;
import com.saiyanstudio.gamerack.R;
import com.saiyanstudio.gamerack.common.Constants;
import com.saiyanstudio.gamerack.common.Utility;
import com.saiyanstudio.gamerack.messageevents.UpdateGameMessageEvent;
import com.saiyanstudio.gamerack.models.Game;
import com.saiyanstudio.gamerack.models.Website;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by deekshith on 05-11-2017.
 */

public class BaseDetailsFragment extends Fragment {

    @BindView(R.id.imageView_game) ImageView gameImage;
    @BindView(R.id.imageView_backgroundImage) ImageView backgroundImage;
    @BindView(R.id.textView_game_status) TextView gameStatus;
    @BindView(R.id.textView_game_platform_store) TextView gamePlatformStore;
    @BindView(R.id.textView_igdb_ratings) TextView igdbRatings;
    @BindView(R.id.textView_metacritic_ratings) TextView criticRatings;
    @BindView(R.id.textView_ign_ratings) TextView ignRatings;
    @BindView(R.id.textView_description) TextView description;
    @BindView(R.id.textView_release_date) TextView releaseDate;
    @BindView(R.id.textView_developer) TextView developer;
    @BindView(R.id.textView_genres_tags) TextView genre;
    @BindView(R.id.textView_gameplay_time) TextView gameplayTime;
    @BindView(R.id.textView_content_rating) TextView contentRating;

    @BindView(R.id.layout_external_links) ConstraintLayout externalLinksLayout;
    @BindView(R.id.layout_link_official_site) LinearLayout officialSiteLayout;
    @BindView(R.id.layout_link_steam) LinearLayout steamLayout;
    @BindView(R.id.layout_link_twitch) LinearLayout twitchLayout;
    @BindView(R.id.layout_link_wikia) LinearLayout wikiaLayout;
    @BindView(R.id.textView_link_official_site) TextView officialSiteLink;
    @BindView(R.id.textView_link_steam) TextView steamLink;
    @BindView(R.id.textView_link_twitch) TextView twitchLink;
    @BindView(R.id.textView_link_wikia) TextView wikiaLink;

    @BindView(R.id.adView1) AdView adView1;
    @BindView(R.id.adView2) AdView adView2;
    @BindView(R.id.adView3) AdView adView3;

    private Game baseGame;

    public BaseDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_base_details, container, false);

        ButterKnife.bind(this, fragmentView);

        // Load ads
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(Constants.AdMobs.testDeviceId)
                .build();

        adView1.loadAd(adRequest);
        adView1.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                adView1.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                adView1.setVisibility(View.GONE);
            }
        });
        adView2.loadAd(adRequest);
        adView2.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                adView2.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                adView2.setVisibility(View.GONE);
            }
        });
        adView3.loadAd(adRequest);
        adView3.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                adView3.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                adView3.setVisibility(View.GONE);
            }
        });

        Bundle bundle = getArguments();
        baseGame = bundle.getParcelable(Constants.IntentKeys.baseGame);

        if(baseGame.getCover() != null && baseGame.getCover().getCloudinaryId() != null && !baseGame.getCover().getCloudinaryId().isEmpty()){
            String imageUrl = String.format(Constants.IGDBApi.coverBigImageBaseUrl, baseGame.getCover().getCloudinaryId());
            Picasso.with(getActivity())
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_image_grey_24dp)
                    .fit()
                    .into(gameImage);
            Picasso.with(getActivity())
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_image_grey_24dp)
                    .into(backgroundImage);
        }

        gameImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FullscreenImageActivity.class);

                if(baseGame.getCover() != null && baseGame.getCover().getCloudinaryId() != null && !baseGame.getCover().getCloudinaryId().isEmpty())
                    intent.putExtra(Constants.IntentKeys.cloudinaryId, baseGame.getCover().getCloudinaryId());
                getActivity().startActivity(intent);
            }
        });

        if(baseGame.getStatus() != null && !baseGame.getStatus().isEmpty()) {
            if(baseGame.getStatus().equalsIgnoreCase(Constants.Status.currentlyPlaying)){
                gameStatus.setTextColor(getActivity().getResources().getColor(R.color.colorAccent));
            }
            gameStatus.setText(baseGame.getStatus());
        } else {
            gameStatus.setText(Constants.Status.toBePlayed);
        }

        if(baseGame.getPlatform() != null && !baseGame.getPlatform().isEmpty() &&
                baseGame.getStore() != null && !baseGame.getStore().isEmpty()) {
            gamePlatformStore.setText(String.format(Constants.PlatformAndStore.platformAndStore, baseGame.getPlatform(), baseGame.getStore()));
        } else {
            gamePlatformStore.setText(Constants.Generic.notAvailable);
        }

        if(baseGame.getRating() > 0) {
            igdbRatings.setText((int)baseGame.getRating() + "");
        } else {
            igdbRatings.setText(Constants.Generic.notAvailable);
        }

        if(baseGame.getAggregatedRating() > 0) {
            criticRatings.setText((int)baseGame.getAggregatedRating() + "");
        } else {
            criticRatings.setText(Constants.Generic.notAvailable);
        }

        if(baseGame.getIgnRatings() != null && !baseGame.getIgnRatings().isEmpty()) {
            ignRatings.setText(baseGame.getIgnRatings());
        } else {
            ignRatings.setText(Constants.Generic.notAvailable);
        }

        if(baseGame.getSummary() != null && !baseGame.getSummary().isEmpty()) {
            description.setText(baseGame.getSummary());
        } else {
            description.setText(Constants.Generic.notAvailable);
        }

        if(baseGame.getDevelopersInfoString() != null && !baseGame.getDevelopersInfoString().isEmpty()) {
            developer.setText(baseGame.getDevelopersInfoString());
        } else {
            developer.setText(Constants.Generic.notAvailable);
        }

        if(baseGame.getFirstReleaseDate() > 0) {
            releaseDate.setText(Utility.epochTimeToYear(baseGame.getFirstReleaseDate()));
        } else {
            releaseDate.setText(Constants.Generic.notAvailable);
        }

        if(baseGame.getGenresAndTagsString() != null && !baseGame.getGenresAndTagsString().isEmpty()) {
            genre.setText(baseGame.getGenresAndTagsString());
        } else {
            genre.setText(Constants.Generic.notAvailable);
        }

        if(baseGame.getTimeToBeat() != null && baseGame.getTimeToBeat().getNormally() > 0) {
            gameplayTime.setText(Utility.secondsToHours(baseGame.getTimeToBeat().getNormally()) + " hrs");
        } else {
            gameplayTime.setText(Constants.Generic.notAvailable);
        }

        if(baseGame.getEsrb() != null && baseGame.getPegi() != null) {
            contentRating.setText(String.format(Constants.ContentRating.esrbAndPegi, baseGame.getEsrb().getRating(), baseGame.getPegi().getRating()));
        } else if (baseGame.getEsrb() != null) {
            contentRating.setText(String.format(Constants.ContentRating.esrb, baseGame.getEsrb().getRating()));
        } else if (baseGame.getEsrb() != null) {
            contentRating.setText(String.format(Constants.ContentRating.pegi, baseGame.getPegi().getRating()));
        } else {
            contentRating.setText(Constants.Generic.notAvailable);
        }

        List<Website> websiteList = baseGame.getWebsites();
        if(websiteList != null && websiteList.size() > 0){

            for (final Website website: websiteList) {
                if(website.getCategory() == Constants.WebSiteCategory.OfficialSite){ // Official

                    officialSiteLayout.setVisibility(View.VISIBLE);
                    officialSiteLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openBrowser(website.getUrl());
                        }
                    });

                } else if(website.getCategory() == Constants.WebSiteCategory.Steam) { // Steam

                    steamLayout.setVisibility(View.VISIBLE);
                    steamLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openBrowser(website.getUrl());
                        }
                    });

                } else if(website.getCategory() == Constants.WebSiteCategory.Twitch){ // Twitch

                    twitchLayout.setVisibility(View.VISIBLE);
                    twitchLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openBrowser(website.getUrl());
                        }
                    });

                } else if(website.getCategory() == Constants.WebSiteCategory.Wikia){ // Wikia

                    wikiaLayout.setVisibility(View.VISIBLE);
                    wikiaLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openBrowser(website.getUrl());
                        }
                    });

                }
            }
        } else {
            externalLinksLayout.setVisibility(View.GONE);
        }

        return fragmentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onPause() {
        if (adView1 != null) {
            adView1.pause();
        }
        if (adView2 != null) {
            adView2.pause();
        }
        if (adView3 != null) {
            adView3.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView1 != null) {
            adView1.resume();
        }
        if (adView2 != null) {
            adView2.pause();
        }
        if (adView3 != null) {
            adView3.pause();
        }
    }

    @Override
    public void onDestroy() {
        if (adView1 != null) {
            adView1.destroy();
        }
        if (adView2 != null) {
            adView2.pause();
        }
        if (adView3 != null) {
            adView3.pause();
        }
        super.onDestroy();
    }

    //region Event Handlers

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateGameMessageEvent(UpdateGameMessageEvent event) {
        Game updatedGame = event.getGame();

        baseGame.setPlatform(updatedGame.getPlatform());
        baseGame.setStore(updatedGame.getStore());
        baseGame.setStatus(updatedGame.getStatus());

        if(baseGame.getStatus() != null && !baseGame.getStatus().isEmpty()) {
            if(baseGame.getStatus().equalsIgnoreCase(Constants.Status.currentlyPlaying)){
                gameStatus.setTextColor(getActivity().getResources().getColor(R.color.colorAccent));
            } else {
                gameStatus.setTextColor(getActivity().getResources().getColor(R.color.text_color_text_view_details_game_status));
            }
            gameStatus.setText(baseGame.getStatus());
        } else {
            gameStatus.setText(Constants.Status.toBePlayed);
        }

        if(baseGame.getPlatform() != null && !baseGame.getPlatform().isEmpty() &&
                baseGame.getStore() != null && !baseGame.getStore().isEmpty()) {
            gamePlatformStore.setText(String.format(Constants.PlatformAndStore.platformAndStore, baseGame.getPlatform(), baseGame.getStore()));
        } else {
            gamePlatformStore.setText(Constants.Generic.notAvailable);
        }
    }

    //endregion

    //region Private Helpers

    private void openBrowser(String url){
        Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    //endregion

}
