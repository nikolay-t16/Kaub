package com.ter.nikolay.kaub;

import android.content.ClipData;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by nikolay on 14.06.2015.
 */
public class ChoiceTouchStatListener implements View.OnTouchListener {
    private static final String TAG = "myLogs";

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

            //setup drag
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

            //start dragging the item touched
            view.startDrag(data, shadowBuilder, view, 0);
            return true;
        }
        else {
            return false;
        }
    }
}