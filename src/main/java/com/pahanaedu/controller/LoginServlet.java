package com.pahanaedu.controller;

import com.pahanaedu.dao.UserDAO;
import com.pahanaedu.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/login") // URL pattern for the login form POST
public class LoginServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO(); // initialize DAO
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get form data from login.jsp
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Use DAO to check user credentials
        User user = userDAO.loginUser(email, password);

        if (user != null) {
            // Login success → start session
            HttpSession session = request.getSession();
            session.setAttribute("loggedUser", user);

            // Redirect to dashboard or welcome page
            response.sendRedirect("dashboard.jsp");
        } else {
            // Login failed → show error message
            request.setAttribute("error", "Invalid email or password!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
