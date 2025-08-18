package com.icbt.cis6003.pahanaedubookshop.service;

import com.icbt.cis6003.pahanaedubookshop.model.Bill;
import com.icbt.cis6003.pahanaedubookshop.model.BillItem;
import com.icbt.cis6003.pahanaedubookshop.model.Bill.BillStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for Billing business logic
 */
public interface BillingService {
    
    /**
     * Create a new bill
     * @param customerId the customer ID
     * @return the created bill
     * @throws RuntimeException if customer not found
     */
    Bill createBill(Long customerId);
    
    /**
     * Add item to bill
     * @param billId the bill ID
     * @param itemId the item ID
     * @param quantity the quantity
     * @return the updated bill
     * @throws RuntimeException if bill/item not found or insufficient stock
     */
    Bill addItemToBill(Long billId, Long itemId, Integer quantity);
    
    /**
     * Remove item from bill
     * @param billId the bill ID
     * @param itemId the item ID
     * @return the updated bill
     * @throws RuntimeException if bill/item not found
     */
    Bill removeItemFromBill(Long billId, Long itemId);
    
    /**
     * Update item quantity in bill
     * @param billId the bill ID
     * @param itemId the item ID
     * @param newQuantity the new quantity
     * @return the updated bill
     * @throws RuntimeException if bill/item not found or insufficient stock
     */
    Bill updateItemQuantityInBill(Long billId, Long itemId, Integer newQuantity);
    
    /**
     * Finalize bill (calculate totals and update stock)
     * @param billId the bill ID
     * @return the finalized bill
     * @throws RuntimeException if bill not found or already finalized
     */
    Bill finalizeBill(Long billId);
    
    /**
     * Cancel bill
     * @param billId the bill ID
     * @return the cancelled bill
     * @throws RuntimeException if bill not found or cannot be cancelled
     */
    Bill cancelBill(Long billId);
    
    /**
     * Get bill by ID
     * @param billId the bill ID
     * @return Optional containing the bill if found
     */
    Optional<Bill> getBillById(Long billId);
    
    /**
     * Get all bills
     * @return list of all bills
     */
    List<Bill> getAllBills();
    
    /**
     * Get bills by customer
     * @param customerId the customer ID
     * @return list of bills for the customer
     */
    List<Bill> getBillsByCustomer(Long customerId);
    
    /**
     * Get bills by status
     * @param status the bill status
     * @return list of bills with the status
     */
    List<Bill> getBillsByStatus(BillStatus status);
    
    /**
     * Get bills by date
     * @param date the bill date
     * @return list of bills for the date
     */
    List<Bill> getBillsByDate(LocalDate date);
    
    /**
     * Get bills by date range
     * @param startDate the start date
     * @param endDate the end date
     * @return list of bills in the date range
     */
    List<Bill> getBillsByDateRange(LocalDate startDate, LocalDate endDate);
    
    /**
     * Get recent bills
     * @param limit the maximum number of bills to return
     * @return list of recent bills
     */
    List<Bill> getRecentBills(int limit);
    
    /**
     * Get today's bills
     * @return list of bills for today
     */
    List<Bill> getTodaysBills();
    
    /**
     * Get current month's bills
     * @return list of bills for current month
     */
    List<Bill> getCurrentMonthBills();
    
    /**
     * Search bills by customer name
     * @param customerName the customer name to search for
     * @return list of matching bills
     */
    List<Bill> searchBillsByCustomerName(String customerName);
    
    /**
     * Get bills with pagination
     * @param page the page number (0-based)
     * @param size the page size
     * @return list of bills for the page
     */
    List<Bill> getBillsWithPagination(int page, int size);
    
    /**
     * Get total bill count
     * @return total number of bills
     */
    long getTotalBillCount();
    
    /**
     * Get bill count by status
     * @param status the bill status
     * @return number of bills with the status
     */
    long getBillCountByStatus(BillStatus status);
    
    /**
     * Get today's bill count
     * @return number of bills created today
     */
    long getTodaysBillCount();
    
    /**
     * Get current month's bill count
     * @return number of bills for current month
     */
    long getCurrentMonthBillCount();
    
    /**
     * Get today's sales total
     * @return total amount of today's sales
     */
    BigDecimal getTodaysSalesTotal();
    
    /**
     * Get current month's sales total
     * @return total amount of current month's sales
     */
    BigDecimal getCurrentMonthSalesTotal();
    
    /**
     * Get current year's sales total
     * @return total amount of current year's sales
     */
    BigDecimal getCurrentYearSalesTotal();
    
    /**
     * Get sales total by date range
     * @param startDate the start date
     * @param endDate the end date
     * @return total amount of sales in the date range
     */
    BigDecimal getSalesTotalByDateRange(LocalDate startDate, LocalDate endDate);
    
    /**
     * Get customer's total purchases
     * @param customerId the customer ID
     * @return total amount of customer's purchases
     */
    BigDecimal getCustomerTotalPurchases(Long customerId);
    
    /**
     * Get average bill amount
     * @return average amount of all bills
     */
    BigDecimal getAverageBillAmount();
    
    /**
     * Get current month's average bill amount
     * @return average amount of current month's bills
     */
    BigDecimal getCurrentMonthAverageBillAmount();
    
    /**
     * Calculate bill totals
     * @param bill the bill to calculate
     * @return the bill with calculated totals
     */
    Bill calculateBillTotals(Bill bill);
    
    /**
     * Validate bill before finalization
     * @param bill the bill to validate
     * @return true if valid
     * @throws IllegalArgumentException if validation fails
     */
    boolean validateBill(Bill bill);
    
    /**
     * Check if bill can be modified
     * @param billId the bill ID
     * @return true if bill can be modified
     */
    boolean canModifyBill(Long billId);
    
    /**
     * Check if bill can be cancelled
     * @param billId the bill ID
     * @return true if bill can be cancelled
     */
    boolean canCancelBill(Long billId);
    
    /**
     * Get sales statistics
     * @return sales statistics object
     */
    SalesStatistics getSalesStatistics();
    
    /**
     * Generate bill report
     * @param startDate the start date
     * @param endDate the end date
     * @return bill report data
     */
    BillReport generateBillReport(LocalDate startDate, LocalDate endDate);
    
    /**
     * Inner class for sales statistics
     */
    class SalesStatistics {
        private long totalBills;
        private long todaysBills;
        private long monthlyBills;
        private BigDecimal totalSales;
        private BigDecimal todaysSales;
        private BigDecimal monthlySales;
        private BigDecimal averageBillAmount;
        
        public SalesStatistics(long totalBills, long todaysBills, long monthlyBills,
                             BigDecimal totalSales, BigDecimal todaysSales, 
                             BigDecimal monthlySales, BigDecimal averageBillAmount) {
            this.totalBills = totalBills;
            this.todaysBills = todaysBills;
            this.monthlyBills = monthlyBills;
            this.totalSales = totalSales;
            this.todaysSales = todaysSales;
            this.monthlySales = monthlySales;
            this.averageBillAmount = averageBillAmount;
        }
        
        // Getters
        public long getTotalBills() { return totalBills; }
        public long getTodaysBills() { return todaysBills; }
        public long getMonthlyBills() { return monthlyBills; }
        public BigDecimal getTotalSales() { return totalSales; }
        public BigDecimal getTodaysSales() { return todaysSales; }
        public BigDecimal getMonthlySales() { return monthlySales; }
        public BigDecimal getAverageBillAmount() { return averageBillAmount; }
    }
    
    /**
     * Inner class for bill report
     */
    class BillReport {
        private LocalDate startDate;
        private LocalDate endDate;
        private long totalBills;
        private BigDecimal totalAmount;
        private BigDecimal averageAmount;
        private List<Bill> bills;
        
        public BillReport(LocalDate startDate, LocalDate endDate, long totalBills,
                         BigDecimal totalAmount, BigDecimal averageAmount, List<Bill> bills) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.totalBills = totalBills;
            this.totalAmount = totalAmount;
            this.averageAmount = averageAmount;
            this.bills = bills;
        }
        
        // Getters
        public LocalDate getStartDate() { return startDate; }
        public LocalDate getEndDate() { return endDate; }
        public long getTotalBills() { return totalBills; }
        public BigDecimal getTotalAmount() { return totalAmount; }
        public BigDecimal getAverageAmount() { return averageAmount; }
        public List<Bill> getBills() { return bills; }
    }
}
