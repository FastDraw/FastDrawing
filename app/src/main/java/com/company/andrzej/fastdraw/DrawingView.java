package com.company.andrzej.fastdraw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class DrawingView extends View {
    private static final int TRANSPARENT = 0;
    private static final int BLACK = 3;
    private static final int RED = 6;
    private static final int GREEN = 9;
    private static final int BLUE = 12;
    private static final int SMALL = 0;
    private static final int MEDIUM = 1;
    private static final int BIG = 2;

    private static final float TOLERANCE = 5;
    private final Context context;
    private ArrayList<Path> paths;
    private Path lastPath;
    private ArrayList<Paint> paints;
    private Integer[] colors;
    private Float[] styles;
    private Paint currentPaint;
    private ArrayList<Paint> usedPaints;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private float mX, mY;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        paths = new ArrayList<Path>();
        paths.add(new Path());
        lastPath = paths.get(paths.size() - 1);
        paints = new ArrayList<Paint>();
        usedPaints = new ArrayList<Paint>();
        initColorsAndStyles();
        currentPaint = paints.get(BLACK+MEDIUM);
    }

<<<<<<< HEAD
    //cast this methods for settings of marke,pen,feather

    private void penSettings() {
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(6f);
    }

    private void featherSettings() {
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(12f);
    }

    private void markerSettings() {
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(18f);
=======
    private void initColorsAndStyles(){
        colors = new Integer[] {Color.TRANSPARENT, Color.BLACK, Color.RED, Color.GREEN, Color.BLUE};
        styles = new Float[] {6f, 12f, 18f};
        for (int i=0; i<colors.length; i++){
            for (int j=0; j<styles.length; j++){
                Paint p = new Paint();
                p.setColor(colors[i]);
                p.setStrokeWidth(styles[j]);
                if (i==TRANSPARENT){
                    p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                } else {
                    p.setXfermode(null);
                }
                paintInit(p);
                paints.add(p);
            }
        }
>>>>>>> 86a60add316dfb3e475bb3005d1d9ab9cfdfdd84
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
        lastPath = paths.get(paths.size()-1);
        usedPaints.clear();
        postInvalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (usedPaints.size() == 0 || usedPaints.get(usedPaints.size()-1) != currentPaint){
            usedPaints.add(currentPaint);
        }
        for (Path p : paths) {
            // FIXME change currentPaint for list of colors and use appropriate
            // [now it duplicates -> can lead to infinite currentPaint number]
            // Fixed?
            canvas.drawPath(p, usedPaints.get(paths.indexOf(p)));
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        float pointX = event.getX();
        float pointY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastPath.moveTo(pointX, pointY);
                mX = pointX;
                mY = pointY;
                postInvalidate();
                break;
            case MotionEvent.ACTION_MOVE:
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
                lastPath.lineTo(mX, mY);
                postInvalidate();
                break;
            default:
                return false;
        }
        return true;
    }

<<<<<<< HEAD
    public void changeColor(int color, boolean eraser) {
        paints.add(new Paint(paint));
        paints.get(paints.size() - 1).setColor(color);
        if (eraser) {
            // sets layer for transparent paint
            Paint q = new Paint(Paint.ANTI_ALIAS_FLAG);
            setLayerType(LAYER_TYPE_HARDWARE, q);
            paints.get(paints.size() - 1).setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        } else {
            paints.get(paints.size() - 1).setXfermode(null);
=======
    public void changeColorAndStyle(int color, float style, boolean eraser){
        // TODO change method to not create different paint but use predefined instead
        int i=0;
        while (!colors[i].equals(color)){
            if (colors.length == i+1){
                // invalid color sent from Toolbar - shouldn't ever happen
                break;
            }
            i++;
        }
        int j=0;
        while (!styles[j].equals(style)){
            if (styles.length == j+1){
                // invalid style sent from Toolbar - shouldn't ever happen
                break;
            }
            j++;
        }
        currentPaint = paints.get(3*i+j);
        if (eraser){
            // sets layer for transparent currentPaint
            Paint q = new Paint(Paint.ANTI_ALIAS_FLAG);
            setLayerType(LAYER_TYPE_HARDWARE, q);
>>>>>>> 86a60add316dfb3e475bb3005d1d9ab9cfdfdd84
        }
        paths.add(new Path());
        lastPath = paths.get(paths.size() - 1);
        postInvalidate();
    }
}