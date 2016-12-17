package com.kingdevelopers.www.vehicledatabase.model;

/**
 * Created by saibaba on 12/15/2016.
 */
public class DisView {
    private int type;
    private String displayText;
    private DisView() {
    }

    public DisView(String name, int type) {
        this.displayText = name;
        this.type = type;

    }

    public String getDisplayText() {
        return displayText;
    }

    public int getType() {
        return type;
    }
}
