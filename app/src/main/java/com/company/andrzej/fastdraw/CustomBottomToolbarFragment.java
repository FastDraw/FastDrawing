package com.company.andrzej.fastdraw;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import butterknife.ButterKnife;

public class CustomBottomToolbarFragment extends Fragment {

    private Activity activity;
    private Context context;
    private ImageButton btnHide;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        context = activity.getApplicationContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_toolbar_fragment, container, false);
        ButterKnife.bind(this, view);
        btnHide = (ImageButton) view.findViewById(R.id.btn_hidefragment);
        hideFragment();
        return view;
    }

    private void hideFragment() {
        btnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) activity).hideToolbarFragment();
                ((MainActivity) activity).setButtonsVisible();
            }
        });
    }
}
