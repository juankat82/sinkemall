package com.juan.android.sinkemall;

/**
 * Created by juan on 01/02/18.
 */

public class Turn {

    private int mTurnNumber;
    private int mTimesHit;
    private int mTimesHitting;
    private int mWhoHitnext;//0 means YOU, 1 COM

    public Turn()
    {
        this(0,0,0,0);
    }
    private Turn(int turnNumber,int timesHit,int timesHitting,int whoHitNext)
    {
        mTurnNumber=turnNumber;
        mTimesHit=timesHit;
        mTimesHitting=timesHitting;
        mWhoHitnext=whoHitNext;
    }
    public int getmTurnNumber() {
        return mTurnNumber;
    }

    public void setmTurnNumber(int turnNumber) {
        mTurnNumber = turnNumber;
    }

    public int getmTimesHit() {
        return mTimesHit;
    }

    public void setmTimesHit(int timesHit) {
        mTimesHit = timesHit;
    }

    public int getmTimesHitting() {
        return mTimesHitting;
    }

    public void setmTimesHitting(int timesHitting) {
        mTimesHitting = timesHitting;
    }

    public int getWhoHitnext() { return mWhoHitnext; }

    public void setmWhohitNext(int whoHitnext) { mWhoHitnext=whoHitnext; }
}
