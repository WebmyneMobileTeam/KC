package com.webmyne.kidscrown.model;

/**
 * Created by vatsaldesai on 25-01-2017.
 */

public class VersionModel {


    /**
     * DeviceType : Android
     * IsMendatory : true
     * LatestVersion : 1.9
     * YourVersion : 1.9
     */

    private String DeviceType;
    private boolean IsMendatory;
    private String LatestVersion;
    private String YourVersion;

    public String getDeviceType() {
        return DeviceType;
    }

    public void setDeviceType(String DeviceType) {
        this.DeviceType = DeviceType;
    }

    public boolean isIsMendatory() {
        return IsMendatory;
    }

    public void setIsMendatory(boolean IsMendatory) {
        this.IsMendatory = IsMendatory;
    }

    public String getLatestVersion() {
        return LatestVersion;
    }

    public void setLatestVersion(String LatestVersion) {
        this.LatestVersion = LatestVersion;
    }

    public String getYourVersion() {
        return YourVersion;
    }

    public void setYourVersion(String YourVersion) {
        this.YourVersion = YourVersion;
    }
}
