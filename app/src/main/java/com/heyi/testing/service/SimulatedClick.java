package com.heyi.testing.service;

import android.app.Instrumentation;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;

/**
 * SimulatedClick class
 *
 * @author auser
 * @date 11/2/2019
 */
public class SimulatedClick extends Thread {
    private float x,y;
    //400,689
    @Override
    public void run() {
        // 可以不用在 Activity 中增加任何处理，各 Activity 都可以响应
        try {
            Instrumentation inst = new Instrumentation();
            inst.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),SystemClock.uptimeMillis(),
                    MotionEvent.ACTION_DOWN, x, y, 0));
            inst.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),SystemClock.uptimeMillis(),
                    MotionEvent.ACTION_UP, x, y, 0));
            Log.e("点击位置", x+","+y);
        }catch(Exception e) {
            Log.e("Exception when sendPointerSync", e.toString());
        }
    }
    public SimulatedClick(float x,float y){
        this.x=x;
        this.y=y;
    }

}
