package com.company.andrzej.fastdraw;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

public class BackgroundSelectFragment extends Fragment {

    private Activity activity;
    private Context context;
    private String[] web;

    //ToDo try to add integer-array and initialize it
    Integer[] imageID = {
            R.drawable.farm_small,
            R.drawable.scroll_small,
            R.drawable.desert_small,
            R.drawable.bg_blue_small,
            R.drawable.city_small,
            R.drawable.clip_small,
            R.drawable.grass_small,
            R.drawable.green_leaves_small,
            R.drawable.moon_landscape_small,
            R.drawable.prismatic_floruish_small,
            R.drawable.technologic_small,
            R.drawable.white_small,
            R.drawable.red_small,
            R.drawable.bllue_small,
            R.drawable.yellow_small
    };

    private void initStringResources() {
        Resources res;
        res = this.getResources();
        web = res.getStringArray(R.array.background_name);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        context = activity.getApplicationContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_background, container, false);
        ButterKnife.bind(this, view);
        initStringResources();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewForBackground mAdapter = new RecyclerViewForBackground(this, web, imageID);
        recyclerView.setAdapter(mAdapter);
        return view;
    }
}
