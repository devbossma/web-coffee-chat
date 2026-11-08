package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.MessageSender;
import service.ChatService;

import java.io.IOException;

import static listener.AppInitListener.CHAT_SERVICE;

/**
 * The one shared barista chat console: every active session is listed, whoever is logged in can
 * open any of them and reply. There is no per-barista assignment, matching the kiosk's single
 * counter.
 */
public class BaristaConsoleController extends HttpServlet {

    private ChatService chatService;

    @Override
    public void init() {
        chatService = (ChatService) getServletContext().getAttribute(CHAT_SERVICE);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("sessions", chatService.getActiveSessions());

        String sessionIdParam = request.getParameter("sessionId");
        if (sessionIdParam != null) {
            int sessionId = Integer.parseInt(sessionIdParam);
            chatService.findSession(sessionId).ifPresent(chat -> {
                request.setAttribute("selectedSession", chat);
                request.setAttribute("messages", chatService.getMessages(sessionId));
            });
        }
        request.getRequestDispatcher("/WEB-INF/views/barista-console.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int sessionId = Integer.parseInt(request.getParameter("sessionId"));
        String text = request.getParameter("text");

        if (text != null && !text.isBlank()) {
            chatService.postMessage(sessionId, MessageSender.BARISTA, text.trim());
        }
        response.sendRedirect(request.getContextPath() + "/barista/console?sessionId=" + sessionId);
    }
}
