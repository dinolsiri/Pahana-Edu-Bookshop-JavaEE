package com.icbt.cis6003.pahanaedubookshop.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * BillItem entity representing an item in a bill
 */
public class BillItem {
    private Long id;
    private Long billId;
    private Long itemId;
    private String itemCode;
    private String itemName;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal total;

    // Default constructor
    public BillItem() {
    }

    // Constructor with required fields
    public BillItem(Long itemId, String itemCode, String itemName, 
                   BigDecimal unitPrice, Integer quantity) {
        this.itemId = itemId;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        calculateTotal();
    }

    // Full constructor
    public BillItem(Long id, Long billId, Long itemId, String itemCode, 
                   String itemName, BigDecimal unitPrice, Integer quantity) {
        this.id = id;
        this.billId = billId;
        this.itemId = itemId;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        calculateTotal();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        calculateTotal();
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        calculateTotal();
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    // Business methods
    public void calculateTotal() {
        if (unitPrice != null && quantity != null) {
            this.total = unitPrice.multiply(BigDecimal.valueOf(quantity));
        } else {
            this.total = BigDecimal.ZERO;
        }
    }

    public boolean isValid() {
        return itemId != null &&
               itemCode != null && !itemCode.trim().isEmpty() &&
               itemName != null && !itemName.trim().isEmpty() &&
               unitPrice != null && unitPrice.compareTo(BigDecimal.ZERO) >= 0 &&
               quantity != null && quantity > 0;
    }

    public void updateQuantity(int newQuantity) {
        if (newQuantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.quantity = newQuantity;
        calculateTotal();
    }

    public void updateUnitPrice(BigDecimal newPrice) {
        if (newPrice == null || newPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Unit price must be non-negative");
        }
        this.unitPrice = newPrice;
        calculateTotal();
    }

    public String getDisplayInfo() {
        return String.format("%s (%s) - Qty: %d @ $%.2f = $%.2f", 
                           itemName, itemCode, quantity, unitPrice, total);
    }

    // Static factory methods
    public static BillItem fromItem(Item item, int quantity) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (!item.canFulfillOrder(quantity)) {
            throw new IllegalArgumentException("Insufficient stock for item: " + item.getName());
        }

        return new BillItem(
            item.getId(),
            item.getCode(),
            item.getName(),
            item.getPrice(),
            quantity
        );
    }

    // Override methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillItem billItem = (BillItem) o;
        return Objects.equals(id, billItem.id) &&
               Objects.equals(itemId, billItem.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, itemId);
    }

    @Override
    public String toString() {
        return "BillItem{" +
                "id=" + id +
                ", billId=" + billId +
                ", itemId=" + itemId +
                ", itemCode='" + itemCode + '\'' +
                ", itemName='" + itemName + '\'' +
                ", unitPrice=" + unitPrice +
                ", quantity=" + quantity +
                ", total=" + total +
                '}';
    }
}
