package com.android.hospitalapplication.UtilityAndNetworkingClasses;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Gaurav on 28-12-2017.
 */

public class OnSwipeListener implements View.OnTouchListener {

    private final GestureDetector mGestureListener;

    public OnSwipeListener(Context ctx) {
        mGestureListener = new GestureDetector(ctx,new GestureListener()) ;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return mGestureListener.onTouchEvent(motionEvent);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener{

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            boolean handeled = false;

            try{
                float diffX = e2.getX()-e1.getX();
                float diffY = e2.getY()-e1.getY();

                if(Math.abs(diffX)>Math.abs(diffY)){
                    if(Math.abs(diffX)>SWIPE_THRESHOLD && Math.abs(velocityX)>SWIPE_VELOCITY_THRESHOLD){
                                  if(diffX>0){
                                      onSwipeRight();
                                  }
                                  else{
                                      onSwipeLeft();
                                  }
                                  handeled=true;
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
           return handeled;
        }

    }

    public void onSwipeRight(){

    }

    public void onSwipeLeft(){

    }
}
