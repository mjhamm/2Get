package com.app.toget;

import java.io.Serializable;

public class ViewListItem implements Serializable {

    private final String itemName;
    private boolean isStrikeThrough;
    private String itemDetail;
    private boolean hasDetail;

    public ViewListItem(String itemName, boolean isStrikeThrough, String itemDetail, boolean hasDetail) {
        this.itemName = itemName;
        this.isStrikeThrough = isStrikeThrough;
        this.itemDetail = itemDetail;
        this.hasDetail = hasDetail;
    }

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
