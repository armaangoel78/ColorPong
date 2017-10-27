package com.armaangoel.myapplication;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by sunjaygoel on 9/2/16.
 */
public class ScoreManager implements  GameObject{
    private int color;
    private int score;
    private int width;
    private int height;


    public int getScore () {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public ScoreManager (int color, int width, int height) {
        this.color = color;
        this.width = width;
        this.height = height;

    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setTextSize(100);
        paint.setColor(color);
        canvas.drawText(Integer.toString(score), width, height , paint);
    }

    @Override
    public void update() {

    }

    public void update(Canvas canvas) {

    }
}
