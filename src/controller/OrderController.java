package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CoffeeShopService;

import java.io.IOException;

import static listener.AppInitListener.COFFEE_SHOP_SERVICE;

/** The public pickup board: every order, visible to anyone, no login needed to look at it. */
public class OrderController extends HttpServlet {

    private CoffeeShopService coffeeShopService;

    @Override
    public void init() {
        coffeeShopService = (CoffeeShopService) getServletContext().getAttribute(COFFEE_SHOP_SERVICE);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("orders", coffeeShopService.getOrders());
        request.getRequestDispatcher("/WEB-INF/views/orders.jsp").forward(request, response);
    }
}
