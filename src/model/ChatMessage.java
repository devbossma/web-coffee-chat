package model;

import java.time.LocalDateTime;

/** One line of a chat session's transcript. */
public class ChatMessage {

    private final int sessionId;
    private final MessageSender sender;
    private final String text;
    private final LocalDateTime sentAt;

    public ChatMessage(int sessionId, MessageSender sender, String text) {
        this.sessionId = sessionId;
        this.sender = sender;
        this.text = text;
        this.sentAt = LocalDateTime.now();
    }

    public int getSessionId() {
        return sessionId;
    }

    public MessageSender getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }
}
