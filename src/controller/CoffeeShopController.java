package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.ChatSession;
import model.Order;
import service.ChatService;
import service.CoffeeShopService;
import service.CustomerIdGenerator;

import java.io.IOException;

import static listener.AppInitListener.CHAT_SERVICE;
import static listener.AppInitListener.COFFEE_SHOP_SERVICE;
import static listener.AppInitListener.CUSTOMER_ID_GENERATOR;

/**
 * The menu/landing page. Every visitor is handed a {@code customerId} (the date+counter receipt
 * id) in their {@code HttpSession} the first time they arrive, there is no sign-up, no name, this
 * id is the only thing that will ever identify them.
 */
public class CoffeeShopController extends HttpServlet {

    private CoffeeShopService coffeeShopService;
    private ChatService chatService;
    private CustomerIdGenerator customerIdGenerator;

    @Override
    public void init() {
        coffeeShopService = (CoffeeShopService) getServletContext().getAttribute(COFFEE_SHOP_SERVICE);
        chatService = (ChatService) getServletContext().getAttribute(CHAT_SERVICE);
        customerIdGenerator = (CustomerIdGenerator) getServletContext().getAttribute(CUSTOMER_ID_GENERATOR);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ensureCustomerId(request);
        request.setAttribute("coffees", coffeeShopService.getCoffees());
        request.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String customerId = ensureCustomerId(request);
        String coffeeType = request.getParameter("coffeeType");

        try {
            Order order = coffeeShopService.placeOrder(coffeeType, customerId);
            ChatSession chat = chatService.startSession(customerId);
            chatService.linkOrder(chat.getId(), order.getId());
            request.getSession().setAttribute("chatSessionId", chat.getId());
            response.sendRedirect(request.getContextPath() + "/chat");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("coffees", coffeeShopService.getCoffees());
            request.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(request, response);
        }
    }

    private String ensureCustomerId(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String customerId = (String) session.getAttribute("customerId");
        if (customerId == null) {
            customerId = customerIdGenerator.nextId();
            session.setAttribute("customerId", customerId);
        }
        return customerId;
    }
}
