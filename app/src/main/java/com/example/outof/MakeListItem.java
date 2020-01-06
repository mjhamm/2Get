package com.example.outof;

public class MakeListItem {

    String itemName;
    boolean isSelected = false;

    public MakeListItem(String name, boolean selected) {
        this.itemName = name;
        this.isSelected = selected;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
