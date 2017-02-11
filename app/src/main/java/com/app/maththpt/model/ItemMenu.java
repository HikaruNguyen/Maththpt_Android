package com.app.maththpt.model;

/**
 * Created by manhi on 2/7/2016.
 */

public class ItemMenu {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_HOME = 1;
    public static final int TYPE_BODY = 2;

    public int type;
    public boolean isSelected;
    public String title;
    public int icon;

    public ItemMenu() {
        type = TYPE_HEADER;
    }

    public ItemMenu(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public ItemMenu(String title, int icon, int type) {
        this.title = title;
        this.icon = icon;
        this.type = type;
        this.isSelected = false;
    }

    public ItemMenu(String title, int icon, int type, boolean isSelected) {
        this.title = title;
        this.icon = icon;
        this.type = type;
        this.isSelected = isSelected;
    }
}
