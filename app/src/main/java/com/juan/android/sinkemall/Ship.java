package com.juan.android.sinkemall;

import android.graphics.PointF;

/**
 * Created by juan on 05/12/17.
 */

public class Ship {

    private static final String TAG="SHIP";
    private int mId;
    private PointF mOriginPoint;
    private PointF mFinishPoint;
    private boolean isIn=false;
    private boolean isVisible=false;

    public Ship(int id) {

        mId=id;
    }

    public int getId() {
        return mId;
    }

    public PointF getOriginPoint() {
        return mOriginPoint;
    }

    public void setOriginPoint(PointF originPoint) {
        mOriginPoint = originPoint;
    }

    public PointF getFinishPoint() {
        return mFinishPoint;
    }

    public void setFinishPoint(PointF finishPoint) {
        mFinishPoint = finishPoint;
    }

    public boolean isIn() {
        return isIn;
    }

    public void setIn(boolean in) {
        isIn = in;
    }

    public void setVisible(boolean visible)
    {
        isVisible=visible;
    }
    public boolean isVisible()
    {
        return isVisible;
    }


}
