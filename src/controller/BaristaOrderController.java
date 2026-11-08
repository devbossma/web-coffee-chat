package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ChatService;
import service.CoffeeShopService;

import java.io.IOException;

import static listener.AppInitListener.CHAT_SERVICE;
import static listener.AppInitListener.COFFEE_SHOP_SERVICE;

/**
 * Where the barista moves an order through {@code PLACED -> PAID -> FULFILLED}. Marking an order
 * fulfilled is the one moment this app "sends an event": it closes that order's chat session
 * right here, a plain method call, not a real event bus, there is nothing else listening.
 */
public class BaristaOrderController extends HttpServlet {

    private CoffeeShopService coffeeShopService;
    private ChatService chatService;

    @Override
    public void init() {
        coffeeShopService = (CoffeeShopService) getServletContext().getAttribute(COFFEE_SHOP_SERVICE);
        chatService = (ChatService) getServletContext().getAttribute(CHAT_SERVICE);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("orders", coffeeShopService.getOrders());
        request.getRequestDispatcher("/WEB-INF/views/barista-orders.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        String action = request.getParameter("action");

        if ("markPaid".equals(action)) {
            coffeeShopService.markPaid(orderId);
        } else if ("markFulfilled".equals(action)) {
            coffeeShopService.markFulfilled(orderId);
            chatService.closeSessionByOrderId(orderId);
        }
        response.sendRedirect(request.getContextPath() + "/barista/orders");
    }
}
