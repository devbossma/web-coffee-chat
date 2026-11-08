package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.BaristaAuthService;

import java.io.IOException;

import static listener.AppInitListener.BARISTA_AUTH_SERVICE;

/** Deliberately minimal: one shared in-memory credential pair, no accounts, no password hashing. */
public class BaristaLoginController extends HttpServlet {

    private BaristaAuthService baristaAuthService;

    @Override
    public void init() {
        baristaAuthService = (BaristaAuthService) getServletContext().getAttribute(BARISTA_AUTH_SERVICE);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/barista-login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (baristaAuthService.authenticate(username, password)) {
            request.getSession().setAttribute("barista", username);
            response.sendRedirect(request.getContextPath() + "/barista/console");
        } else {
            request.setAttribute("error", "Wrong username or password");
            request.getRequestDispatcher("/WEB-INF/views/barista-login.jsp").forward(request, response);
        }
    }
}
