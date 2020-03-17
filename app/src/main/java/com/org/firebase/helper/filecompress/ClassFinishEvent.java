package com.org.firebase.helper.filecompress;


import com.org.firebase.data.models.FolderDetailItem;

public class ClassFinishEvent {

    private boolean isFinished;
    private FolderDetailItem item;

    public ClassFinishEvent(boolean isFinished, FolderDetailItem item) {
        this.isFinished = isFinished;
        this.item = item;
    }

    public FolderDetailItem getItem() {
        return item;
    }

    public void setItem(FolderDetailItem item) {
        this.item = item;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }
}
