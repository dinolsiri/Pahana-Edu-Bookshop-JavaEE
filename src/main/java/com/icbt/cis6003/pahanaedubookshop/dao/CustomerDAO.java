package com.icbt.cis6003.pahanaedubookshop.dao;

import com.icbt.cis6003.pahanaedubookshop.model.Customer;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Customer operations
 */
public interface CustomerDAO {
    
    /**
     * Save a new customer
     * @param customer the customer to save
     * @return the saved customer with generated ID
     */
    Customer save(Customer customer);
    
    /**
     * Update an existing customer
     * @param customer the customer to update
     * @return the updated customer
     */
    Customer update(Customer customer);
    
    /**
     * Delete a customer by ID
     * @param id the customer ID
     * @return true if deleted successfully, false otherwise
     */
    boolean delete(Long id);
    
    /**
     * Find a customer by ID
     * @param id the customer ID
     * @return Optional containing the customer if found
     */
    Optional<Customer> findById(Long id);
    
    /**
     * Find a customer by account number
     * @param accountNumber the account number
     * @return Optional containing the customer if found
     */
    Optional<Customer> findByAccountNumber(String accountNumber);
    
    /**
     * Find all customers
     * @return list of all customers
     */
    List<Customer> findAll();
    
    /**
     * Find customers by name (partial match, case-insensitive)
     * @param name the name to search for
     * @return list of matching customers
     */
    List<Customer> findByNameContaining(String name);
    
    /**
     * Find customers by phone number
     * @param phone the phone number
     * @return list of matching customers
     */
    List<Customer> findByPhone(String phone);
    
    /**
     * Find customers registered between two dates
     * @param startDate the start date
     * @param endDate the end date
     * @return list of customers registered in the date range
     */
    List<Customer> findByRegistrationDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find customers registered on a specific date
     * @param date the registration date
     * @return list of customers registered on the date
     */
    List<Customer> findByRegistrationDate(LocalDate date);
    
    /**
     * Search customers by multiple criteria
     * @param searchTerm search term to match against name, account number, or phone
     * @return list of matching customers
     */
    List<Customer> search(String searchTerm);
    
    /**
     * Get the total count of customers
     * @return total number of customers
     */
    long count();
    
    /**
     * Get the count of customers registered this month
     * @return number of customers registered this month
     */
    long countByCurrentMonth();
    
    /**
     * Check if a customer exists by account number
     * @param accountNumber the account number to check
     * @return true if customer exists, false otherwise
     */
    boolean existsByAccountNumber(String accountNumber);
    
    /**
     * Check if a customer exists by ID
     * @param id the customer ID to check
     * @return true if customer exists, false otherwise
     */
    boolean existsById(Long id);
    
    /**
     * Get customers with pagination
     * @param offset the starting position
     * @param limit the maximum number of records to return
     * @return list of customers
     */
    List<Customer> findWithPagination(int offset, int limit);
    
    /**
     * Get customers ordered by registration date (newest first)
     * @param limit the maximum number of records to return
     * @return list of recent customers
     */
    List<Customer> findRecentCustomers(int limit);
}
