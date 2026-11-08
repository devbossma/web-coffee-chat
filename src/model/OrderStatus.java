package model;

/** An order's lifecycle: placed by the customer, paid for, then handed over. */
public enum OrderStatus {
    PLACED,
    PAID,
    FULFILLED
}
