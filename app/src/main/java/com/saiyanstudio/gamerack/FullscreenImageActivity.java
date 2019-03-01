package com.saiyanstudio.gamerack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.github.chrisbanes.photoview.PhotoView;
import com.saiyanstudio.gamerack.common.Constants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FullscreenImageActivity extends AppCompatActivity {

    @BindView(R.id.imageView_game) PhotoView gameImage;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_image);

        ButterKnife.bind(this);

        final String imageId = getIntent().getStringExtra(Constants.IntentKeys.imageId);

        if(imageId != null && !imageId.isEmpty()) {
            String fullScreenImageUrl = String.format(Constants.IGDBApi.coverFullScreenImageBaseUrl, imageId);

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
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
