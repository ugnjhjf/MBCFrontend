package com.example.friendlist;

public class Message {
    private String senderUID;
    private String receiverUID;
    private String content;
    private String time; // 暂留，可能会用到 (吗?

    public Message(String senderUID, String receiverUID, String content) {
        this.senderUID = senderUID;
        this.receiverUID = receiverUID;
        this.content = content;
   }

    public String getSender() {
        return senderUID;
    }

    public String getReceiver() {
        return receiverUID;
    }

    public String getContent() {
        return content;
    }


    public void setSenderUID(String senderUID) {
        this.senderUID = senderUID;
    }

    public void setReceiverUID(String receiverUID) {
        this.receiverUID = receiverUID;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
