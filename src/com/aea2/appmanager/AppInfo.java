package com.aea2.appmanager;

import android.graphics.drawable.Drawable;

public class AppInfo {

    private String name; // 应用名
    private String packageName; // 包名
    private String versionName; // 版本名
    private Drawable icon; // 应用图标
    public long codeSize;
    public long dataSize;
    public long cacheSize;
    public long appSize;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

}
