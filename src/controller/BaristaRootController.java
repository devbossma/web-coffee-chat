package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** Sends a barista to wherever they actually belong: the console if already logged in, the login form otherwise. */
public class BaristaRootController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean loggedIn = request.getSession(true).getAttribute("barista") != null;
        String target = loggedIn ? "/barista/console" : "/barista/login";
        response.sendRedirect(request.getContextPath() + target);
    }
}
