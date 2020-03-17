package com.org.firebase.data.models;


public class FolderItem {

    private String folderName;
    private String folderIcon;
    private String folderId;

    public FolderItem(String folderName, String folderIcon, String folderId) {
        this.folderName = folderName;
        this.folderIcon = folderIcon;
        this.folderId = folderId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderIcon() {
        return folderIcon;
    }

    public void setFolderIcon(String folderIcon) {
        this.folderIcon = folderIcon;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }
}