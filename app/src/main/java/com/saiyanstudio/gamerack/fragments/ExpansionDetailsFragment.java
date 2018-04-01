package com.saiyanstudio.gamerack.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.saiyanstudio.gamerack.R;
import com.saiyanstudio.gamerack.adapters.ExpansionAdapter;
import com.saiyanstudio.gamerack.common.Constants;
import com.saiyanstudio.gamerack.models.Expansion;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExpansionDetailsFragment extends Fragment {

    @BindView(R.id.checkBox_played_all) CheckBox playedAll;
    @BindView(R.id.textView_no_expansions_msg) TextView noExpansionsMsg;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private List<Expansion> expansionList;
    private ExpansionAdapter expansionAdapter;

    public ExpansionDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_expansion_details, container, false);

        ButterKnife.bind(this, fragmentView);

        Bundle bundle = getArguments();

        expansionList = bundle.getParcelableArrayList(Constants.IntentKeys.expansion);

        if(expansionList == null) expansionList = new ArrayList<>();

        if(expansionList.isEmpty()){
            playedAll.setVisibility(View.INVISIBLE);
            noExpansionsMsg.setVisibility(View.VISIBLE);
        }

        expansionAdapter = new ExpansionAdapter(getActivity(), expansionList);
        expansionAdapter.notifyDataSetChanged();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(expansionAdapter);

        return fragmentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
