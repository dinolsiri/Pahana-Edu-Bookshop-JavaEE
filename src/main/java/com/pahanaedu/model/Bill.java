package com.pahanaedu.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Bill {
    private int id;
    private String billNumber;
    private int customerId;
    private Customer customer;
    private LocalDateTime billDate;
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal total;
    private List<BillItem> items;

    // Constructors
    public Bill() {}

    public Bill(String billNumber, int customerId, BigDecimal subtotal, BigDecimal tax, BigDecimal total) {
        this.billNumber = billNumber;
        this.customerId = customerId;
        this.subtotal = subtotal;
        this.tax = tax;
        this.total = total;
        this.billDate = LocalDateTime.now();
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public LocalDateTime getBillDate() { return billDate; }
    public void setBillDate(LocalDateTime billDate) { this.billDate = billDate; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public BigDecimal getTax() { return tax; }
    public void setTax(BigDecimal tax) { this.tax = tax; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public List<BillItem> getItems() { return items; }
    public void setItems(List<BillItem> items) { this.items = items; }
}