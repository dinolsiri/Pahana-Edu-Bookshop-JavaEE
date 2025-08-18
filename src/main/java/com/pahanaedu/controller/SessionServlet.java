package com.pahanaedu.controller;

import com.pahanaedu.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/api/auth/session")
public class SessionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        response.setContentType("application/json");

        if (session != null && session.getAttribute("loggedUser") != null) {
            User user = (User) session.getAttribute("loggedUser");
            response.getWriter().write(String.format(
                "{\"success\": true, \"user\": {\"id\": %d, \"name\": \"%s\", \"email\": \"%s\"}}",
                user.getId(), user.getName(), user.getEmail()
            ));
        } else {
            response.getWriter().write("{\"success\": false, \"message\": \"No active session\"}");
        }
    }
}