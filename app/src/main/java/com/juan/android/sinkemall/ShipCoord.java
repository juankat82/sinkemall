package com.juan.android.sinkemall;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juan on 20/12/17.
 */

public class ShipCoord {

    private int mId;
    private String mName;
    private List<Integer> x;
    private List<Integer> y;
    private int mColor;
    private int x1=0,y1=0;

    public ShipCoord(int id, String name)
    {
            mId=id;
            mName=name;
            y=new ArrayList<>();
            x=new ArrayList<>();
    }
    public void setColor(int color)
    {
        mColor=color;
    }
    public int getColor()
    {
        return mColor;
    }
    public void addPosition(int col,int row)
    {
        x1=col;
        y1=row;
        x.add(col);
        y.add(row);
    }
    public int getx1()
    {
        return x1;
    }
    public int gety1()
    {
        return y1;
    }
    public int getId()
    {
        return mId;
    }
    public List<Integer> getPositionsX()
    {
        return x;
    }
    public List<Integer> getPositionsY()
    {
        return y;
    }
}
