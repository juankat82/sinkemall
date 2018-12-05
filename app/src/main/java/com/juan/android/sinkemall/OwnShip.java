package com.juan.android.sinkemall;

/**
 * Created by juan on 21/02/18.
 */

public class OwnShip {

    private int mId;//id whatever shipCompOne blahblah and so on
    private int mIndexA;//Number >=0 for real index, <0 means theres no such index (ship can be less than 4 indexes). Atleast this one will be filled
    private int mIndexB;//same
    private int mIndexC;//same
    private int mIndexD;//same
    private String mIndexAState;//O or X   >>>>> X default
    private String mIndexBState;//O or X
    private String mIndexCState;//O or X
    private String mIndexDState;//O or X
    private int mColor; //color anything
    private String mState;//when all the indexes ar O, this will be DOWN, otherwise UP

//////////////////////////////////////////////////////////////////////////////////////////

    public void setmId(int id) {
        mId = id;
    }
    public void setmIndexA(int indexA) {
        mIndexA = indexA;
    }
    public void setmIndexB(int indexB) {
        mIndexB = indexB;
    }
    public void setmIndexC(int indexC) {
        mIndexC = indexC;
    }
    public void setmIndexD(int indexD) {mIndexD = indexD;}
    public void setmIndexAState(String indexAState) {
        mIndexAState = indexAState;
    }
    public void setmIndexBState(String indexBState) {
        mIndexBState = indexBState;
    }
    public void setmIndexCState(String indexCState) {
        mIndexCState = indexCState;
    }
    public void setmIndexDState(String indexDState) {
        mIndexDState = indexDState;
    }
    public void setmColor(int color) {
        mColor = color;
    }
    public void setmState(String state) {
        mState = state;
    }


    public int getmId() {
        return mId;
    }
    public int getmIndexA() {
        return mIndexA;
    }
    public int getmIndexB() {
        return mIndexB;
    }
    public int getmIndexC() {
        return mIndexC;
    }
    public int getmIndexD() {
        return mIndexD;
    }
    public String getmIndexAState() {
        return mIndexAState;
    }
    public String getmIndexBState() {
        return mIndexBState;
    }
    public String getmIndexCState() {
        return mIndexCState;
    }
    public String getmIndexDState() {
        return mIndexDState;
    }
    public int getmColor() {
        return mColor;
    }
    public String getmState() {
        return mState;
    }
}
