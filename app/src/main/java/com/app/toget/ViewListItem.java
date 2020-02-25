package com.app.toget;

import java.io.Serializable;

public class ViewListItem implements Serializable {

    //Item name
    private final String itemName;
    //Boolean for whether or not the item has a strikethrough for being purchased or not
    private boolean isStrikeThrough;
    //Item detail information
    private String itemDetail;
    //Boolean for whether or not the item has an item detail or not
    private boolean hasDetail;

    //Constructor
    public ViewListItem(String itemName, boolean isStrikeThrough, String itemDetail, boolean hasDetail) {
        this.itemName = itemName;
        this.isStrikeThrough = isStrikeThrough;
        this.itemDetail = itemDetail;
        this.hasDetail = hasDetail;
    }

    //Setters and Getters
    public String getItemName() {
        return itemName;
    }

    public boolean getIsStrikeThrough() {
        return isStrikeThrough;
    }

    public void setStrikeThrough(boolean strikeThrough) {
        this.isStrikeThrough = strikeThrough;
    }

    public String getItemDetail() {
        return itemDetail;
    }

    public void setItemDetail(String detail) {
        this.itemDetail = detail;
    }

    public boolean hasDetail() {
        return hasDetail;
    }

    public void setHasDetail(boolean hasDetail) {
        this.hasDetail = hasDetail;
    }
}
