package model;

/** One coffee on the shop's fixed menu. */
public class Coffee {

    private final String type;
    private final double price;

    public Coffee(String type, double price) {
        this.type = type;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }
}
