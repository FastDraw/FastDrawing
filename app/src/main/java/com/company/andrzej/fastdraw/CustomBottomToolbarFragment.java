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
        currentColor = Color.BLACK;
        currentStyle = 12f;
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
                    pencil.setAlpha(0.3f);
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
                if (isChecked) {
                    pencil.setAlpha(1f);
                    pen.setAlpha(0.3f);
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
                if (isChecked) {
                    pencil.setAlpha(1f);
                    pen.setAlpha(1f);
                    marker.setAlpha(0.3f);
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
                if (isChecked) {
                    color_black.setAlpha(0.3f);
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
                if (isChecked) {
                    color_black.setAlpha(1f);
                    color_red.setAlpha(0.3f);
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
                if (isChecked) {
                    color_black.setAlpha(1f);
                    color_red.setAlpha(1f);
                    color_green.setAlpha(0.3f);
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
                if (isChecked) {
                    color_black.setAlpha(1f);
                    color_red.setAlpha(1f);
                    color_green.setAlpha(1f);
                    color_blue.setAlpha(0.3f);
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
                if (isChecked) {
                    Toast.makeText(context, "To use other settings turn off eraser",
                            Toast.LENGTH_SHORT).show();
                    eraser.setAlpha(0.3f);
                    eraserSeekBar.setVisibility(View.VISIBLE);
                    savedColor = getCurrentColor();
                    currentColor = Color.TRANSPARENT;
                    updateDrawingTool(eraserSeekBar.getProgress());
                    setButtonsEnabled(false);
                } else {
                    // TODO remove blocking pens when eraser is on (set erazer the same way as it was transparent pen)
                    eraser.setAlpha(1f);
                    eraserSeekBar.setVisibility(View.INVISIBLE);
                    currentColor = getSavedColor();
                    updateDrawingTool();
                    setButtonsEnabled(true);
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

    private void setButtonsEnabled(boolean enable) {
        pencil.setEnabled(enable);
        pen.setEnabled(enable);
        marker.setEnabled(enable);
        color_black.setEnabled(enable);
        color_red.setEnabled(enable);
        color_green.setEnabled(enable);
        color_blue.setEnabled(enable);
    }

    void updateDrawingTool() {
        drawingView.changeColorAndStyle(currentColor, currentStyle);
    }

    void updateDrawingTool(int style) {
        drawingView.erase(style);
    }

    public int getCurrentColor() {
        return currentColor;
    }

    public int getSavedColor() {
        return savedColor;
    }
}
