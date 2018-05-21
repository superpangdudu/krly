package com.krly.project.batterymanagement.batteryserver;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/17.
 */
public class BatteryInfo implements Serializable {
    private String containerId;
    private long id = -1;
    private int status;
    private int slot;

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }
}
