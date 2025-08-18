package com.icbt.cis6003.pahanaedubookshop.controller;

import com.icbt.cis6003.pahanaedubookshop.model.Item;
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
import java.math.BigDecimal;

/**
 * Simple servlet controller for Item operations
 */
@WebServlet(name = "ItemController", urlPatterns = {"/api/items/*"})
public class ItemController extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("ItemController initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (pathInfo == null || pathInfo.equals("/")) {
            // Get all items or search
            String searchTerm = request.getParameter("search");
            String category = request.getParameter("category");

            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                handleSearchItems(request, response, searchTerm, category);
            } else if (category != null && !category.trim().isEmpty()) {
                handleGetItemsByCategory(request, response, category);
            } else {
                handleGetAllItems(request, response);
            }
        } else if (pathInfo.matches("/\\d+")) {
            // Get item by ID
            Long itemId = Long.parseLong(pathInfo.substring(1));
            handleGetItemById(request, response, itemId);
        } else if (pathInfo.equals("/count")) {
            // Get item count
            handleGetItemCount(request, response);
        } else if (pathInfo.equals("/low-stock")) {
            // Get low stock items
            handleGetLowStockItems(request, response);
        } else if (pathInfo.equals("/in-stock")) {
            // Get items in stock
            handleGetItemsInStock(request, response);
        } else if (pathInfo.equals("/statistics")) {
            // Get inventory statistics
            handleGetInventoryStatistics(request, response);
        } else if (pathInfo.equals("/categories")) {
            // Get available categories
            handleGetCategories(request, response);
        } else {
            sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Endpoint not found");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            // Create new item
            Item item = parseItemFromRequest(request);
            handleCreateItem(request, response, item);
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
            if (pathInfo != null && pathInfo.matches("/\\d+")) {
                Long itemId = Long.parseLong(pathInfo.substring(1));
                Item item = parseItemFromRequest(request);
                item.setId(itemId);
                handleUpdateItem(request, response, item);
            } else if (pathInfo != null && pathInfo.matches("/\\d+/stock")) {
                // Update stock
                Long itemId = Long.parseLong(pathInfo.substring(1, pathInfo.indexOf("/stock")));
                Integer newStock = Integer.parseInt(request.getParameter("stock"));
                handleUpdateStock(request, response, itemId, newStock);
            } else {
                sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, 
                                "Item ID required for update");
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
                Long itemId = Long.parseLong(pathInfo.substring(1));
                handleDeleteItem(request, response, itemId);
            } else {
                sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, 
                                "Item ID required for deletion");
            }
        } catch (Exception e) {
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            "Internal server error");
        }
    }
    
    // Handler methods
    
    private void handleGetAllItems(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        // Mock data for now - replace with actual service call
        JsonArrayBuilder itemsArray = Json.createArrayBuilder();
        
        // Add sample items
        itemsArray.add(createItemJson(1L, "BOOK001", "Mathematics Grade 10", "textbook", 
                      25.99, 50, 10, "Grade 10 Mathematics textbook"));
        itemsArray.add(createItemJson(2L, "BOOK002", "English Literature", "textbook", 
                      22.50, 30, 5, "English Literature reference book"));
        itemsArray.add(createItemJson(3L, "STAT001", "Blue Pen Pack", "stationery", 
                      3.99, 100, 20, "Pack of 10 blue pens"));
        
        JsonObject responseJson = Json.createObjectBuilder()
                .add("success", true)
                .add("data", itemsArray)
                .add("message", "Items retrieved successfully")
                .build();
        
        sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
    }
    
    private void handleGetItemById(HttpServletRequest request, HttpServletResponse response, 
                                 Long itemId) throws IOException {
        // Mock data for now - replace with actual service call
        if (itemId == 1L) {
            JsonObject item = createItemJson(1L, "BOOK001", "Mathematics Grade 10", "textbook", 
                            25.99, 50, 10, "Grade 10 Mathematics textbook");
            
            JsonObject responseJson = Json.createObjectBuilder()
                    .add("success", true)
                    .add("data", item)
                    .add("message", "Item retrieved successfully")
                    .build();
            
            sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
        } else {
            sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Item not found");
        }
    }
    
    private void handleSearchItems(HttpServletRequest request, HttpServletResponse response, 
                                 String searchTerm, String category) throws IOException {
        // Mock search results - replace with actual service call
        JsonArrayBuilder itemsArray = Json.createArrayBuilder();
        
        if (searchTerm.toLowerCase().contains("math")) {
            itemsArray.add(createItemJson(1L, "BOOK001", "Mathematics Grade 10", "textbook", 
                          25.99, 50, 10, "Grade 10 Mathematics textbook"));
        }
        
        JsonObject responseJson = Json.createObjectBuilder()
                .add("success", true)
                .add("data", itemsArray)
                .add("message", "Search completed successfully")
                .build();
        
        sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
    }
    
    private void handleGetItemsByCategory(HttpServletRequest request, HttpServletResponse response, 
                                        String category) throws IOException {
        // Mock category results - replace with actual service call
        JsonArrayBuilder itemsArray = Json.createArrayBuilder();
        
        if (category.equals("textbook")) {
            itemsArray.add(createItemJson(1L, "BOOK001", "Mathematics Grade 10", "textbook", 
                          25.99, 50, 10, "Grade 10 Mathematics textbook"));
            itemsArray.add(createItemJson(2L, "BOOK002", "English Literature", "textbook", 
                          22.50, 30, 5, "English Literature reference book"));
        }
        
        JsonObject responseJson = Json.createObjectBuilder()
                .add("success", true)
                .add("data", itemsArray)
                .add("message", "Items by category retrieved successfully")
                .build();
        
        sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
    }
    
    private void handleCreateItem(HttpServletRequest request, HttpServletResponse response, 
                                Item item) throws IOException {
        // Mock creation - replace with actual service call
        JsonObject itemJson = createItemJson(4L, item.getCode(), item.getName(), 
                              item.getCategory().toString().toLowerCase(), 
                              item.getPrice().doubleValue(), item.getStock(), 
                              item.getMinStock(), item.getDescription());
        
        JsonObject responseJson = Json.createObjectBuilder()
                .add("success", true)
                .add("data", itemJson)
                .add("message", "Item created successfully")
                .build();
        
        sendJsonResponse(response, HttpServletResponse.SC_CREATED, responseJson);
    }
    
    private void handleUpdateItem(HttpServletRequest request, HttpServletResponse response, 
                                Item item) throws IOException {
        // Mock update - replace with actual service call
        JsonObject itemJson = createItemJson(item.getId(), item.getCode(), item.getName(), 
                              item.getCategory().toString().toLowerCase(), 
                              item.getPrice().doubleValue(), item.getStock(), 
                              item.getMinStock(), item.getDescription());
        
        JsonObject responseJson = Json.createObjectBuilder()
                .add("success", true)
                .add("data", itemJson)
                .add("message", "Item updated successfully")
                .build();
        
        sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
    }
    
    private void handleUpdateStock(HttpServletRequest request, HttpServletResponse response, 
                                 Long itemId, Integer newStock) throws IOException {
        // Mock stock update - replace with actual service call
        JsonObject responseJson = Json.createObjectBuilder()
                .add("success", true)
                .add("data", Json.createObjectBuilder()
                    .add("itemId", itemId)
                    .add("newStock", newStock))
                .add("message", "Stock updated successfully")
                .build();
        
        sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
    }
    
    private void handleDeleteItem(HttpServletRequest request, HttpServletResponse response, 
                                Long itemId) throws IOException {
        // Mock deletion - replace with actual service call
        JsonObject responseJson = Json.createObjectBuilder()
                .add("success", true)
                .add("message", "Item deleted successfully")
                .build();
        
        sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
    }
    
    private void handleGetItemCount(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        // Mock count - replace with actual service call
        JsonObject responseJson = Json.createObjectBuilder()
                .add("success", true)
                .add("data", Json.createObjectBuilder().add("count", 150))
                .add("message", "Item count retrieved successfully")
                .build();
        
        sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
    }
    
    private void handleGetLowStockItems(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        // Mock low stock items - replace with actual service call
        JsonArrayBuilder itemsArray = Json.createArrayBuilder();
        itemsArray.add(createItemJson(4L, "BOOK003", "Science Grade 11", "textbook", 
                      28.75, 8, 10, "Grade 11 Science textbook"));
        
        JsonObject responseJson = Json.createObjectBuilder()
                .add("success", true)
                .add("data", itemsArray)
                .add("message", "Low stock items retrieved successfully")
                .build();
        
        sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
    }
    
    private void handleGetItemsInStock(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        // Mock in-stock items - replace with actual service call
        JsonArrayBuilder itemsArray = Json.createArrayBuilder();
        itemsArray.add(createItemJson(1L, "BOOK001", "Mathematics Grade 10", "textbook", 
                      25.99, 50, 10, "Grade 10 Mathematics textbook"));
        itemsArray.add(createItemJson(3L, "STAT001", "Blue Pen Pack", "stationery", 
                      3.99, 100, 20, "Pack of 10 blue pens"));
        
        JsonObject responseJson = Json.createObjectBuilder()
                .add("success", true)
                .add("data", itemsArray)
                .add("message", "In-stock items retrieved successfully")
                .build();
        
        sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
    }
    
    private void handleGetInventoryStatistics(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        // Mock statistics - replace with actual service call
        JsonObject statistics = Json.createObjectBuilder()
                .add("totalItems", 150)
                .add("inStockItems", 120)
                .add("lowStockItems", 15)
                .add("outOfStockItems", 15)
                .add("totalValue", 12500.75)
                .build();
        
        JsonObject responseJson = Json.createObjectBuilder()
                .add("success", true)
                .add("data", statistics)
                .add("message", "Inventory statistics retrieved successfully")
                .build();
        
        sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
    }
    
    private void handleGetCategories(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        // Return available categories
        JsonArrayBuilder categoriesArray = Json.createArrayBuilder();
        categoriesArray.add(Json.createObjectBuilder()
                .add("value", "textbook")
                .add("label", "Textbooks"));
        categoriesArray.add(Json.createObjectBuilder()
                .add("value", "reference")
                .add("label", "Reference Books"));
        categoriesArray.add(Json.createObjectBuilder()
                .add("value", "stationery")
                .add("label", "Stationery"));
        categoriesArray.add(Json.createObjectBuilder()
                .add("value", "digital")
                .add("label", "Digital Resources"));
        
        JsonObject responseJson = Json.createObjectBuilder()
                .add("success", true)
                .add("data", categoriesArray)
                .add("message", "Categories retrieved successfully")
                .build();
        
        sendJsonResponse(response, HttpServletResponse.SC_OK, responseJson);
    }
    
    // Utility methods
    
    private Item parseItemFromRequest(HttpServletRequest request) throws IOException {
        // In a real application, you would parse JSON from request body
        // For now, we'll get parameters from form data or query string
        Item item = new Item();
        item.setCode(request.getParameter("code"));
        item.setName(request.getParameter("name"));
        
        String categoryStr = request.getParameter("category");
        if (categoryStr != null) {
            item.setCategory(Item.ItemCategory.valueOf(categoryStr.toUpperCase()));
        }
        
        String priceStr = request.getParameter("price");
        if (priceStr != null) {
            item.setPrice(new BigDecimal(priceStr));
        }
        
        String stockStr = request.getParameter("stock");
        if (stockStr != null) {
            item.setStock(Integer.parseInt(stockStr));
        }
        
        String minStockStr = request.getParameter("minStock");
        if (minStockStr != null) {
            item.setMinStock(Integer.parseInt(minStockStr));
        }
        
        item.setDescription(request.getParameter("description"));
        return item;
    }
    
    private JsonObject createItemJson(Long id, String code, String name, String category, 
                                    double price, int stock, int minStock, String description) {
        String status = stock <= minStock ? "Low Stock" : "In Stock";
        
        return Json.createObjectBuilder()
                .add("id", id)
                .add("code", code)
                .add("name", name)
                .add("category", category)
                .add("price", price)
                .add("stock", stock)
                .add("minStock", minStock)
                .add("description", description != null ? description : "")
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
