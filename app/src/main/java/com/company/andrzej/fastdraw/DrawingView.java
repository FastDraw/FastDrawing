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

    private static final float TOLERANCE = 5;
    private final Context context;
    private ArrayList<Path> paths;
    private Path lastPath;
    private ArrayList<Paint> paints;
    private Paint paint;
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
        paint = new Paint();
        paintInit();
        paints.add(paint);
    }

    //cast this methods for settings of marke,pen,feather

    private void penSettings(){
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(6f);
    }

    private void featherSettings(){
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(12f);
    }

    private void markerSettings(){
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(18f);
    }

    private void paintInit() {
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(10f);
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
        for (Path p : paths) {
            // FIXME change currentPaint for list of colors and use appropriate
            // [now it duplicates -> can lead to infinite paint number]
            Paint currentPaint = paints.get(paths.indexOf(p));
            canvas.drawPath(p, currentPaint);
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

    public void changeColor(int color, boolean eraser){
        paints.add(new Paint(paint));
        paints.get(paints.size()-1).setColor(color);
        if (eraser){
            // sets layer for transparent paint
            Paint q = new Paint(Paint.ANTI_ALIAS_FLAG);
            setLayerType(LAYER_TYPE_HARDWARE, q);
            paints.get(paints.size()-1).setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        } else {
            paints.get(paints.size()-1).setXfermode(null);
        }
        paths.add(new Path());
        lastPath = paths.get(paths.size()-1);
        postInvalidate();
    }
}