package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.ChatSession;
import model.ChatSessionStatus;
import model.MessageSender;
import service.ChatService;
import service.CoffeeShopService;

import java.io.IOException;
import java.util.Optional;

import static listener.AppInitListener.CHAT_SERVICE;
import static listener.AppInitListener.COFFEE_SHOP_SERVICE;

/**
 * The customer side of the chat. There is no push here, nothing in plain Servlets/JSP provides
 * one, so the view polls this endpoint on a short meta-refresh and this is where a closed session
 * actually gets noticed: once the linked order is fulfilled and the session flips to
 * {@code CLOSED}, the next poll redirects back to the menu instead of rendering the chat.
 */
public class ChatController extends HttpServlet {

    private ChatService chatService;
    private CoffeeShopService coffeeShopService;

    @Override
    public void init() {
        chatService = (ChatService) getServletContext().getAttribute(CHAT_SERVICE);
        coffeeShopService = (CoffeeShopService) getServletContext().getAttribute(COFFEE_SHOP_SERVICE);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer chatSessionId = (Integer) session.getAttribute("chatSessionId");

        Optional<ChatSession> chat = chatSessionId == null
                ? Optional.empty()
                : chatService.findSession(chatSessionId);

        if (chat.isEmpty() || chat.get().getStatus() == ChatSessionStatus.CLOSED) {
            session.removeAttribute("chatSessionId");
            response.sendRedirect(request.getContextPath() + "/coffeeshop");
            return;
        }

        request.setAttribute("chatSession", chat.get());
        request.setAttribute("messages", chatService.getMessages(chatSessionId));
        if (chat.get().getOrderId() != null) {
            coffeeShopService.findOrder(chat.get().getOrderId())
                    .ifPresent(order -> request.setAttribute("order", order));
        }
        request.getRequestDispatcher("/WEB-INF/views/chat.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Integer chatSessionId = (Integer) session.getAttribute("chatSessionId");
        String text = request.getParameter("text");

        if (chatSessionId != null && text != null && !text.isBlank()) {
            chatService.postMessage(chatSessionId, MessageSender.CUSTOMER, text.trim());
        }
        response.sendRedirect(request.getContextPath() + "/chat");
    }
}
