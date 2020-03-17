package com.org.firebase.helper.filecompress;


import com.org.firebase.data.models.FolderDetailItem;

public class ImageSelectionEvent {
    private FolderDetailItem item;



    public ImageSelectionEvent(FolderDetailItem item) {
        this.item = item;

    }

    public FolderDetailItem getItem() {
        return item;
    }


    public void setItem(FolderDetailItem item) {
        this.item = item;
    }
}