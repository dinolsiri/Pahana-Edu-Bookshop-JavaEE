package com.icbt.cis6003.pahanaedubookshop.controller;

import com.icbt.cis6003.pahanaedubookshop.model.Customer;
import com.icbt.cis6003.pahanaedubookshop.service.CustomerService;
import com.icbt.cis6003.pahanaedubookshop.service.impl.CustomerServiceImpl;
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
import java.util.List;
import java.util.Optional;

/**
 * Servlet controller for Customer operations
 */
@WebServlet(name = "CustomerController", urlPatterns = {"/api/customers/*"})
public class CustomerController extends HttpServlet {

    private CustomerService customerService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.customerService = new CustomerServiceImpl();
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
        try {
            List<Customer> customers = customerService.getAllCustomers();
            JsonArrayBuilder customersArray = Json.createArrayBuilder();

            for (Customer customer : customers) {
                customersArray.add(createCustomerJson(customer));
            }

            JsonObject responseJson = JsonUtil.createSuccessResponse("Customers retrieved successfully", customersArray);
            sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
        } catch (Exception e) {
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            "Error retrieving customers: " + e.getMessage());
        }
    }
    
    private void handleGetCustomerById(HttpServletRequest request, HttpServletResponse response,
                                     Long customerId) throws IOException {
        try {
            Optional<Customer> customerOpt = customerService.getCustomerById(customerId);
            if (customerOpt.isPresent()) {
                JsonObject customerJson = createCustomerJson(customerOpt.get());
                JsonObject responseJson = JsonUtil.createSuccessResponse("Customer retrieved successfully", customerJson);
                sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
            } else {
                sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Customer not found");
            }
        } catch (Exception e) {
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            "Error retrieving customer: " + e.getMessage());
        }
    }
    
    private void handleSearchCustomers(HttpServletRequest request, HttpServletResponse response,
                                     String searchTerm) throws IOException {
        try {
            List<Customer> customers = customerService.searchCustomers(searchTerm);
            JsonArrayBuilder customersArray = Json.createArrayBuilder();

            for (Customer customer : customers) {
                customersArray.add(createCustomerJson(customer));
            }

            JsonObject responseJson = JsonUtil.createSuccessResponse("Search completed successfully", customersArray);
            sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
        } catch (Exception e) {
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            "Error searching customers: " + e.getMessage());
        }
    }
    
    private void handleCreateCustomer(HttpServletRequest request, HttpServletResponse response,
                                    Customer customer) throws IOException {
        try {
            Customer createdCustomer = customerService.createCustomer(customer);
            JsonObject customerJson = createCustomerJson(createdCustomer);
            JsonObject responseJson = JsonUtil.createSuccessResponse("Customer created successfully", customerJson);
            sendJsonResponse(response, HttpServletResponse.SC_CREATED, responseJson);
        } catch (Exception e) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST,
                            "Error creating customer: " + e.getMessage());
        }
    }
    
    private void handleUpdateCustomer(HttpServletRequest request, HttpServletResponse response,
                                    Customer customer) throws IOException {
        try {
            Customer updatedCustomer = customerService.updateCustomer(customer);
            JsonObject customerJson = createCustomerJson(updatedCustomer);
            JsonObject responseJson = JsonUtil.createSuccessResponse("Customer updated successfully", customerJson);
            sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
        } catch (Exception e) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST,
                            "Error updating customer: " + e.getMessage());
        }
    }
    
    private void handleDeleteCustomer(HttpServletRequest request, HttpServletResponse response,
                                    Long customerId) throws IOException {
        try {
            boolean deleted = customerService.deleteCustomer(customerId);
            if (deleted) {
                JsonObject responseJson = Json.createObjectBuilder()
                    .add("success", true)
                    .add("message", "Customer deleted successfully")
                    .build();
                sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
            } else {
                sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Customer not found");
            }
        } catch (Exception e) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST,
                            "Error deleting customer: " + e.getMessage());
        }
    }
    
    private void handleGetCustomerCount(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            long count = customerService.getTotalCustomerCount();
            JsonObject data = Json.createObjectBuilder().add("count", count).build();
            JsonObject responseJson = JsonUtil.createSuccessResponse("Customer count retrieved successfully", data);
            sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
        } catch (Exception e) {
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            "Error retrieving customer count: " + e.getMessage());
        }
    }

    private void handleGetRecentCustomers(HttpServletRequest request, HttpServletResponse response,
                                        int limit) throws IOException {
        try {
            List<Customer> customers = customerService.getRecentCustomers(limit);
            JsonArrayBuilder customersArray = Json.createArrayBuilder();

            for (Customer customer : customers) {
                customersArray.add(createCustomerJson(customer));
            }

            JsonObject responseJson = JsonUtil.createSuccessResponse("Recent customers retrieved successfully", customersArray);
            sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
        } catch (Exception e) {
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            "Error retrieving recent customers: " + e.getMessage());
        }
    }

    private void handleGetCustomerStatistics(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            CustomerService.CustomerStatistics stats = customerService.getCustomerStatistics();
            JsonObject statistics = Json.createObjectBuilder()
                    .add("totalCustomers", stats.getTotalCustomers())
                    .add("newThisMonth", stats.getNewThisMonth())
                    .add("newToday", stats.getNewToday())
                    .add("growthRate", stats.getGrowthRate())
                    .build();

            JsonObject responseJson = JsonUtil.createSuccessResponse("Customer statistics retrieved successfully", statistics);
            sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
        } catch (Exception e) {
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            "Error retrieving customer statistics: " + e.getMessage());
        }
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

        // Generate account number if not provided
        if (customer.getAccountNumber() == null || customer.getAccountNumber().trim().isEmpty()) {
            customer.setAccountNumber(customerService.generateNextAccountNumber());
        }

        return customer;
    }

    private JsonObject createCustomerJson(Customer customer) {
        return Json.createObjectBuilder()
                .add("id", customer.getId() != null ? customer.getId() : 0)
                .add("accountNumber", customer.getAccountNumber() != null ? customer.getAccountNumber() : "")
                .add("name", customer.getName() != null ? customer.getName() : "")
                .add("address", customer.getAddress() != null ? customer.getAddress() : "")
                .add("phone", customer.getPhone() != null ? customer.getPhone() : "")
                .add("email", customer.getEmail() != null ? customer.getEmail() : "")
                .add("registrationDate", customer.getRegistrationDate() != null ? customer.getRegistrationDate().toString() : "")
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
