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
import java.util.LinkedHashMap;


public class DrawingView extends View {

    private static final int TRANSPARENT = 0;
    private static final int BLACK = 3;
    private static final int MEDIUM = 1;
    private static final float TOLERANCE = 5;

    private Context context;
    private Path lastPath;
    private ArrayList<Paint> paints;
    private ArrayList<Paint> erasers;
    private Integer[] colors;
    private Integer[] eraserColors;
    private Float[] styles;
    private Float[] eraserStyles;
    private Paint currentPaint;
    private float mX, mY;
    private ImageView pointer;
    private float pointerXcenter;
    private float pointerYcenter;

    private LinkedHashMap<Path, Paint> pathPaintMap;
    private boolean fingerDown;
    private LinkedHashMap<Path, Paint> deletedPaths;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        pathPaintMap = new LinkedHashMap<>();
        deletedPaths = new LinkedHashMap<>();
        lastPath = new Path();
        paints = new ArrayList<>();
        erasers = new ArrayList<>();
        initColorsAndStyles();
        initErasers();
        setCurrentPaint(BLACK, MEDIUM);
        setFingerDown(false);
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
        eraserColors = new Integer[]{Color.TRANSPARENT};
        eraserStyles = new Float[]{15f, 16f, 17f, 18f, 19f, 20f, 21f, 22f, 23f, 24f, 25f, 26f, 27f, 28f, 29f, 30f, 31f, 32f, 33f, 34f, 35f};
        for (int i = 0; i < eraserColors.length; i++) {
            for (Float style : eraserStyles) {
                Paint p = new Paint();
                p.setColor(eraserColors[i]);
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
        pathPaintMap.clear();
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isPathPaintMapEmpty()) {
            for (Path p : pathPaintMap.keySet()) {
                canvas.drawPath(p, pathPaintMap.get(p));
            }
        }
        // Draws the path that is currently created, but not finished (finger down, but not up yet)
        if (isFingerDown()) {
            canvas.drawPath(lastPath, currentPaint);
        }
    }

    @Override
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
                clearDeletedPaths();
                setFingerDown(true);
                lastPath = new Path();
                pointer.setVisibility(VISIBLE);
                pointer.setX(pointX - pointerXcenter);
                pointer.setY(pointY - pointerYcenter);
                lastPath.moveTo(pointX, pointY);
                mX = pointX;
                mY = pointY;
                postInvalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                setFingerDown(true);
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
                addPathPaintToMap();
                setFingerDown(false);
                postInvalidate();
                break;
            default:
                return false;
        }
        return true;
    }

    public void changeColorAndStyle(int color, float style) {
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
        while (!styles[j].equals(style)) {
            if (styles.length == j + 1) {
                // invalid style sent from Toolbar - shouldn't ever happen
                break;
            }
            j++;
        }
        currentPaint = paints.get(3 * i + j);
        lastPath = new Path();
        postInvalidate();
    }

    public void erase(int style) {
        currentPaint = erasers.get(style);
        Paint q = new Paint(Paint.ANTI_ALIAS_FLAG);
        setLayerType(LAYER_TYPE_HARDWARE, q);
        postInvalidate();
    }

    public void text(int style){
        //TODO add text fields
    }

    public void undo() {
        Path p = getLastKeyFromMap(pathPaintMap);
        addDeletedPath(p, pathPaintMap.get(p));
        pathPaintMap.remove(p);
        postInvalidate();
    }

    public void forward() {
        Path p = getLastKeyFromMap(deletedPaths);
        pathPaintMap.put(p, deletedPaths.get(p));
        removeDeletedPath(p);
        postInvalidate();
    }

    public Path getLastKeyFromMap(LinkedHashMap<Path, Paint> map) {
        int i = 0;
        for (Path p : map.keySet()) {
            i++;
            if (i == map.size()) {
                return p;
            }
        }
        return null; //TODO exception?
    }

    public void addDeletedPath(Path path, Paint paint) {
        deletedPaths.put(path, paint);
    }

    public void removeDeletedPath(Path path) {
        deletedPaths.remove(path);
    }

    public void clearDeletedPaths() {
        if (!deletedPaths.isEmpty()) {
            deletedPaths.clear();
        }
    }

    public boolean isDeletedPathsEmpty() {
        return deletedPaths.isEmpty();
    }

    public boolean isPathPaintMapEmpty() {
        return pathPaintMap.isEmpty();
    }

    public void addPathPaintToMap() {
        if (lastPath != null) {
            pathPaintMap.put(lastPath, currentPaint);
        }
    }

    // Getters and Setters

    private void setCurrentPaint(int color, int style) {
        currentPaint = paints.get(color + style);
    }

    public boolean isFingerDown() {
        return fingerDown;
    }

    public void setFingerDown(boolean fingerDown) {
        this.fingerDown = fingerDown;
    }
}