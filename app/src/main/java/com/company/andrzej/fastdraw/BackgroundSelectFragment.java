package com.company.andrzej.fastdraw;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

public class BackgroundSelectFragment extends Fragment {

    private Context context;
    private String[] web;
    private RecyclerView recyclerView;

    private int[] drawableResources() {
        TypedArray ar = context.getResources().obtainTypedArray(R.array.drawable_positions);
        int len = ar.length();
        int[] imageID = new int[len];
        for (int i = 0; i < len; i++)
            imageID[i] = ar.getResourceId(i, 0);
        ar.recycle();
        return imageID;
    }

    public void initStringResources() {
        Resources res;
        res = context.getResources();
        web = res.getStringArray(R.array.background_name);

    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    // onAttach(Activity) was deprecated with API 23, but new onAttach(Context) doesn't work with API<23 in this form
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            this.context = activity;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_background, container, false);
        ButterKnife.bind(this, view);
        initStringResources();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        initRecyclerView();
        return view;
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewForBackground mAdapter = new RecyclerViewForBackground(web, drawableResources());
        recyclerView.setAdapter(mAdapter);
    }
}
