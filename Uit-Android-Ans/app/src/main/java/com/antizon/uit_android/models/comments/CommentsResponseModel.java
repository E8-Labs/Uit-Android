package com.antizon.uit_android.models.comments;

import com.google.gson.annotations.SerializedName;

import org.w3c.dom.Comment;

import java.util.List;

public class CommentsResponseModel {
    @SerializedName("status")
    private boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<CommentDataModel> commentsList;

    public CommentsResponseModel() {
    }

    public CommentsResponseModel(boolean status, String message, List<CommentDataModel> commentsList) {
        this.status = status;
        this.message = message;
        this.commentsList = commentsList;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CommentDataModel> getCommentsList() {
        return commentsList;
    }

    public void setCommentsList(List<CommentDataModel> commentsList) {
        this.commentsList = commentsList;
    }
}
