package com.icbt.cis6003.pahanaedubookshop.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Bill entity representing a customer bill/invoice
 */
public class Bill {
    private Long id;
    private Long customerId;
    private String customerName;
    private String customerAccountNumber;
    private LocalDate billDate;
    private LocalDateTime createdAt;
    private List<BillItem> items;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private BigDecimal taxRate;
    private BillStatus status;

    // Default constructor
    public Bill() {
        this.items = new ArrayList<>();
        this.billDate = LocalDate.now();
        this.createdAt = LocalDateTime.now();
        this.taxRate = new BigDecimal("0.10"); // 10% tax rate
        this.status = BillStatus.DRAFT;
    }

    // Constructor with customer
    public Bill(Long customerId, String customerName, String customerAccountNumber) {
        this();
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerAccountNumber = customerAccountNumber;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAccountNumber() {
        return customerAccountNumber;
    }

    public void setCustomerAccountNumber(String customerAccountNumber) {
        this.customerAccountNumber = customerAccountNumber;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<BillItem> getItems() {
        return items;
    }

    public void setItems(List<BillItem> items) {
        this.items = items;
        calculateTotals();
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BillStatus getStatus() {
        return status;
    }

    public void setStatus(BillStatus status) {
        this.status = status;
    }

    // Business methods
    public void addItem(BillItem item) {
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
        calculateTotals();
    }

    public void removeItem(BillItem item) {
        if (items != null) {
            items.remove(item);
            calculateTotals();
        }
    }

    public void calculateTotals() {
        if (items == null || items.isEmpty()) {
            subtotal = BigDecimal.ZERO;
            taxAmount = BigDecimal.ZERO;
            totalAmount = BigDecimal.ZERO;
            return;
        }

        subtotal = items.stream()
                .map(BillItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        taxAmount = subtotal.multiply(taxRate);
        totalAmount = subtotal.add(taxAmount);
    }

    public int getTotalItemCount() {
        if (items == null) {
            return 0;
        }
        return items.stream()
                .mapToInt(BillItem::getQuantity)
                .sum();
    }

    public boolean isValid() {
        return customerId != null &&
               customerName != null && !customerName.trim().isEmpty() &&
               items != null && !items.isEmpty() &&
               items.stream().allMatch(BillItem::isValid);
    }

    public void finalizeBill() {
        calculateTotals();
        this.status = BillStatus.FINALIZED;
    }

    public boolean canBeModified() {
        return status == BillStatus.DRAFT;
    }

    // Override methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bill bill = (Bill) o;
        return Objects.equals(id, bill.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Bill{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", billDate=" + billDate +
                ", totalAmount=" + totalAmount +
                ", status=" + status +
                '}';
    }

    // Enums
    public enum BillStatus {
        DRAFT("Draft"),
        FINALIZED("Finalized"),
        PAID("Paid"),
        CANCELLED("Cancelled");

        private final String displayName;

        BillStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }
}
