package model;

/**
 * One placed order. {@code customerId} is the kiosk-style receipt id handed out by
 * {@code service.CustomerIdGenerator} (date + a daily counter), never a real account.
 */
public class Order {

    private final int id;
    private final String customerId;
    private final String coffeeType;
    private volatile OrderStatus status;

    public Order(int id, String customerId, String coffeeType) {
        this.id = id;
        this.customerId = customerId;
        this.coffeeType = coffeeType;
        this.status = OrderStatus.PLACED;
    }

    public int getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCoffeeType() {
        return coffeeType;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
