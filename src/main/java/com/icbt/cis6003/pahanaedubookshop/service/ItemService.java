package com.icbt.cis6003.pahanaedubookshop.service;

import com.icbt.cis6003.pahanaedubookshop.model.Item;
import com.icbt.cis6003.pahanaedubookshop.model.Item.ItemCategory;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for Item business logic
 */
public interface ItemService {
    
    /**
     * Create a new item
     * @param item the item to create
     * @return the created item
     * @throws IllegalArgumentException if item data is invalid
     * @throws RuntimeException if item code already exists
     */
    Item createItem(Item item);
    
    /**
     * Update an existing item
     * @param item the item to update
     * @return the updated item
     * @throws IllegalArgumentException if item data is invalid
     * @throws RuntimeException if item not found or code conflicts
     */
    Item updateItem(Item item);
    
    /**
     * Delete an item by ID
     * @param itemId the item ID
     * @return true if deleted successfully
     * @throws RuntimeException if item not found or has associated bills
     */
    boolean deleteItem(Long itemId);
    
    /**
     * Get an item by ID
     * @param itemId the item ID
     * @return Optional containing the item if found
     */
    Optional<Item> getItemById(Long itemId);
    
    /**
     * Get an item by code
     * @param code the item code
     * @return Optional containing the item if found
     */
    Optional<Item> getItemByCode(String code);
    
    /**
     * Get all items
     * @return list of all items
     */
    List<Item> getAllItems();
    
    /**
     * Get items by category
     * @param category the item category
     * @return list of items in the category
     */
    List<Item> getItemsByCategory(ItemCategory category);
    
    /**
     * Search items by name, code, or description
     * @param searchTerm the search term
     * @return list of matching items
     */
    List<Item> searchItems(String searchTerm);
    
    /**
     * Search items by name/code and category
     * @param searchTerm the search term
     * @param category the category filter (null for all categories)
     * @return list of matching items
     */
    List<Item> searchItems(String searchTerm, ItemCategory category);
    
    /**
     * Get items in stock (stock > 0)
     * @return list of items in stock
     */
    List<Item> getItemsInStock();
    
    /**
     * Get items with low stock
     * @return list of items with low stock
     */
    List<Item> getLowStockItems();
    
    /**
     * Get out of stock items
     * @return list of out of stock items
     */
    List<Item> getOutOfStockItems();
    
    /**
     * Get items by price range
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @return list of items in the price range
     */
    List<Item> getItemsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    
    /**
     * Update item stock
     * @param itemId the item ID
     * @param newStock the new stock quantity
     * @return the updated item
     * @throws RuntimeException if item not found
     */
    Item updateItemStock(Long itemId, Integer newStock);
    
    /**
     * Increase item stock
     * @param itemId the item ID
     * @param quantity the quantity to add
     * @return the updated item
     * @throws RuntimeException if item not found
     */
    Item increaseStock(Long itemId, Integer quantity);
    
    /**
     * Decrease item stock
     * @param itemId the item ID
     * @param quantity the quantity to subtract
     * @return the updated item
     * @throws RuntimeException if item not found or insufficient stock
     */
    Item decreaseStock(Long itemId, Integer quantity);
    
    /**
     * Check if item has sufficient stock for order
     * @param itemId the item ID
     * @param requiredQuantity the required quantity
     * @return true if sufficient stock available
     */
    boolean hasSufficientStock(Long itemId, Integer requiredQuantity);
    
    /**
     * Reserve stock for an order
     * @param itemId the item ID
     * @param quantity the quantity to reserve
     * @return true if reservation successful
     * @throws RuntimeException if insufficient stock
     */
    boolean reserveStock(Long itemId, Integer quantity);
    
    /**
     * Get items with pagination
     * @param page the page number (0-based)
     * @param size the page size
     * @return list of items for the page
     */
    List<Item> getItemsWithPagination(int page, int size);
    
    /**
     * Get total item count
     * @return total number of items
     */
    long getTotalItemCount();
    
    /**
     * Get count of items by category
     * @param category the category
     * @return number of items in the category
     */
    long getItemCountByCategory(ItemCategory category);
    
    /**
     * Get count of items in stock
     * @return number of items with stock > 0
     */
    long getInStockItemCount();
    
    /**
     * Get count of low stock items
     * @return number of items with low stock
     */
    long getLowStockItemCount();
    
    /**
     * Check if item code is available
     * @param code the item code to check
     * @return true if available, false if already exists
     */
    boolean isItemCodeAvailable(String code);
    
    /**
     * Generate next available item code for category
     * @param category the item category
     * @return the next available item code
     */
    String generateNextItemCode(ItemCategory category);
    
    /**
     * Validate item data
     * @param item the item to validate
     * @return true if valid
     * @throws IllegalArgumentException if validation fails
     */
    boolean validateItem(Item item);
    
    /**
     * Get inventory statistics
     * @return inventory statistics object
     */
    InventoryStatistics getInventoryStatistics();
    
    /**
     * Check if item can be deleted (not in any bills)
     * @param itemId the item ID
     * @return true if item can be deleted
     */
    boolean canDeleteItem(Long itemId);
    
    /**
     * Get total inventory value
     * @return total value of all items in stock
     */
    BigDecimal getTotalInventoryValue();
    
    /**
     * Get inventory value by category
     * @param category the category
     * @return total value of items in the category
     */
    BigDecimal getInventoryValueByCategory(ItemCategory category);
    
    /**
     * Inner class for inventory statistics
     */
    class InventoryStatistics {
        private long totalItems;
        private long inStockItems;
        private long lowStockItems;
        private long outOfStockItems;
        private BigDecimal totalValue;
        
        public InventoryStatistics(long totalItems, long inStockItems, 
                                 long lowStockItems, long outOfStockItems, 
                                 BigDecimal totalValue) {
            this.totalItems = totalItems;
            this.inStockItems = inStockItems;
            this.lowStockItems = lowStockItems;
            this.outOfStockItems = outOfStockItems;
            this.totalValue = totalValue;
        }
        
        // Getters
        public long getTotalItems() { return totalItems; }
        public long getInStockItems() { return inStockItems; }
        public long getLowStockItems() { return lowStockItems; }
        public long getOutOfStockItems() { return outOfStockItems; }
        public BigDecimal getTotalValue() { return totalValue; }
        
        // Setters
        public void setTotalItems(long totalItems) { this.totalItems = totalItems; }
        public void setInStockItems(long inStockItems) { this.inStockItems = inStockItems; }
        public void setLowStockItems(long lowStockItems) { this.lowStockItems = lowStockItems; }
        public void setOutOfStockItems(long outOfStockItems) { this.outOfStockItems = outOfStockItems; }
        public void setTotalValue(BigDecimal totalValue) { this.totalValue = totalValue; }
    }
}
