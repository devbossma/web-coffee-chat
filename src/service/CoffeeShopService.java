package service;

import model.Coffee;
import model.Order;
import model.OrderStatus;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Everything lives in memory for the lifetime of the application, there is no database: a fixed
 * coffee menu and a running list of orders, exactly like the assignment's own example. The order
 * list and id counter are the only state written from more than one request at a time (a customer
 * placing an order, a barista marking one paid or fulfilled), so a {@link CopyOnWriteArrayList}
 * and an {@link AtomicInteger} are enough, no locking of our own is needed since every write is a
 * single call on one of those two.
 */
public class CoffeeShopService {

    private final List<Coffee> coffees;
    private final List<Order> orders = new CopyOnWriteArrayList<>();
    private final AtomicInteger orderIdCounter = new AtomicInteger(1);

    public CoffeeShopService() {
        coffees = List.of(
                new Coffee("Espresso", 2.50),
                new Coffee("Americano", 2.75),
                new Coffee("Macchiato", 3.25),
                new Coffee("Cappuccino", 3.50),
                new Coffee("Latte", 3.75),
                new Coffee("Mocha", 4.00));
    }

    public List<Coffee> getCoffees() {
        return coffees;
    }

    /** @throws IllegalArgumentException if coffeeType is not on the menu */
    public Order placeOrder(String coffeeType, String customerId) {
        boolean onMenu = coffees.stream().anyMatch(c -> c.getType().equals(coffeeType));
        if (!onMenu) {
            throw new IllegalArgumentException("Not on the menu: " + coffeeType);
        }
        Order order = new Order(orderIdCounter.getAndIncrement(), customerId, coffeeType);
        orders.add(order);
        return order;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public Optional<Order> findOrder(int orderId) {
        return orders.stream().filter(o -> o.getId() == orderId).findFirst();
    }

    public void markPaid(int orderId) {
        findOrder(orderId).ifPresent(o -> o.setStatus(OrderStatus.PAID));
    }

    public void markFulfilled(int orderId) {
        findOrder(orderId).ifPresent(o -> o.setStatus(OrderStatus.FULFILLED));
    }
}
