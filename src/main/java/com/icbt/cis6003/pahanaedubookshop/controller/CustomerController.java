package com.icbt.cis6003.pahanaedubookshop.controller;

import com.icbt.cis6003.pahanaedubookshop.model.Customer;
import com.icbt.cis6003.pahanaedubookshop.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonArrayBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

/**
 * Simple servlet controller for Customer operations
 */
@WebServlet(name = "CustomerController", urlPatterns = {"/api/customers/*"})
public class CustomerController extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("CustomerController initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (pathInfo == null || pathInfo.equals("/")) {
            // Get all customers or search
            String searchTerm = request.getParameter("search");
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                handleSearchCustomers(request, response, searchTerm);
            } else {
                handleGetAllCustomers(request, response);
            }
        } else if (pathInfo.matches("/\\d+")) {
            // Get customer by ID
            Long customerId = Long.parseLong(pathInfo.substring(1));
            handleGetCustomerById(request, response, customerId);
        } else if (pathInfo.equals("/count")) {
            // Get customer count
            handleGetCustomerCount(request, response);
        } else if (pathInfo.equals("/recent")) {
            // Get recent customers
            int limit = Integer.parseInt(request.getParameter("limit") != null ?
                       request.getParameter("limit") : "5");
            handleGetRecentCustomers(request, response, limit);
        } else if (pathInfo.equals("/statistics")) {
            // Get customer statistics
            handleGetCustomerStatistics(request, response);
        } else {
            sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Endpoint not found");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Create new customer
        Customer customer = parseCustomerFromRequest(request);
        handleCreateCustomer(request, response, customer);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (pathInfo != null && pathInfo.matches("/\\d+")) {
            Long customerId = Long.parseLong(pathInfo.substring(1));
            Customer customer = parseCustomerFromRequest(request);
            customer.setId(customerId);
            handleUpdateCustomer(request, response, customer);
        } else {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST,
                            "Customer ID required for update");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (pathInfo != null && pathInfo.matches("/\\d+")) {
            Long customerId = Long.parseLong(pathInfo.substring(1));
            handleDeleteCustomer(request, response, customerId);
        } else {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST,
                            "Customer ID required for deletion");
        }
    }
    
    // Handler methods
    
    private void handleGetAllCustomers(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        // Mock data for now - replace with actual service call
        JsonArrayBuilder customersArray = Json.createArrayBuilder();
        
        // Add sample customers
        customersArray.add(createCustomerJson(1L, "CUST001", "John Doe", 
                          "123 Main St, Colombo 01", "+94 77 123 4567", 
                          "john.doe@email.com", "2024-01-15"));
        customersArray.add(createCustomerJson(2L, "CUST002", "Jane Smith", 
                          "456 Galle Road, Colombo 03", "+94 71 987 6543", 
                          "jane.smith@email.com", "2024-02-20"));
        
        JsonObject responseJson = Json.createObjectBuilder()
                .add("success", true)
                .add("data", customersArray)
                .add("message", "Customers retrieved successfully")
                .build();
        
        sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
    }
    
    private void handleGetCustomerById(HttpServletRequest request, HttpServletResponse response, 
                                     Long customerId) throws IOException {
        // Mock data for now - replace with actual service call
        if (customerId == 1L) {
            JsonObject customer = createCustomerJson(1L, "CUST001", "John Doe", 
                                   "123 Main St, Colombo 01", "+94 77 123 4567", 
                                   "john.doe@email.com", "2024-01-15");
            
            JsonObject responseJson = Json.createObjectBuilder()
                    .add("success", true)
                    .add("data", customer)
                    .add("message", "Customer retrieved successfully")
                    .build();
            
            sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
        } else {
            sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Customer not found");
        }
    }
    
    private void handleSearchCustomers(HttpServletRequest request, HttpServletResponse response, 
                                     String searchTerm) throws IOException {
        // Mock search results - replace with actual service call
        JsonArrayBuilder customersArray = Json.createArrayBuilder();
        
        if (searchTerm.toLowerCase().contains("john")) {
            customersArray.add(createCustomerJson(1L, "CUST001", "John Doe", 
                              "123 Main St, Colombo 01", "+94 77 123 4567", 
                              "john.doe@email.com", "2024-01-15"));
        }
        
        JsonObject responseJson = Json.createObjectBuilder()
                .add("success", true)
                .add("data", customersArray)
                .add("message", "Search completed successfully")
                .build();
        
        sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
    }
    
    private void handleCreateCustomer(HttpServletRequest request, HttpServletResponse response, 
                                    Customer customer) throws IOException {
        // Mock creation - replace with actual service call
        JsonObject customerJson = createCustomerJson(3L, customer.getAccountNumber(), 
                                  customer.getName(), customer.getAddress(), 
                                  customer.getPhone(), customer.getEmail(), 
                                  LocalDate.now().toString());
        
        JsonObject responseJson = Json.createObjectBuilder()
                .add("success", true)
                .add("data", customerJson)
                .add("message", "Customer created successfully")
                .build();
        
        sendJsonResponse(response, HttpServletResponse.SC_CREATED, responseJson);
    }
    
    private void handleUpdateCustomer(HttpServletRequest request, HttpServletResponse response, 
                                    Customer customer) throws IOException {
        // Mock update - replace with actual service call
        JsonObject customerJson = createCustomerJson(customer.getId(), customer.getAccountNumber(), 
                                  customer.getName(), customer.getAddress(), 
                                  customer.getPhone(), customer.getEmail(), 
                                  "2024-01-15");
        
        JsonObject responseJson = Json.createObjectBuilder()
                .add("success", true)
                .add("data", customerJson)
                .add("message", "Customer updated successfully")
                .build();
        
        sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
    }
    
    private void handleDeleteCustomer(HttpServletRequest request, HttpServletResponse response, 
                                    Long customerId) throws IOException {
        // Mock deletion - replace with actual service call
        JsonObject responseJson = Json.createObjectBuilder()
                .add("success", true)
                .add("message", "Customer deleted successfully")
                .build();
        
        sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
    }
    
    private void handleGetCustomerCount(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        // Mock count - replace with actual service call
        JsonObject responseJson = Json.createObjectBuilder()
                .add("success", true)
                .add("data", Json.createObjectBuilder().add("count", 25))
                .add("message", "Customer count retrieved successfully")
                .build();
        
        sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
    }
    
    private void handleGetRecentCustomers(HttpServletRequest request, HttpServletResponse response, 
                                        int limit) throws IOException {
        // Mock recent customers - replace with actual service call
        JsonArrayBuilder customersArray = Json.createArrayBuilder();
        customersArray.add(createCustomerJson(2L, "CUST002", "Jane Smith", 
                          "456 Galle Road, Colombo 03", "+94 71 987 6543", 
                          "jane.smith@email.com", "2024-02-20"));
        
        JsonObject responseJson = Json.createObjectBuilder()
                .add("success", true)
                .add("data", customersArray)
                .add("message", "Recent customers retrieved successfully")
                .build();
        
        sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
    }
    
    private void handleGetCustomerStatistics(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        // Mock statistics - replace with actual service call
        JsonObject statistics = Json.createObjectBuilder()
                .add("totalCustomers", 25)
                .add("newThisMonth", 5)
                .add("newToday", 1)
                .add("growthRate", 12.5)
                .build();
        
        JsonObject responseJson = Json.createObjectBuilder()
                .add("success", true)
                .add("data", statistics)
                .add("message", "Customer statistics retrieved successfully")
                .build();
        
        sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
    }
    
    // Utility methods
    
    private Customer parseCustomerFromRequest(HttpServletRequest request) throws IOException {
        // In a real application, you would parse JSON from request body
        // For now, we'll get parameters from form data or query string
        Customer customer = new Customer();
        customer.setAccountNumber(request.getParameter("accountNumber"));
        customer.setName(request.getParameter("name"));
        customer.setAddress(request.getParameter("address"));
        customer.setPhone(request.getParameter("phone"));
        customer.setEmail(request.getParameter("email"));
        return customer;
    }
    
    private JsonObject createCustomerJson(Long id, String accountNumber, String name, 
                                        String address, String phone, String email, String registrationDate) {
        return Json.createObjectBuilder()
                .add("id", id)
                .add("accountNumber", accountNumber)
                .add("name", name)
                .add("address", address)
                .add("phone", phone)
                .add("email", email != null ? email : "")
                .add("registrationDate", registrationDate)
                .build();
    }
    
    private void sendJsonResponse(HttpServletResponse response, int statusCode, JsonObject jsonObject) 
            throws IOException {
        response.setStatus(statusCode);
        try (PrintWriter out = response.getWriter()) {
            out.print(jsonObject.toString());
            out.flush();
        }
    }
    
    private void sendErrorResponse(HttpServletResponse response, int statusCode, String message)
            throws IOException {
        JsonObject errorJson = JsonUtil.createErrorResponse(message);
        sendJsonResponse(response, statusCode, errorJson);
    }
}
