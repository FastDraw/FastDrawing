package com.company.andrzej.fastdraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class DrawingView extends View {

    private static final int TRANSPARENT = 0;
    private static final int BLACK = 3;
    private static final int MEDIUM = 1;
    private static final float TOLERANCE = 5;

    private Context context;
    private ArrayList<Path> paths;
    private Path lastPath;
    private ArrayList<Paint> paints;
    private ArrayList<Paint> erasers;
    private Integer[] colors;
    private Float[] styles;
    private Paint currentPaint;
    private ArrayList<Paint> usedPaints;
    private float mX, mY;
    private ImageView pointer;
    private float pointerXcenter;
    private float pointerYcenter;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        paths = new ArrayList<>();
        paths.add(new Path());
        lastPath = paths.get(paths.size() - 1);
        paints = new ArrayList<>();
        erasers = new ArrayList<>();
        usedPaints = new ArrayList<>();
        initColorsAndStyles();
        initErasers();
        currentPaint = paints.get(BLACK + MEDIUM);
    }

    private void initColorsAndStyles() {
        colors = new Integer[]{Color.TRANSPARENT, Color.BLACK, Color.RED, Color.GREEN, Color.BLUE};
        styles = new Float[]{6f, 12f, 18f};
        for (int i = 0; i < colors.length; i++) {
            for (Float style : styles) {
                Paint p = new Paint();
                p.setColor(colors[i]);
                p.setStrokeWidth(style);
                if (i == TRANSPARENT) {
                    p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                } else {
                    p.setXfermode(null);
                }
                paintInit(p);
                paints.add(p);
            }
        }
    }

    private void initErasers() {
        colors = new Integer[]{Color.TRANSPARENT};
        styles = new Float[]{15f,16f,17f,18f,19f,20f,21f,22f,23f,24f,25f,26f,27f,28f,29f,30f,31f,32f,33f,34f,35f};
        for (int i = 0; i < colors.length; i++) {
            for (Float style : styles) {
                Paint p = new Paint();
                p.setColor(colors[i]);
                p.setStrokeWidth(style);
                if (i == TRANSPARENT) {
                    p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                } else {
                    p.setXfermode(null);
                }
                paintInit(p);
                erasers.add(p);
            }
        }
    }

    private void paintInit(Paint p) {
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeJoin(Paint.Join.ROUND);
    }

    public void resetCanvas() {
        for (Path p : paths) {
            p.reset();
        }
        paths.clear();
        paths.add(new Path());
        lastPath = paths.get(paths.size() - 1);
        usedPaints.clear();
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (usedPaints.size() == 0 || usedPaints.get(usedPaints.size() - 1) != currentPaint) {
            usedPaints.add(currentPaint);
        }
        for (Path p : paths) {
            canvas.drawPath(p, usedPaints.get(paths.indexOf(p)));
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (pointer == null) {
            pointer = ((MainActivity) context).getPointer();
        }
        float scale = (currentPaint.getStrokeWidth() + 8) / pointer.getHeight();
        pointer.setScaleX(scale);
        pointer.setScaleY(scale);
        pointerXcenter = pointer.getHeight() / 2;
        pointerYcenter = pointer.getWidth() / 2;
        float pointX = event.getX();
        float pointY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pointer.setVisibility(VISIBLE);
                pointer.setX(pointX - pointerXcenter);
                pointer.setY(pointY - pointerYcenter);
                lastPath.moveTo(pointX, pointY);
                mX = pointX;
                mY = pointY;
                postInvalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                pointer.animate()
                        .x(pointX - pointerXcenter)
                        .y(pointY - pointerYcenter)
                        .setDuration(0)
                        .start();
                float dx = Math.abs(pointX - mX);
                float dy = Math.abs(pointY - mY);
                if (dx >= TOLERANCE || dy >= TOLERANCE) {
                    lastPath.quadTo(mX, mY, (pointX + mX) / 2, (pointY + mY) / 2);
                    mX = pointX;
                    mY = pointY;
                }
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                pointer.setVisibility(INVISIBLE);
                lastPath.lineTo(mX, mY);
                postInvalidate();
                break;
            default:
                return false;
        }
        return true;
    }

    public void changeColorAndStyle(int color, float style){//}, boolean eraser) {
        // TODO change method to not create different paint but use predefined instead
        int i = 0;
        while (!colors[i].equals(color)) {
            if (colors.length == i + 1) {
                // invalid color sent from Toolbar - shouldn't ever happen
                break;
            }
            i++;
        }
        int j = 0;
        //if (!eraser) {
            while (!styles[j].equals(style)) {
                if (styles.length == j + 1) {
                    // invalid style sent from Toolbar - shouldn't ever happen
                    break;
                }
                j++;
            }
        //}
        currentPaint = paints.get(3 * i + j);
        //if (eraser) {
        //    currentPaint.setStrokeWidth(style);
        //    // sets layer for transparent currentPaint
        //    Paint q = new Paint(Paint.ANTI_ALIAS_FLAG);
        //    setLayerType(LAYER_TYPE_HARDWARE, q);
        //}
        paths.add(new Path());
        lastPath = paths.get(paths.size() - 1);
        postInvalidate();
    }

    public void erase(int style) {
        currentPaint = erasers.get(style);
        //currentPaint.setStrokeWidth(style);
        // sets layer for transparent currentPaint
        Paint q = new Paint(Paint.ANTI_ALIAS_FLAG);
        setLayerType(LAYER_TYPE_HARDWARE, q);
        paths.add(new Path());
        lastPath = paths.get(paths.size() - 1);
        //usedPaints.add(currentPaint);
        postInvalidate();
    }
}