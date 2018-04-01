package com.saiyanstudio.gamerack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.saiyanstudio.gamerack.common.Constants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FullscreenImageActivity extends AppCompatActivity {

    @BindView(R.id.imageView_game) PhotoView gameImage;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    @BindView(R.id.adView) AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_image);

        ButterKnife.bind(this);

        // Load ads
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(Constants.AdMobs.testDeviceId)
                .build();
        adView.loadAd(adRequest);

        final String cloudinaryId = getIntent().getStringExtra(Constants.IntentKeys.cloudinaryId);

        if(cloudinaryId != null && !cloudinaryId.isEmpty()) {
            String fullScreenImageUrl = String.format(Constants.IGDBApi.coverFullScreenImageBaseUrl, cloudinaryId);

            Picasso.with(this)
                .load(fullScreenImageUrl)
                .placeholder(R.drawable.ic_image_grey_24dp)
                .into(gameImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        gameImage.setZoomable(false);
                        progressBar.setVisibility(View.GONE);
                    }
                });
        } else {
            gameImage.setZoomable(false);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

}
