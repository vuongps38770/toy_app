package com.project1.toystoreapp.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MovableButton extends FloatingActionButton {

    private float dX, dY;
    private int screenWidth, screenHeight;

    public MovableButton(Context context) {
        super(context);
        init(context);
    }

    public MovableButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MovableButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dX = getX() - event.getRawX();
                dY = getY() - event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                float newX = event.getRawX() + dX;
                float newY = event.getRawY() + dY;

                newX = Math.max(0, Math.min(newX, screenWidth - getWidth()));
                newY = Math.max(0, Math.min(newY, screenHeight - getHeight()));

                animate()
                        .x(newX)
                        .y(newY)
                        .setDuration(0)
                        .start();
                break;

            default:
                return false;
        }
        return true;
    }
}
