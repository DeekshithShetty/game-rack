package com.saiyanstudio.gamerack.adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saiyanstudio.gamerack.R;
import com.saiyanstudio.gamerack.common.Constants;
import com.saiyanstudio.gamerack.handlers.DatabaseHandler;
import com.saiyanstudio.gamerack.models.Game;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by deekshith on 05-11-2017.
 */

public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.MyViewHolder> {

    private Context context;
    private List<Game> gamesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.constraintLayout) ConstraintLayout constraintLayout;
        @BindView(R.id.imageView_game) ImageView gameImage;
        @BindView(R.id.textView_game_name) TextView name;
        @BindView(R.id.textView_game_platform_store) TextView platform_and_store;
        @BindView(R.id.textView_game_status) TextView status;
        @BindView(R.id.imageView_game_isFavorite) ImageView isFavorite;
        @BindView(R.id.layout_game_currently_playing) LinearLayout currentlyPlayingStatus;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public GamesAdapter(Context context, List<Game> gamesList) {
        this.context = context;
        this.gamesList = gamesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_game, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Game game = gamesList.get(position);

        if(game.getCover() != null){
            String imageUrl = String.format(Constants.IGDBApi.coverBigImageBaseUrl, game.getCover().getCloudinaryId());
            Picasso.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_image_grey_24dp)
                    .fit()
                    .into(holder.gameImage);
        }

        holder.name.setText(game.getName());
        holder.platform_and_store.setText(String.format(Constants.PlatformAndStore.platformAndStore, game.getPlatform(), game.getStore()));
        holder.status.setText(game.getStatus());

        if(game.getStatus().equalsIgnoreCase(Constants.Status.currentlyPlaying)){
            holder.currentlyPlayingStatus.setVisibility(View.VISIBLE);
        } else {
            holder.currentlyPlayingStatus.setVisibility(View.GONE);
        }

        if(game.isFavorite()){
            holder.isFavorite.setVisibility(View.VISIBLE);
        } else {
            holder.isFavorite.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return gamesList.size();
    }

    public void filter(List<Game> temp){
        gamesList = new ArrayList<>();
        gamesList.addAll(temp);
        notifyDataSetChanged();
    }

    public List<Game> getList(){
        return gamesList;
    }

}
