package com.icbt.cis6003.pahanaedubookshop.service;

import com.icbt.cis6003.pahanaedubookshop.model.Customer;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for Customer business logic
 */
public interface CustomerService {
    
    /**
     * Create a new customer
     * @param customer the customer to create
     * @return the created customer
     * @throws IllegalArgumentException if customer data is invalid
     * @throws RuntimeException if account number already exists
     */
    Customer createCustomer(Customer customer);
    
    /**
     * Update an existing customer
     * @param customer the customer to update
     * @return the updated customer
     * @throws IllegalArgumentException if customer data is invalid
     * @throws RuntimeException if customer not found or account number conflicts
     */
    Customer updateCustomer(Customer customer);
    
    /**
     * Delete a customer by ID
     * @param customerId the customer ID
     * @return true if deleted successfully
     * @throws RuntimeException if customer not found or has associated bills
     */
    boolean deleteCustomer(Long customerId);
    
    /**
     * Get a customer by ID
     * @param customerId the customer ID
     * @return Optional containing the customer if found
     */
    Optional<Customer> getCustomerById(Long customerId);
    
    /**
     * Get a customer by account number
     * @param accountNumber the account number
     * @return Optional containing the customer if found
     */
    Optional<Customer> getCustomerByAccountNumber(String accountNumber);
    
    /**
     * Get all customers
     * @return list of all customers
     */
    List<Customer> getAllCustomers();
    
    /**
     * Search customers by name, account number, or phone
     * @param searchTerm the search term
     * @return list of matching customers
     */
    List<Customer> searchCustomers(String searchTerm);
    
    /**
     * Get customers registered in a date range
     * @param startDate the start date
     * @param endDate the end date
     * @return list of customers registered in the range
     */
    List<Customer> getCustomersByRegistrationDateRange(LocalDate startDate, LocalDate endDate);
    
    /**
     * Get recent customers
     * @param limit the maximum number of customers to return
     * @return list of recent customers
     */
    List<Customer> getRecentCustomers(int limit);
    
    /**
     * Get customers with pagination
     * @param page the page number (0-based)
     * @param size the page size
     * @return list of customers for the page
     */
    List<Customer> getCustomersWithPagination(int page, int size);
    
    /**
     * Get total customer count
     * @return total number of customers
     */
    long getTotalCustomerCount();
    
    /**
     * Get count of customers registered this month
     * @return number of customers registered this month
     */
    long getNewCustomersThisMonth();
    
    /**
     * Get count of customers registered today
     * @return number of customers registered today
     */
    long getNewCustomersToday();
    
    /**
     * Check if account number is available
     * @param accountNumber the account number to check
     * @return true if available, false if already exists
     */
    boolean isAccountNumberAvailable(String accountNumber);
    
    /**
     * Generate next available account number
     * @return the next available account number
     */
    String generateNextAccountNumber();
    
    /**
     * Validate customer data
     * @param customer the customer to validate
     * @return true if valid
     * @throws IllegalArgumentException if validation fails
     */
    boolean validateCustomer(Customer customer);
    
    /**
     * Get customer statistics
     * @return customer statistics object
     */
    CustomerStatistics getCustomerStatistics();
    
    /**
     * Check if customer can be deleted (no associated bills)
     * @param customerId the customer ID
     * @return true if customer can be deleted
     */
    boolean canDeleteCustomer(Long customerId);
    
    /**
     * Get customers by phone number
     * @param phone the phone number
     * @return list of customers with the phone number
     */
    List<Customer> getCustomersByPhone(String phone);
    
    /**
     * Get customers by name (partial match)
     * @param name the name to search for
     * @return list of customers with matching names
     */
    List<Customer> getCustomersByName(String name);
    
    /**
     * Inner class for customer statistics
     */
    class CustomerStatistics {
        private long totalCustomers;
        private long newThisMonth;
        private long newToday;
        private double growthRate;
        
        public CustomerStatistics(long totalCustomers, long newThisMonth, 
                                long newToday, double growthRate) {
            this.totalCustomers = totalCustomers;
            this.newThisMonth = newThisMonth;
            this.newToday = newToday;
            this.growthRate = growthRate;
        }
        
        // Getters
        public long getTotalCustomers() { return totalCustomers; }
        public long getNewThisMonth() { return newThisMonth; }
        public long getNewToday() { return newToday; }
        public double getGrowthRate() { return growthRate; }
        
        // Setters
        public void setTotalCustomers(long totalCustomers) { this.totalCustomers = totalCustomers; }
        public void setNewThisMonth(long newThisMonth) { this.newThisMonth = newThisMonth; }
        public void setNewToday(long newToday) { this.newToday = newToday; }
        public void setGrowthRate(double growthRate) { this.growthRate = growthRate; }
    }
}
