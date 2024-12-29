package net.idalya.core.player.notifications;

import org.bson.Document;

import java.util.Date;
import java.util.UUID;

public class Notification {

    private final UUID receiver;
    private final String sender;
    private final String message;
    private final NotificationType type;
    private final Date when;
    private final int id;

    public Notification(UUID receiver, String sender, String message, NotificationType type, Date when, int id) {
        this.receiver = receiver;
        this.sender = sender;
        this.message = message;
        this.type = type;
        this.when = when;
        this.id = id;
    }

    public Document toBson() {
        return new Document().append("receiver", this.receiver.toString())
                .append("sender", this.sender)
                .append("message", this.message)
                .append("type", this.type.name())
                .append("date", this.when)
                .append("id", this.id);
    }

    public static Notification fromDocument(Document document) {
        return new Notification(UUID.fromString(document.getString("receiver")),
                document.getString("sender"),
                document.getString("message")
                , NotificationType.valueOf(document.getString("type")),
                document.getDate("date"),
                document.getInteger("id"));
    }

    public UUID getReceiver() {
        return receiver;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public NotificationType getType() {
        return type;
    }

    public Date getWhen() {
        return when;
    }

    public int getId() {
        return id;
    }
}
