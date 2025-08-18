package com.pahanaedu.controller;

import com.pahanaedu.model.Customer;
import com.pahanaedu.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/customers/*")
public class CustomerApiServlet extends HttpServlet {

    private CustomerService customerService;
    private ObjectMapper objectMapper;

    @Override
    public void init() {
        customerService = new CustomerService();
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("CustomerApiServlet doGet called");
        System.out.println("Path info: " + request.getPathInfo());
        System.out.println("Request URI: " + request.getRequestURI());

        if (!isAuthenticated(request)) {
            System.out.println("Authentication failed");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        response.setContentType("application/json");
        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                System.out.println("Getting all customers");
                // Get all customers
                List<Customer> customers = customerService.getAllCustomers();
                response.getWriter().write(objectMapper.writeValueAsString(customers));
            } else if (pathInfo.equals("/count")) {
                System.out.println("Getting customer count");
                // Get customer count for dashboard
                int count = customerService.getCustomerCount();
                response.getWriter().write("{\"count\": " + count + "}");
            } else {
                // Get specific customer by ID
                String[] pathParts = pathInfo.split("/");
                if (pathParts.length == 2) {
                    int customerId = Integer.parseInt(pathParts[1]);
                    Customer customer = customerService.getCustomerById(customerId);
                    if (customer != null) {
                        response.getWriter().write(objectMapper.writeValueAsString(customer));
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        response.getWriter().write("{\"success\": false, \"message\": \"Customer not found\"}");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error in CustomerApiServlet: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Error retrieving customers\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAuthenticated(request)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        response.setContentType("application/json");

        try {
            Customer customer = objectMapper.readValue(request.getReader(), Customer.class);
            boolean success = customerService.addCustomer(customer);

            if (success) {
                response.getWriter().write("{\"success\": true, \"message\": \"Customer added successfully\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\": false, \"message\": \"Failed to add customer\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Error adding customer\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAuthenticated(request)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        response.setContentType("application/json");
        String pathInfo = request.getPathInfo();

        try {
            String[] pathParts = pathInfo.split("/");
            if (pathParts.length == 2) {
                int customerId = Integer.parseInt(pathParts[1]);
                Customer customer = objectMapper.readValue(request.getReader(), Customer.class);
                customer.setId(customerId);

                boolean success = customerService.updateCustomer(customer);

                if (success) {
                    response.getWriter().write("{\"success\": true, \"message\": \"Customer updated successfully\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"success\": false, \"message\": \"Failed to update customer\"}");
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Error updating customer\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAuthenticated(request)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        response.setContentType("application/json");
        String pathInfo = request.getPathInfo();

        try {
            String[] pathParts = pathInfo.split("/");
            if (pathParts.length == 2) {
                int customerId = Integer.parseInt(pathParts[1]);
                boolean success = customerService.deleteCustomer(customerId);

                if (success) {
                    response.getWriter().write("{\"success\": true, \"message\": \"Customer deleted successfully\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"success\": false, \"message\": \"Failed to delete customer\"}");
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Error deleting customer\"}");
        }
    }

    private boolean isAuthenticated(HttpServletRequest request) {
        // Temporarily bypass authentication for development
        return true;
        
        // Original authentication code (commented out for now)
        // HttpSession session = request.getSession(false);
        // return session != null && session.getAttribute("loggedUser") != null;
    }
}