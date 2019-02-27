package com.saiyanstudio.gamerack.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.saiyanstudio.gamerack.R;
import com.saiyanstudio.gamerack.common.Constants;
import com.saiyanstudio.gamerack.common.Utility;
import com.saiyanstudio.gamerack.models.Game;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by deekshith on 12-11-2017.
 */

public class AddGameAdapter extends RecyclerView.Adapter<AddGameAdapter.MyViewHolder> {

    private Context context;
    private List<Game> gamesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView_game) ImageView gameImage;
        @BindView(R.id.textView_game_name) TextView name;
        @BindView(R.id.textView_game_release_year) TextView releaseYear;
        @BindView(R.id.checkBox_game_in_library) CheckBox inLibrary;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public AddGameAdapter(Context context, List<Game> gamesList) {
        this.context = context;
        this.gamesList = gamesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_add_game, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Game game = gamesList.get(position);
        holder.name.setText(game.getName());
        holder.releaseYear.setText(Utility.epochTimeToYear(game.getFirstReleaseDate()));
        holder.inLibrary.setChecked(game.isInLibrary());
        if(game.getCover() != null){
            String imageUrl = String.format(Constants.IGDBApi.coverBigImageBaseUrl, game.getCover().getImageId());
            Picasso.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_image_grey_24dp)
                    .fit()
                    .into(holder.gameImage);
        }
    }

    @Override
    public int getItemCount() {
        return gamesList.size();
    }
}
