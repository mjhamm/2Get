package com.example.outof;

import java.io.Serializable;

public class ViewListItem implements Serializable {

    private String itemName;
    private boolean isStrikeThrough = false;

    public ViewListItem(String itemName, boolean isStrikeThrough) {
        this.itemName = itemName;
        this.isStrikeThrough = isStrikeThrough;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public boolean getIsStrikeThrough() {
        return isStrikeThrough;
    }

    public void setStrikeThrough(boolean strikeThrough) {
        this.isStrikeThrough = strikeThrough;
    }
}
