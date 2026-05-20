package com.lld.patterns.behavioral.mediator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

interface Mediator {
    // Mediator interface only exposes coordination contract — send. Receive belongs to Colleague.
    void send(User sender, String message);
}

class ChatRoom implements Mediator {
    private List<User> users;

    public ChatRoom() {
        this.users = new ArrayList<>();
    }

    @Override
    public void send(User sender, String message) {
        for (User user : users) {
            // LEARNING: User should not send message to itself.
            if (!Objects.equals(user, sender)) {
                user.receive(sender.getUserName(), message);
            }
        }
    }

    public void addUser(User user) {
        users.add(user);
        System.out.println(user.getUserName() + " has been added");
    }

    public void removeUser(User user) {
        users.remove(user);
        System.out.println(user.getUserName() + " has left the chat");
    }

}

class User {
    // LEARNING: User hold reference of mediator interface not concrete class which help in extending our code to other rooms.
    private Mediator mediator;
    private String userName;

    public User(Mediator mediator, String userName) {
        this.mediator = mediator;
        this.userName = userName;
    }

    void send(String message) {
        // LEARNING: Passing self user by this keyword.
        mediator.send(this, message);
    }

    void receive(String senderName, String message) {
        System.out.println(this.userName + "'s POV: " + senderName + " : " + message);
    }

    public String getUserName() {
        return userName;
    }
}

public class MediatorDemo {

    public static void main(String[] args) {
        ChatRoom chatRoom = new ChatRoom();
        User user1 = new User(chatRoom, "Alex");

        User user2 = new User(chatRoom, "Carry");

        User user3 = new User(chatRoom, "James");

        chatRoom.addUser(user1);
        chatRoom.addUser(user2);
        chatRoom.addUser(user3);

        user1.send("Hi guys! hows it's going?");
        user3.send("I am not liking this group, I am leaving");

        // LEARNING: After removing user3 , user3 will not be able to get any kind of chat.
        chatRoom.removeUser(user3);

        user2.send("Why? James has left the chat?");
    }
}
