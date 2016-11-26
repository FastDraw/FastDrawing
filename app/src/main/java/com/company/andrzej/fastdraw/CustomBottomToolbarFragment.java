package com.company.andrzej.fastdraw;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import butterknife.ButterKnife;

public class CustomBottomToolbarFragment extends Fragment {

    private Activity activity;
    private Context context;
    private ImageButton btnHide;
    private ToggleButton pencil, pen, marker, color_black, color_red, color_green, color_blue, eraser;
    private DrawingView drawingView;
    private int currentColor;
    private float currentStyle;
    private boolean eraserMode;
    private int savedColor; // to remember last used paint when using eraser (workaround)

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
        pencil = (ToggleButton) view.findViewById(R.id.pencil);
        pen = (ToggleButton) view.findViewById(R.id.pen);
        marker = (ToggleButton) view.findViewById(R.id.marker);
        color_black = (ToggleButton) view.findViewById(R.id.color_black);
        color_red = (ToggleButton) view.findViewById(R.id.color_red);
        color_green = (ToggleButton) view.findViewById(R.id.color_green);
        color_blue = (ToggleButton) view.findViewById(R.id.color_blue);
        eraser = (ToggleButton) view.findViewById(R.id.eraser);
        currentColor = Color.BLACK;
        currentStyle = 12f;
        hideFragment();
        setButtonsListeners();
        return view;
    }

<<<<<<< HEAD
    private void hideFragment() {
=======
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        drawingView = (DrawingView) getActivity().findViewById(R.id.drawing_canvas);
    }

    private void hideFragment(){
>>>>>>> 86a60add316dfb3e475bb3005d1d9ab9cfdfdd84
        btnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) activity).hideToolbarFragment();
                ((MainActivity) activity).setButtonsVisible();
            }
        });
    }

    private void setButtonsListeners() {
        pencil.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // temporary alfa change to indicate button on/off status
                if (isChecked){
                    pencil.setAlpha(0.5f);
                    pen.setAlpha(1f);
                    marker.setAlpha(1f);
                    currentStyle = 6f;
                    updateDrawingTool();
                    pen.setChecked(false);
                    marker.setChecked(false);
                    pencil.setEnabled(false);
                    pen.setEnabled(true);
                    marker.setEnabled(true);
                }
            }
        });
        pen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // temporary alfa change to indicate button on/off status
                if (isChecked){
                    pencil.setAlpha(1f);
                    pen.setAlpha(0.5f);
                    marker.setAlpha(1f);
                    currentStyle = 12f;
                    updateDrawingTool();
                    pencil.setChecked(false);
                    marker.setChecked(false);
                    pencil.setEnabled(true);
                    pen.setEnabled(false);
                    marker.setEnabled(true);
                }
            }
        });
        marker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // temporary alfa change to indicate button on/off status
                if (isChecked){
                    pencil.setAlpha(1f);
                    pen.setAlpha(1f);
                    marker.setAlpha(0.5f);
                    currentStyle = 18f;
                    updateDrawingTool();
                    pencil.setChecked(false);
                    pen.setChecked(false);
                    pencil.setEnabled(true);
                    pen.setEnabled(true);
                    marker.setEnabled(false);
                }
            }
        });
        color_black.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // temporary alfa change to indicate button on/off status
                if (isChecked){
                    color_black.setAlpha(0.5f);
                    color_red.setAlpha(1f);
                    color_green.setAlpha(1f);
                    color_blue.setAlpha(1f);
                    currentColor = Color.BLACK;
                    updateDrawingTool();
                    color_red.setChecked(false);
                    color_green.setChecked(false);
                    color_blue.setChecked(false);
                    color_black.setEnabled(false);
                    color_red.setEnabled(true);
                    color_green.setEnabled(true);
                    color_blue.setEnabled(true);
                }
            }
        });
        color_red.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // temporary alfa change to indicate button on/off status
                if (isChecked){
                    color_black.setAlpha(1f);
                    color_red.setAlpha(0.5f);
                    color_green.setAlpha(1f);
                    color_blue.setAlpha(1f);
                    currentColor = Color.RED;
                    updateDrawingTool();
                    color_black.setChecked(false);
                    color_green.setChecked(false);
                    color_blue.setChecked(false);
                    color_black.setEnabled(true);
                    color_red.setEnabled(false);
                    color_green.setEnabled(true);
                    color_blue.setEnabled(true);
                }
            }
        });
        color_green.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // temporary alfa change to indicate button on/off status
                if (isChecked){
                    color_black.setAlpha(1f);
                    color_red.setAlpha(1f);
                    color_green.setAlpha(0.5f);
                    color_blue.setAlpha(1f);
                    currentColor = Color.GREEN;
                    updateDrawingTool();
                    color_black.setChecked(false);
                    color_red.setChecked(false);
                    color_blue.setChecked(false);
                    color_black.setEnabled(true);
                    color_red.setEnabled(true);
                    color_green.setEnabled(false);
                    color_blue.setEnabled(true);
                }
            }
        });
        color_blue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // temporary alfa change to indicate button on/off status
                if (isChecked){
                    color_black.setAlpha(1f);
                    color_red.setAlpha(1f);
                    color_green.setAlpha(1f);
                    color_blue.setAlpha(0.5f);
                    currentColor = Color.BLUE;
                    updateDrawingTool();
                    color_black.setChecked(false);
                    color_red.setChecked(false);
                    color_green.setChecked(false);
                    color_black.setEnabled(true);
                    color_red.setEnabled(true);
                    color_green.setEnabled(true);
                    color_blue.setEnabled(false);
                }
            }
        });
        eraser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // temporary alfa change to indicate button on/off status
                if (isChecked){
                    eraser.setAlpha(0.5f);
                    savedColor = getCurrentColor();
                    currentColor = Color.TRANSPARENT;
                    eraserMode = true;
                    updateDrawingTool();
                    setButtonsEnabled(false);
                } else {
                    eraser.setAlpha(1f);
                    currentColor = getSavedColor();
                    eraserMode = false;
                    updateDrawingTool();
                    setButtonsEnabled(true);
                }
            }
        });
    }

    private void setButtonsEnabled(boolean enable) {
        pencil.setEnabled(enable);
        pen.setEnabled(enable);
        marker.setEnabled(enable);
        color_black.setEnabled(enable);
        color_red.setEnabled(enable);
        color_green.setEnabled(enable);
        color_blue.setEnabled(enable);
    }

    void updateDrawingTool(){
        drawingView.changeColorAndStyle(currentColor, currentStyle, eraserMode);
    }

    public int getCurrentColor() {
        return currentColor;
    }

    public int getSavedColor() {
        return savedColor;
    }
}
