package com.armaangoel.myapplication;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by sunjaygoel on 9/2/16.
 */
public class GameMenu implements  GameObject{
    private int color;
    private String text;
    private int textSize;
    private int width;
    private int height;
    private boolean colorMode;
    private int menuOrRetry;

    public GameMenu (int menuOrRetry, String text,int width, int height, int color, int textSize) {
        this.text = text;
        this.color = color;
        this.width = width;
        this.height = height;
        this.menuOrRetry = menuOrRetry;
        this.textSize = textSize;
    }

    public int setMenuColor() {
        if (colorMode == false) return color;
        else return Color.BLACK;
    }


    public String setText() {
        if (menuOrRetry == 1) return "Color Pong";
        else if (menuOrRetry == 2) return "Play again?";
        else return text;
    }

    public void blankGameMenu(boolean mode) {
        colorMode = mode;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setFakeBoldText(true);
        paint.setColor(setMenuColor());
        canvas.drawText(setText(), width, height , paint);
    }

    @Override
    public void update() {

    }

    public void update(Canvas canvas) {

    }

}
