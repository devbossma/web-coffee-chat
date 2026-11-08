package model;

/**
 * One customer's chat with "the barista" (there is exactly one shared barista console, no
 * per-barista identity or matching, this is a kiosk, not a multi-counter shop). A session starts
 * the moment a customer opens the chat and closes the moment its linked order is marked
 * {@link OrderStatus#FULFILLED}.
 */
public class ChatSession {

    private final int id;
    private final String customerId;
    private volatile Integer orderId;
    private volatile ChatSessionStatus status;

    public ChatSession(int id, String customerId) {
        this.id = id;
        this.customerId = customerId;
        this.orderId = null;
        this.status = ChatSessionStatus.ACTIVE;
    }

    public int getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public ChatSessionStatus getStatus() {
        return status;
    }

    public void setStatus(ChatSessionStatus status) {
        this.status = status;
    }
}
