package com.icbt.cis6003.pahanaedubookshop.controller;

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

@WebServlet(name = "BillingController", urlPatterns = {"/api/bills/*"})
public class BillingController extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("BillingController initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (pathInfo == null || pathInfo.equals("/")) {
            // Get all bills or filter by customer/date
            String customerId = request.getParameter("customerId");
            String date = request.getParameter("date");

            if (customerId != null) {
                handleGetBillsByCustomer(request, response, Long.parseLong(customerId));
            } else if (date != null) {
                handleGetBillsByDate(request, response, LocalDate.parse(date));
            } else {
                handleGetAllBills(request, response);
            }
        } else if (pathInfo.matches("/\\d+")) {
            // Get bill by ID
            Long billId = Long.parseLong(pathInfo.substring(1));
            handleGetBillById(request, response, billId);
        } else if (pathInfo.equals("/recent")) {
            // Get recent bills
            int limit = Integer.parseInt(request.getParameter("limit") != null ?
                       request.getParameter("limit") : "10");
            handleGetRecentBills(request, response, limit);
        } else if (pathInfo.equals("/today")) {
            // Get today's bills
            handleGetTodaysBills(request, response);
        } else if (pathInfo.equals("/statistics")) {
            // Get sales statistics
            handleGetSalesStatistics(request, response);
        } else if (pathInfo.equals("/sales-total")) {
            // Get sales totals
            String period = request.getParameter("period"); // today, month, year
            handleGetSalesTotal(request, response, period);
        } else {
            sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Endpoint not found");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // Create new bill
                String customerIdStr = request.getParameter("customerId");
                if (customerIdStr != null) {
                    Long customerId = Long.parseLong(customerIdStr);
                    handleCreateBill(request, response, customerId);
                } else {
                    sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, 
                                    "Customer ID required");
                }
            } else if (pathInfo.matches("/\\d+/items")) {
                // Add item to bill
                Long billId = Long.parseLong(pathInfo.substring(1, pathInfo.indexOf("/items")));
                Long itemId = Long.parseLong(request.getParameter("itemId"));
                Integer quantity = Integer.parseInt(request.getParameter("quantity"));
                handleAddItemToBill(request, response, billId, itemId, quantity);
            } else if (pathInfo.matches("/\\d+/finalize")) {
                // Finalize bill
                Long billId = Long.parseLong(pathInfo.substring(1, pathInfo.indexOf("/finalize")));
                handleFinalizeBill(request, response, billId);
            } else {
                sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, 
                                "Invalid endpoint");
            }
        } catch (Exception e) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST,
                            "Invalid request data");
        }
    }
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            if (pathInfo != null && pathInfo.matches("/\\d+/items/\\d+")) {
                // Update item quantity in bill
                String[] parts = pathInfo.split("/");
                Long billId = Long.parseLong(parts[1]);
                Long itemId = Long.parseLong(parts[3]);
                Integer newQuantity = Integer.parseInt(request.getParameter("quantity"));
                handleUpdateItemQuantity(request, response, billId, itemId, newQuantity);
            } else {
                sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, 
                                "Invalid endpoint for update");
            }
        } catch (Exception e) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST,
                            "Invalid request data");
        }
    }
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            if (pathInfo != null && pathInfo.matches("/\\d+")) {
                // Cancel/delete bill
                Long billId = Long.parseLong(pathInfo.substring(1));
                handleCancelBill(request, response, billId);
            } else if (pathInfo != null && pathInfo.matches("/\\d+/items/\\d+")) {
                // Remove item from bill
                String[] parts = pathInfo.split("/");
                Long billId = Long.parseLong(parts[1]);
                Long itemId = Long.parseLong(parts[3]);
                handleRemoveItemFromBill(request, response, billId, itemId);
            } else {
                sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, 
                                "Invalid endpoint for deletion");
            }
        } catch (Exception e) {
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            "Internal server error");
        }
    }
    
    // Handler methods
    
    private void handleGetAllBills(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        // Mock data for now - replace with actual service call
        JsonArrayBuilder billsArray = Json.createArrayBuilder();
        
        // Add sample bills
        billsArray.add(createBillJson(1L, 1L, "John Doe", "CUST001", "2024-08-17", 
                      75.50, 7.55, 83.05, "FINALIZED"));
        billsArray.add(createBillJson(2L, 2L, "Jane Smith", "CUST002", "2024-08-16", 
                      45.25, 4.53, 49.78, "FINALIZED"));
        
        JsonObject responseJson = Json.createObjectBuilder()
                .add("success", true)
                .add("data", billsArray)
                .add("message", "Bills retrieved successfully")
                .build();
        
        sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
    }
    
    private void handleGetBillById(HttpServletRequest request, HttpServletResponse response, 
                                 Long billId) throws IOException {
        // Mock data for now - replace with actual service call
        if (billId == 1L) {
            JsonObject bill = createBillJson(1L, 1L, "John Doe", "CUST001", "2024-08-17", 
                            75.50, 7.55, 83.05, "FINALIZED");
            
            JsonObject responseJson = Json.createObjectBuilder()
                    .add("success", true)
                    .add("data", bill)
                    .add("message", "Bill retrieved successfully")
                    .build();
            
            sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
        } else {
            sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Bill not found");
        }
    }
    
    private void handleGetBillsByCustomer(HttpServletRequest request, HttpServletResponse response, 
                                        Long customerId) throws IOException {
        // Mock data for now - replace with actual service call
        JsonArrayBuilder billsArray = Json.createArrayBuilder();
        
        if (customerId == 1L) {
            billsArray.add(createBillJson(1L, 1L, "John Doe", "CUST001", "2024-08-17", 
                          75.50, 7.55, 83.05, "FINALIZED"));
        }
        
        JsonObject responseJson = Json.createObjectBuilder()
                .add("success", true)
                .add("data", billsArray)
                .add("message", "Customer bills retrieved successfully")
                .build();
        
        sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
    }
    
    private void handleGetBillsByDate(HttpServletRequest request, HttpServletResponse response, 
                                    LocalDate date) throws IOException {
        // Mock data for now - replace with actual service call
        JsonArrayBuilder billsArray = Json.createArrayBuilder();
        
        if (date.equals(LocalDate.parse("2024-08-17"))) {
            billsArray.add(createBillJson(1L, 1L, "John Doe", "CUST001", "2024-08-17", 
                          75.50, 7.55, 83.05, "FINALIZED"));
        }
        
        JsonObject responseJson = Json.createObjectBuilder()
                .add("success", true)
                .add("data", billsArray)
                .add("message", "Bills for date retrieved successfully")
                .build();
        
        sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
    }
    
    private void handleGetRecentBills(HttpServletRequest request, HttpServletResponse response, 
                                    int limit) throws IOException {
        // Mock data for now - replace with actual service call
        JsonArrayBuilder billsArray = Json.createArrayBuilder();
        billsArray.add(createBillJson(2L, 2L, "Jane Smith", "CUST002", "2024-08-16", 
                      45.25, 4.53, 49.78, "FINALIZED"));
        
        JsonObject responseJson = Json.createObjectBuilder()
                .add("success", true)
                .add("data", billsArray)
                .add("message", "Recent bills retrieved successfully")
                .build();
        
        sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
    }
    
    private void handleGetTodaysBills(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        // Mock data for now - replace with actual service call
        JsonArrayBuilder billsArray = Json.createArrayBuilder();
        billsArray.add(createBillJson(1L, 1L, "John Doe", "CUST001", "2024-08-17", 
                      75.50, 7.55, 83.05, "FINALIZED"));
        
        JsonObject responseJson = Json.createObjectBuilder()
                .add("success", true)
                .add("data", billsArray)
                .add("message", "Today's bills retrieved successfully")
                .build();
        
        sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
    }
    
    private void handleGetSalesStatistics(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        // Mock statistics - replace with actual service call
        JsonObject statistics = Json.createObjectBuilder()
                .add("totalBills", 125)
                .add("todaysBills", 8)
                .add("monthlyBills", 45)
                .add("totalSales", 15750.25)
                .add("todaysSales", 425.50)
                .add("monthlySales", 6250.75)
                .add("averageBillAmount", 126.00)
                .build();
        
        JsonObject responseJson = Json.createObjectBuilder()
                .add("success", true)
                .add("data", statistics)
                .add("message", "Sales statistics retrieved successfully")
                .build();
        
        sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
    }
    
    private void handleGetSalesTotal(HttpServletRequest request, HttpServletResponse response, 
                                   String period) throws IOException {
        // Mock sales totals - replace with actual service call
        double total = 0.0;
        String message = "";
        
        switch (period != null ? period : "today") {
            case "today":
                total = 425.50;
                message = "Today's sales total";
                break;
            case "month":
                total = 6250.75;
                message = "Monthly sales total";
                break;
            case "year":
                total = 15750.25;
                message = "Yearly sales total";
                break;
            default:
                total = 425.50;
                message = "Today's sales total";
        }
        
        JsonObject responseJson = Json.createObjectBuilder()
                .add("success", true)
                .add("data", Json.createObjectBuilder()
                    .add("period", period != null ? period : "today")
                    .add("total", total))
                .add("message", message + " retrieved successfully")
                .build();
        
        sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
    }
    
    private void handleCreateBill(HttpServletRequest request, HttpServletResponse response, 
                                Long customerId) throws IOException {
        // Mock creation - replace with actual service call
        JsonObject billJson = createBillJson(3L, customerId, "New Customer", "CUST003", 
                              LocalDate.now().toString(), 0.0, 0.0, 0.0, "DRAFT");
        
        JsonObject responseJson = Json.createObjectBuilder()
                .add("success", true)
                .add("data", billJson)
                .add("message", "Bill created successfully")
                .build();
        
        sendJsonResponse(response, HttpServletResponse.SC_CREATED, responseJson);
    }
    
    private void handleAddItemToBill(HttpServletRequest request, HttpServletResponse response, 
                                   Long billId, Long itemId, Integer quantity) throws IOException {
        // Mock item addition - replace with actual service call
        JsonObject responseJson = Json.createObjectBuilder()
                .add("success", true)
                .add("data", Json.createObjectBuilder()
                    .add("billId", billId)
                    .add("itemId", itemId)
                    .add("quantity", quantity))
                .add("message", "Item added to bill successfully")
                .build();
        
        sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
    }
    
    private void handleFinalizeBill(HttpServletRequest request, HttpServletResponse response, 
                                  Long billId) throws IOException {
        // Mock finalization - replace with actual service call
        JsonObject responseJson = Json.createObjectBuilder()
                .add("success", true)
                .add("data", Json.createObjectBuilder().add("billId", billId))
                .add("message", "Bill finalized successfully")
                .build();
        
        sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
    }
    
    private void handleUpdateItemQuantity(HttpServletRequest request, HttpServletResponse response, 
                                        Long billId, Long itemId, Integer newQuantity) throws IOException {
        // Mock quantity update - replace with actual service call
        JsonObject responseJson = Json.createObjectBuilder()
                .add("success", true)
                .add("data", Json.createObjectBuilder()
                    .add("billId", billId)
                    .add("itemId", itemId)
                    .add("newQuantity", newQuantity))
                .add("message", "Item quantity updated successfully")
                .build();
        
        sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
    }
    
    private void handleRemoveItemFromBill(HttpServletRequest request, HttpServletResponse response, 
                                        Long billId, Long itemId) throws IOException {
        // Mock item removal - replace with actual service call
        JsonObject responseJson = Json.createObjectBuilder()
                .add("success", true)
                .add("message", "Item removed from bill successfully")
                .build();
        
        sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
    }
    
    private void handleCancelBill(HttpServletRequest request, HttpServletResponse response, 
                                Long billId) throws IOException {
        // Mock cancellation - replace with actual service call
        JsonObject responseJson = Json.createObjectBuilder()
                .add("success", true)
                .add("message", "Bill cancelled successfully")
                .build();
        
        sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
    }
    
    // Utility methods
    
    private JsonObject createBillJson(Long id, Long customerId, String customerName, 
                                    String customerAccount, String date, double subtotal, 
                                    double tax, double total, String status) {
        return Json.createObjectBuilder()
                .add("id", id)
                .add("customerId", customerId)
                .add("customerName", customerName)
                .add("customerAccount", customerAccount)
                .add("date", date)
                .add("subtotal", subtotal)
                .add("tax", tax)
                .add("total", total)
                .add("status", status)
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
