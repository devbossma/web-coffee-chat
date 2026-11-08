package service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Hands out kiosk-style receipt ids such as {@code 20261107-003}: today's date plus a counter
 * that resets to 1 the first time a new day is seen. There are no customer accounts or names
 * anywhere in this app, this id is the only thing that identifies "who" an order or chat session
 * belongs to.
 *
 * <p>{@code nextId()} is a single {@code synchronized} method because it is a read-then-maybe-reset
 * of {@code lastDate} followed by an increment of {@code counter}: two related fields that must
 * change together, which is exactly the case a plain {@code AtomicInteger} cannot cover on its
 * own (it only makes a single counter's own increment atomic, not "increment this counter unless
 * the date just rolled over, in which case reset it first"). The servlet container runs each
 * request on its own thread, so two customers starting a chat at the same instant are a real
 * possibility here, not a hypothetical.
 */
public class CustomerIdGenerator {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.BASIC_ISO_DATE;

    private LocalDate lastDate;
    private int counter;

    public synchronized String nextId() {
        LocalDate today = LocalDate.now();
        if (!today.equals(lastDate)) {
            lastDate = today;
            counter = 0;
        }
        counter++;
        return today.format(DATE_FORMAT) + "-" + String.format("%03d", counter);
    }
}
