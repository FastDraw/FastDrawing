package com.company.andrzej.fastdraw;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import butterknife.ButterKnife;

public class CustomBottomToolbarFragment extends Fragment {

    private static final int PENCIL = 0;
    private static final int PEN = 1;
    private static final int MARKER = 2;
    private static final int ERASER = 3;
    private Context context;
    private SOSFragment sFragment;
    private ImageButton btnHide, undoBtn, forwardBtn, sosFragment;
    private ToggleButton pencil, pen, marker, color_black, color_red, color_green, color_blue, eraser;
    private SeekBar eraserSeekBar;
    private DrawingView drawingView;
    private int currentColor;
    private float currentStyle;
    private int savedColor; // to remember last used paint when using eraser (workaround)

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
        eraserSeekBar = (SeekBar) view.findViewById(R.id.eraserSeekBar);
        undoBtn = (ImageButton) view.findViewById(R.id.undo_button);
        forwardBtn = (ImageButton) view.findViewById(R.id.forward_button);
        drawingView = (DrawingView) getActivity().findViewById(R.id.drawing_canvas);
        sosFragment = (ImageButton) view.findViewById(R.id.sos_button);
        setCurrentColor(Color.BLACK);
        setCurrentStyle(12f);
        hideFragment();
        setButtonsListeners();
        openSOSFragment();
        return view;
    }

    private void openSOSFragment(){
        sosFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).hideToolbarFragment();
                ((MainActivity) context).openSOSFragment();
            }
        });
    }

    private void hideFragment() {
        btnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).hideToolbarFragment();
                ((MainActivity) context).setButtonsVisible();
            }
        });
    }

    private void setButtonsListeners() {
        pencil.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // temporary alfa change to indicate button on/off status
                if (isChecked) {
                    setDrawingToolAlpha(PENCIL);
                    setDrawingToolDisabled(PENCIL);
                    setDrawingToolsUnchecked(PENCIL);
                    setCurrentStyle(6f);
                    setCurrentColor(getSavedColor());
                    setColorToolAlpha(currentColor);
                    setColorToolsUnchecked(currentColor);
                    setColorToolDisabled(currentColor);
                    updateDrawingTool();
                }
            }
        });
        pen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // temporary alfa change to indicate button on/off status
                if (isChecked) {
                    setDrawingToolAlpha(PEN);
                    setDrawingToolDisabled(PEN);
                    setDrawingToolsUnchecked(PEN);
                    setCurrentStyle(12f);
                    setCurrentColor(getSavedColor());
                    setColorToolAlpha(currentColor);
                    setColorToolsUnchecked(currentColor);
                    setColorToolDisabled(currentColor);
                    updateDrawingTool();
                }
            }
        });
        marker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // temporary alfa change to indicate button on/off status
                if (isChecked) {
                    setDrawingToolAlpha(MARKER);
                    setDrawingToolDisabled(MARKER);
                    setDrawingToolsUnchecked(MARKER);
                    setCurrentStyle(18f);
                    setCurrentColor(getSavedColor());
                    setColorToolAlpha(currentColor);
                    setColorToolsUnchecked(currentColor);
                    setColorToolDisabled(currentColor);
                    updateDrawingTool();
                }
            }
        });
        color_black.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // temporary alfa change to indicate button on/off status
                if (isChecked) {
                    setColorToolAlpha(Color.BLACK);
                    setColorToolDisabled(Color.BLACK);
                    setColorToolsUnchecked(Color.BLACK);
                    setCurrentColor(Color.BLACK);
                    setSavedColor(Color.BLACK);
                    updateDrawingTool();
                }
            }
        });
        color_red.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // temporary alfa change to indicate button on/off status
                if (isChecked) {
                    setColorToolAlpha(Color.RED);
                    setColorToolDisabled(Color.RED);
                    setColorToolsUnchecked(Color.RED);
                    setCurrentColor(Color.RED);
                    setSavedColor(Color.RED);
                    updateDrawingTool();
                }
            }
        });
        color_green.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // temporary alfa change to indicate button on/off status
                if (isChecked) {
                    setColorToolAlpha(Color.GREEN);
                    setColorToolDisabled(Color.GREEN);
                    setColorToolsUnchecked(Color.GREEN);
                    setCurrentColor(Color.GREEN);
                    setSavedColor(Color.GREEN);
                    updateDrawingTool();
                }
            }
        });
        color_blue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // temporary alfa change to indicate button on/off status
                if (isChecked) {
                    setColorToolAlpha(Color.BLUE);
                    setColorToolDisabled(Color.BLUE);
                    setColorToolsUnchecked(Color.BLUE);
                    setCurrentColor(Color.BLUE);
                    setSavedColor(Color.BLUE);
                    updateDrawingTool();
                }
            }
        });
        eraser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setDrawingToolAlpha(ERASER);
                    setDrawingToolDisabled(ERASER);
                    setDrawingToolsUnchecked(ERASER);
                    setColorToolAlpha(Color.TRANSPARENT);
                    setColorToolDisabled(Color.TRANSPARENT);
                    setColorToolsUnchecked(Color.TRANSPARENT);
                    setSavedColor(getCurrentColor());
                    setCurrentColor(Color.TRANSPARENT);
                    updateErasingTool(eraserSeekBar.getProgress());
                }
            }


        });
        eraserSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                updateDrawingTool(seekBar.getProgress());
            }
        });
        undoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawingView.isPathPaintMapEmpty()) {
                    drawingView.undo();
                } else {
                    Toast.makeText(context, "Nothing to undo",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        forwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawingView.isDeletedPathsEmpty()) {
                    drawingView.forward();
                } else {
                    Toast.makeText(context, "Nothing to forward",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateDrawingTool() {
        drawingView.changeColorAndStyle(currentColor, currentStyle);
    }

    private void updateErasingTool(int style) {
        drawingView.erase(style);
    }

    private void updateTextTool(int style) {
        drawingView.text(style);
    }

    public int getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(int currentColor) {
        this.currentColor = currentColor;
    }

    public int getSavedColor() {
        return savedColor;
    }

    public void setSavedColor(int savedColor) {
        this.savedColor = savedColor;
    }

    public void setCurrentStyle(float currentStyle) {
        this.currentStyle = currentStyle;
    }

    // Sets clicked tool semitransparent and all the other non-transparent
    private void setDrawingToolAlpha(int i){
        switch (i){
            case PENCIL:
                pencil.setAlpha(0.3f);
                pen.setAlpha(1f);
                marker.setAlpha(1f);
                eraser.setAlpha(1f);
                break;
            case PEN:
                pencil.setAlpha(1f);
                pen.setAlpha(0.3f);
                marker.setAlpha(1f);
                eraser.setAlpha(1f);
                break;
            case MARKER:
                pencil.setAlpha(1f);
                pen.setAlpha(1f);
                marker.setAlpha(0.3f);
                eraser.setAlpha(1f);
                break;
            case ERASER:
                pencil.setAlpha(1f);
                pen.setAlpha(1f);
                marker.setAlpha(1f);
                eraser.setAlpha(0.3f);
                break;
        }
    }

    // Sets clicked tool disabled and all the other enabled (hides or shows eraser's seekbar accordingly)
    private void setDrawingToolDisabled(int i){
        switch (i){
            case PENCIL:
                pencil.setEnabled(false);
                pen.setEnabled(true);
                marker.setEnabled(true);
                eraser.setEnabled(true);
                eraserSeekBar.setVisibility(View.INVISIBLE);
                break;
            case PEN:
                pencil.setEnabled(true);
                pen.setEnabled(false);
                marker.setEnabled(true);
                eraser.setEnabled(true); // new
                eraserSeekBar.setVisibility(View.INVISIBLE);
                break;
            case MARKER:
                pencil.setEnabled(true);
                pen.setEnabled(true);
                marker.setEnabled(false);
                eraser.setEnabled(true); // new
                eraserSeekBar.setVisibility(View.INVISIBLE);
                break;
            case ERASER:
                pencil.setEnabled(true);
                pen.setEnabled(true);
                marker.setEnabled(true);
                eraser.setEnabled(false);
                eraserSeekBar.setVisibility(View.VISIBLE);
                break;
        }
    }

    // Sets all not clicked tools unchecked
    private void setDrawingToolsUnchecked(int i){
        switch (i){
            case PENCIL:
                pen.setChecked(false);
                marker.setChecked(false);
                eraser.setChecked(false);
                break;
            case PEN:
                pencil.setChecked(false);
                marker.setChecked(false);
                eraser.setChecked(false);
                break;
            case MARKER:
                pencil.setChecked(false);
                pen.setChecked(false);
                eraser.setChecked(false);
                break;
            case ERASER:
                pencil.setChecked(false);
                pen.setChecked(false);
                marker.setChecked(false);
                break;
        }
    }

    // Sets clicked color button semitransparent and all the other non-transparent
    private void setColorToolAlpha(int i){
        switch (i){
            case Color.BLACK:
                color_black.setAlpha(0.3f);
                color_red.setAlpha(1f);
                color_green.setAlpha(1f);
                color_blue.setAlpha(1f);
                break;
            case Color.RED:
                color_black.setAlpha(1f);
                color_red.setAlpha(0.3f);
                color_green.setAlpha(1f);
                color_blue.setAlpha(1f);
                break;
            case Color.GREEN:
                color_black.setAlpha(1f);
                color_red.setAlpha(1f);
                color_green.setAlpha(0.3f);
                color_blue.setAlpha(1f);
                break;
            case Color.BLUE:
                color_black.setAlpha(1f);
                color_red.setAlpha(1f);
                color_green.setAlpha(1f);
                color_blue.setAlpha(0.3f);
                break;
            case Color.TRANSPARENT:
                color_black.setAlpha(1f);
                color_red.setAlpha(1f);
                color_green.setAlpha(1f);
                color_blue.setAlpha(1f);
                break;
        }
    }

    // Sets clicked color button disabled and all the other enabled
    private void setColorToolDisabled(int i){
        switch (i){
            case Color.BLACK:
                color_black.setEnabled(false);
                color_red.setEnabled(true);
                color_green.setEnabled(true);
                color_blue.setEnabled(true);
                break;
            case Color.RED:
                color_black.setEnabled(true);
                color_red.setEnabled(false);
                color_green.setEnabled(true);
                color_blue.setEnabled(true);
                break;
            case Color.GREEN:
                color_black.setEnabled(true);
                color_red.setEnabled(true);
                color_green.setEnabled(false);
                color_blue.setEnabled(true);
                break;
            case Color.BLUE:
                color_black.setEnabled(true);
                color_red.setEnabled(true);
                color_green.setEnabled(true);
                color_blue.setEnabled(false);
                break;
            case Color.TRANSPARENT:
                color_black.setEnabled(false);
                color_red.setEnabled(false);
                color_green.setEnabled(false);
                color_blue.setEnabled(false);
                break;
        }
    }

    // Sets all not clicked color buttons unchecked
    private void setColorToolsUnchecked(int i){
        switch (i){
            case Color.BLACK:
                color_red.setChecked(false);
                color_green.setChecked(false);
                color_blue.setChecked(false);
                break;
            case Color.RED:
                color_black.setChecked(false);
                color_green.setChecked(false);
                color_blue.setChecked(false);
                break;
            case Color.GREEN:
                color_black.setChecked(false);
                color_red.setChecked(false);
                color_blue.setChecked(false);
                break;
            case Color.BLUE:
                color_black.setChecked(false);
                color_red.setChecked(false);
                color_green.setChecked(false);
                break;
            case Color.TRANSPARENT:
                color_black.setChecked(false);
                color_red.setChecked(false);
                color_green.setChecked(false);
                color_blue.setChecked(false);
                break;
        }
    }
}
