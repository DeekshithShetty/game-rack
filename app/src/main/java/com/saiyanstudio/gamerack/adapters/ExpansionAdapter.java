package com.saiyanstudio.gamerack.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.saiyanstudio.gamerack.R;
import com.saiyanstudio.gamerack.handlers.DatabaseHandler;
import com.saiyanstudio.gamerack.messageevents.UpdateExpansionMessageEvent;
import com.saiyanstudio.gamerack.models.Expansion;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by deekshith on 11-11-2017.
 */

public class ExpansionAdapter extends RecyclerView.Adapter<ExpansionAdapter.MyViewHolder> {

    private Context context;
    private List<Expansion> expansionList;
    private DatabaseHandler db;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cardView) CardView cardView;
        @BindView(R.id.textView_expansion_name) TextView expansionname;
        @BindView(R.id.checkBox_expansion_completed) CheckBox expansionCompleted;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public ExpansionAdapter(Context context, List<Expansion> expansionList) {
        this.context = context;
        this.expansionList = expansionList;
        db = new DatabaseHandler(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_expansion, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Expansion expansion = expansionList.get(position);

        //String expansionName = expansion.getName().replace(expansion.getBaseGameName() + " - ", "");
        //expansionName = expansionName.replace(expansion.getBaseGameName() + ": ", "");

        String expansionName = expansion.getName();
        expansionName = expansionName.substring(expansionName.indexOf(":") + 1);
        expansionName = expansionName.substring(expansionName.indexOf("-") + 1);
        expansionName = expansionName.trim();

        holder.expansionname.setText(expansionName);
        holder.expansionCompleted.setChecked(expansion.isCompleted());

        holder.expansionCompleted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                expansion.setCompleted(isChecked);
                db.updateExpansion(expansion);
                db.closeDB();
                // Send UpdateExpansionMessageEvent
                EventBus.getDefault().post(new UpdateExpansionMessageEvent(expansion));
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isExpansionCompleted = holder.expansionCompleted.isChecked();
                holder.expansionCompleted.setChecked(!isExpansionCompleted);
            }
        });
    }

    @Override
    public int getItemCount() {
        return expansionList.size();
    }
}
