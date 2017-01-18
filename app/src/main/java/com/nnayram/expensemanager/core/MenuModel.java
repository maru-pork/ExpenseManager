package com.nnayram.expensemanager.core;

/**
 * Created by Rufo on 1/14/2017.
 */
public class MenuModel {
    private String title;
    private String description;
    private int iconId;

    public MenuModel(String title, String description, int iconId) {
        this.title = title;
        this.description = description;
        this.iconId = iconId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }
}
