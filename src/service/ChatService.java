package service;

import model.ChatMessage;
import model.ChatSession;
import model.ChatSessionStatus;
import model.MessageSender;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * There is one shared barista console, not a pool of baristas to match against, so this holds
 * just a flat list of sessions and a transcript per session, nothing resembling the queue/matching
 * machinery a multi-barista shop would need.
 */
public class ChatService {

    private final List<ChatSession> sessions = new CopyOnWriteArrayList<>();
    private final Map<Integer, List<ChatMessage>> messagesBySession = new ConcurrentHashMap<>();
    private final AtomicInteger sessionIdCounter = new AtomicInteger(1);

    public ChatSession startSession(String customerId) {
        ChatSession session = new ChatSession(sessionIdCounter.getAndIncrement(), customerId);
        sessions.add(session);
        messagesBySession.put(session.getId(), new CopyOnWriteArrayList<>());
        return session;
    }

    public Optional<ChatSession> findSession(int sessionId) {
        return sessions.stream().filter(s -> s.getId() == sessionId).findFirst();
    }

    public List<ChatSession> getActiveSessions() {
        return sessions.stream().filter(s -> s.getStatus() == ChatSessionStatus.ACTIVE).toList();
    }

    public void linkOrder(int sessionId, int orderId) {
        findSession(sessionId).ifPresent(s -> s.setOrderId(orderId));
    }

    public void postMessage(int sessionId, MessageSender sender, String text) {
        messagesBySession.computeIfAbsent(sessionId, id -> new CopyOnWriteArrayList<>())
                .add(new ChatMessage(sessionId, sender, text));
    }

    public List<ChatMessage> getMessages(int sessionId) {
        return messagesBySession.getOrDefault(sessionId, List.of());
    }

    /** Called when an order is marked fulfilled: the chat it came from has nothing left to say. */
    public void closeSessionByOrderId(int orderId) {
        sessions.stream()
                .filter(s -> s.getOrderId() != null && s.getOrderId() == orderId)
                .forEach(s -> s.setStatus(ChatSessionStatus.CLOSED));
    }
}
