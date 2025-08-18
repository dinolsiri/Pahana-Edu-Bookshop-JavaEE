package com.icbt.cis6003.pahanaedubookshop.dao;

import com.icbt.cis6003.pahanaedubookshop.model.Item;
import com.icbt.cis6003.pahanaedubookshop.model.Item.ItemCategory;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Item operations
 */
public interface ItemDAO {
    
    /**
     * Save a new item
     * @param item the item to save
     * @return the saved item with generated ID
     */
    Item save(Item item);
    
    /**
     * Update an existing item
     * @param item the item to update
     * @return the updated item
     */
    Item update(Item item);
    
    /**
     * Delete an item by ID
     * @param id the item ID
     * @return true if deleted successfully, false otherwise
     */
    boolean delete(Long id);
    
    /**
     * Find an item by ID
     * @param id the item ID
     * @return Optional containing the item if found
     */
    Optional<Item> findById(Long id);
    
    /**
     * Find an item by code
     * @param code the item code
     * @return Optional containing the item if found
     */
    Optional<Item> findByCode(String code);
    
    /**
     * Find all items
     * @return list of all items
     */
    List<Item> findAll();
    
    /**
     * Find items by category
     * @param category the item category
     * @return list of items in the category
     */
    List<Item> findByCategory(ItemCategory category);
    
    /**
     * Find items by name (partial match, case-insensitive)
     * @param name the name to search for
     * @return list of matching items
     */
    List<Item> findByNameContaining(String name);
    
    /**
     * Find items with stock greater than zero
     * @return list of items in stock
     */
    List<Item> findInStock();
    
    /**
     * Find items with low stock (stock <= minStock)
     * @return list of items with low stock
     */
    List<Item> findLowStock();
    
    /**
     * Find items with no stock (stock = 0)
     * @return list of out-of-stock items
     */
    List<Item> findOutOfStock();
    
    /**
     * Find items by price range
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @return list of items in the price range
     */
    List<Item> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    
    /**
     * Search items by multiple criteria
     * @param searchTerm search term to match against name, code, or description
     * @return list of matching items
     */
    List<Item> search(String searchTerm);
    
    /**
     * Search items by name or code and category
     * @param searchTerm search term for name or code
     * @param category the category filter (null for all categories)
     * @return list of matching items
     */
    List<Item> searchByNameOrCodeAndCategory(String searchTerm, ItemCategory category);
    
    /**
     * Update item stock
     * @param itemId the item ID
     * @param newStock the new stock quantity
     * @return true if updated successfully, false otherwise
     */
    boolean updateStock(Long itemId, Integer newStock);
    
    /**
     * Decrease item stock by quantity
     * @param itemId the item ID
     * @param quantity the quantity to decrease
     * @return true if updated successfully, false otherwise
     */
    boolean decreaseStock(Long itemId, Integer quantity);
    
    /**
     * Increase item stock by quantity
     * @param itemId the item ID
     * @param quantity the quantity to increase
     * @return true if updated successfully, false otherwise
     */
    boolean increaseStock(Long itemId, Integer quantity);
    
    /**
     * Get the total count of items
     * @return total number of items
     */
    long count();
    
    /**
     * Get the count of items by category
     * @param category the category
     * @return number of items in the category
     */
    long countByCategory(ItemCategory category);
    
    /**
     * Get the count of items in stock
     * @return number of items with stock > 0
     */
    long countInStock();
    
    /**
     * Get the count of items with low stock
     * @return number of items with low stock
     */
    long countLowStock();
    
    /**
     * Check if an item exists by code
     * @param code the item code to check
     * @return true if item exists, false otherwise
     */
    boolean existsByCode(String code);
    
    /**
     * Check if an item exists by ID
     * @param id the item ID to check
     * @return true if item exists, false otherwise
     */
    boolean existsById(Long id);
    
    /**
     * Get items with pagination
     * @param offset the starting position
     * @param limit the maximum number of records to return
     * @return list of items
     */
    List<Item> findWithPagination(int offset, int limit);
    
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
}
