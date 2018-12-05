package com.juan.android.sinkemall;

/**
 * Created by juan on 12/11/17.
 */

public class Ranks {

    private int mId;
    private String name;
    private long points;
    private boolean machineDefault;

    public boolean isMachineDefault() {
        return machineDefault;
    }

    public void setMachineDefault(boolean machineDefault) {
        this.machineDefault = machineDefault;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }
}
