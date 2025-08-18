package com.icbt.cis6003.pahanaedubookshop.dao;

import com.icbt.cis6003.pahanaedubookshop.model.Bill;
import com.icbt.cis6003.pahanaedubookshop.model.Bill.BillStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Bill operations
 */
public interface BillDAO {
    
    /**
     * Save a new bill
     * @param bill the bill to save
     * @return the saved bill with generated ID
     */
    Bill save(Bill bill);
    
    /**
     * Update an existing bill
     * @param bill the bill to update
     * @return the updated bill
     */
    Bill update(Bill bill);
    
    /**
     * Delete a bill by ID
     * @param id the bill ID
     * @return true if deleted successfully, false otherwise
     */
    boolean delete(Long id);
    
    /**
     * Find a bill by ID
     * @param id the bill ID
     * @return Optional containing the bill if found
     */
    Optional<Bill> findById(Long id);
    
    /**
     * Find all bills
     * @return list of all bills
     */
    List<Bill> findAll();
    
    /**
     * Find bills by customer ID
     * @param customerId the customer ID
     * @return list of bills for the customer
     */
    List<Bill> findByCustomerId(Long customerId);
    
    /**
     * Find bills by status
     * @param status the bill status
     * @return list of bills with the specified status
     */
    List<Bill> findByStatus(BillStatus status);
    
    /**
     * Find bills by date
     * @param date the bill date
     * @return list of bills for the specified date
     */
    List<Bill> findByDate(LocalDate date);
    
    /**
     * Find bills between two dates
     * @param startDate the start date
     * @param endDate the end date
     * @return list of bills in the date range
     */
    List<Bill> findByDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find bills created between two timestamps
     * @param startDateTime the start timestamp
     * @param endDateTime the end timestamp
     * @return list of bills created in the time range
     */
    List<Bill> findByCreatedAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
    
    /**
     * Find bills by customer and date range
     * @param customerId the customer ID
     * @param startDate the start date
     * @param endDate the end date
     * @return list of bills for the customer in the date range
     */
    List<Bill> findByCustomerAndDateBetween(Long customerId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Find bills with total amount greater than specified value
     * @param amount the minimum amount
     * @return list of bills with total >= amount
     */
    List<Bill> findByTotalAmountGreaterThan(BigDecimal amount);
    
    /**
     * Find bills with total amount between two values
     * @param minAmount the minimum amount
     * @param maxAmount the maximum amount
     * @return list of bills with total in the range
     */
    List<Bill> findByTotalAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);
    
    /**
     * Get recent bills ordered by creation date
     * @param limit the maximum number of bills to return
     * @return list of recent bills
     */
    List<Bill> findRecentBills(int limit);
    
    /**
     * Get bills for today
     * @return list of bills created today
     */
    List<Bill> findTodaysBills();
    
    /**
     * Get bills for current month
     * @return list of bills for current month
     */
    List<Bill> findCurrentMonthBills();
    
    /**
     * Get bills for current year
     * @return list of bills for current year
     */
    List<Bill> findCurrentYearBills();
    
    /**
     * Get the total count of bills
     * @return total number of bills
     */
    long count();
    
    /**
     * Get the count of bills by status
     * @param status the bill status
     * @return number of bills with the status
     */
    long countByStatus(BillStatus status);
    
    /**
     * Get the count of bills for today
     * @return number of bills created today
     */
    long countTodaysBills();
    
    /**
     * Get the count of bills for current month
     * @return number of bills for current month
     */
    long countCurrentMonthBills();
    
    /**
     * Get total sales amount for today
     * @return total amount of today's bills
     */
    BigDecimal getTodaysSalesTotal();
    
    /**
     * Get total sales amount for current month
     * @return total amount of current month's bills
     */
    BigDecimal getCurrentMonthSalesTotal();
    
    /**
     * Get total sales amount for current year
     * @return total amount of current year's bills
     */
    BigDecimal getCurrentYearSalesTotal();
    
    /**
     * Get total sales amount between two dates
     * @param startDate the start date
     * @param endDate the end date
     * @return total amount of bills in the date range
     */
    BigDecimal getSalesTotalBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Get total sales amount for a customer
     * @param customerId the customer ID
     * @return total amount of bills for the customer
     */
    BigDecimal getCustomerSalesTotal(Long customerId);
    
    /**
     * Get average bill amount
     * @return average amount of all bills
     */
    BigDecimal getAverageBillAmount();
    
    /**
     * Get average bill amount for current month
     * @return average amount of current month's bills
     */
    BigDecimal getCurrentMonthAverageBillAmount();
    
    /**
     * Check if a bill exists by ID
     * @param id the bill ID to check
     * @return true if bill exists, false otherwise
     */
    boolean existsById(Long id);
    
    /**
     * Get bills with pagination
     * @param offset the starting position
     * @param limit the maximum number of records to return
     * @return list of bills
     */
    List<Bill> findWithPagination(int offset, int limit);
    
    /**
     * Search bills by customer name
     * @param customerName the customer name to search for
     * @return list of matching bills
     */
    List<Bill> findByCustomerNameContaining(String customerName);
}
