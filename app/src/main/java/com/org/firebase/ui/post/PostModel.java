package com.org.firebase.ui.post;

import java.util.List;

public class PostModel {
    private String postContent;
    private String postTitle;
    private String imagepath;
    private String userId;
    private String userName;
    private String date;

    public String getDateWithTime() {
        return dateWithTime;
    }

    public void setDateWithTime(String dateWithTime) {
        this.dateWithTime = dateWithTime;
    }

    private String dateWithTime;
    private boolean isLiked;
    private String postId;

    private List<String> postLikedUser;


    public String getUserName() {
        return userName;
    }

    public List<String> getPostLikedUser() {
        return postLikedUser;
    }

    public void setPostLikedUser(List<String> postLikedUser) {
        this.postLikedUser = postLikedUser;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostId() {
        return postId;
    }
}
