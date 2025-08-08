package com.pahanaedu.controller;

import com.pahanaedu.service.BillingService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/api/bills/*")
public class BillApiServlet extends HttpServlet {

    private BillingService billingService;

    @Override
    public void init() {
        billingService = new BillingService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAuthenticated(request)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        response.setContentType("application/json");
        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo.equals("/count")) {
                int count = billingService.getBillCount();
                response.getWriter().write("{\"count\": " + count + "}");
            } else if (pathInfo.equals("/monthly-sales")) {
                BigDecimal monthlySales = billingService.getMonthlySales();
                response.getWriter().write("{\"total\": " + monthlySales + "}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Error retrieving bill data\"}");
        }
    }

    private boolean isAuthenticated(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("loggedUser") != null;
    }
}