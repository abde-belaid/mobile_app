package com.example.chat_app.messages;

public class MessagesList {
    private String name,phone,lastMessage,profilePic, chatKey;
    private int unseenMessages;

    public MessagesList(String name, String phone, String lastMessage, String profilePic, int unseenMessages,String chatKey) {
        this.name = name;
        this.phone = phone;
        this.lastMessage = lastMessage;
        this.profilePic = profilePic;
        this.unseenMessages = unseenMessages;
        this.chatKey = chatKey;

    }



    public String getChatKey() {
        return chatKey;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public int getUnseenMessages() {
        return unseenMessages;
    }
}
