package listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import service.BaristaAuthService;
import service.ChatService;
import service.CoffeeShopService;
import service.CustomerIdGenerator;

/**
 * Creates the one shared instance of each service and stores it on the {@code ServletContext},
 * so every servlet's {@code init()} pulls the same instance rather than each servlet building
 * its own, disconnected copy. Registered declaratively in web.xml, not via {@code @WebListener},
 * to keep deployment wiring in one place.
 */
public class AppInitListener implements ServletContextListener {

    public static final String COFFEE_SHOP_SERVICE = "coffeeShopService";
    public static final String CHAT_SERVICE = "chatService";
    public static final String CUSTOMER_ID_GENERATOR = "customerIdGenerator";
    public static final String BARISTA_AUTH_SERVICE = "baristaAuthService";

    @Override
    public void contextInitialized(ServletContextEvent event) {
        var context = event.getServletContext();
        context.setAttribute(COFFEE_SHOP_SERVICE, new CoffeeShopService());
        context.setAttribute(CHAT_SERVICE, new ChatService());
        context.setAttribute(CUSTOMER_ID_GENERATOR, new CustomerIdGenerator());
        context.setAttribute(BARISTA_AUTH_SERVICE, new BaristaAuthService());
    }
}
