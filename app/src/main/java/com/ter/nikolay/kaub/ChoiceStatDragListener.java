package com.ter.nikolay.kaub;

import android.util.Log;
import android.view.DragEvent;
import android.view.View;

/**
 * Created by nikolay on 14.06.2015.
 */
public class ChoiceStatDragListener implements View.OnDragListener {
    private static final String TAG = "myLogs";
    private static Float FirstCoord = (float) 0;
    @Override
    public boolean onDrag(View v, DragEvent dragEvent) {
        Float coord = dragEvent.getX();
         switch (dragEvent.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                FirstCoord = coord;
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                //no action necessary
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                //no action necessary
                break;
            case DragEvent.ACTION_DROP:
                Log.i(TAG, "ACTION_DROP:!");
                if(FirstCoord>0 && Math.abs(FirstCoord-coord)>(float) 50){
                    MainActivity.instanse.deleteStatByView(v);
                }
                FirstCoord = (float) 0;
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                break;
            default:
                break;
        }


        return true;
    }
}