package com.example.outof;

import java.io.Serializable;

class MakeListItem implements Serializable {

    private final String itemName;
    private boolean selected;

    public MakeListItem(String name, boolean selected) {
        this.itemName = name;
        this.selected = selected;
    }

    public String getItemName() {
        return itemName;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
