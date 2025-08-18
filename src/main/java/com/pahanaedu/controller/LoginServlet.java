package com.pahanaedu.controller;

import com.pahanaedu.dao.UserDAO;
import com.pahanaedu.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("Login servlet called..");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Use DAO to check user credentials
        User user = userDAO.loginUser(email, password);

        if (user != null) {
            System.out.println("Login success..");
            // Login success → start session
            HttpSession session = request.getSession();
            session.setAttribute("loggedUser", user);
            session.setAttribute("userEmail", user.getEmail());
            session.setAttribute("userName", user.getName());

            // Set response headers to indicate success
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": true, \"message\": \"Login successful\"}");
        } else {
            System.out.println("Login failed..");
            // Login failed → return error
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"Invalid email or password!\"}");
        }
    }
}