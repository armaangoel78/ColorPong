package com.armaangoel.myapplication;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by sunjaygoel on 9/2/16.
 */
public class Obstacle implements GameObject{

    private Rect rectangle;
    private int color;


    public Obstacle(Rect rectangle, int color) {
        this.rectangle = rectangle;
        this.color = color;
    }

    public boolean playerCollide(RectPlayer player) {
        if (rectangle.contains(player.getRectangle().left, player.getRectangle().top)
            || rectangle.contains(player.getRectangle().right, player.getRectangle().top)
            || rectangle.contains(player.getRectangle().left, player.getRectangle().bottom)
            || rectangle.contains(player.getRectangle().right, player.getRectangle().bottom))
        {
            return true;
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle, paint);
    }
    @Override
    public void update() {

    }


}
