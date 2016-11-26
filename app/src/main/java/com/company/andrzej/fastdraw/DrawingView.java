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

    private void init(){
        paths = new ArrayList<Path>();
        paths.add(new Path());
        lastPath = paths.get(paths.size()-1);
        paints = new ArrayList<Paint>();
        usedPaints = new ArrayList<Paint>();
        initColorsAndStyles();
        currentPaint = paints.get(BLACK+MEDIUM);
    }

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
    }

    private void paintInit(Paint p) {
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeJoin(Paint.Join.ROUND);
    }

    // FIXME resets only last lastPath at the moment - consider using as resetPath() method
    // or undo() method if new lastPath is created each time ACTION_UP happens (finger lifted up)
    public void resetCanvas() {
        lastPath.reset();
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
        }
        paths.add(new Path());
        lastPath = paths.get(paths.size()-1);
        postInvalidate();
    }
}