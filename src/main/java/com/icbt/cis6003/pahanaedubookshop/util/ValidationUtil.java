package com.icbt.cis6003.pahanaedubookshop.util;

import com.icbt.cis6003.pahanaedubookshop.model.Customer;
import com.icbt.cis6003.pahanaedubookshop.model.Item;
import com.icbt.cis6003.pahanaedubookshop.model.Bill;
import com.icbt.cis6003.pahanaedubookshop.model.BillItem;

import java.math.BigDecimal;

/**
 * Utility class for basic data validation
 */
public class ValidationUtil {

    /**
     * Simple customer validation
     */
    public static boolean validateCustomer(Customer customer) {
        if (customer == null) return false;
        if (isNullOrEmpty(customer.getAccountNumber())) return false;
        if (isNullOrEmpty(customer.getName())) return false;
        if (isNullOrEmpty(customer.getAddress())) return false;
        if (isNullOrEmpty(customer.getPhone())) return false;
        return true;
    }

    /**
     * Simple item validation
     */
    public static boolean validateItem(Item item) {
        if (item == null) return false;
        if (isNullOrEmpty(item.getCode())) return false;
        if (isNullOrEmpty(item.getName())) return false;
        if (item.getCategory() == null) return false;
        if (item.getPrice() == null || item.getPrice().compareTo(BigDecimal.ZERO) < 0) return false;
        if (item.getStock() == null || item.getStock() < 0) return false;
        return true;
    }

    /**
     * Simple bill validation
     */
    public static boolean validateBill(Bill bill) {
        if (bill == null) return false;
        if (bill.getCustomerId() == null) return false;
        if (isNullOrEmpty(bill.getCustomerName())) return false;
        if (bill.getBillDate() == null) return false;
        if (bill.getItems() == null || bill.getItems().isEmpty()) return false;
        return true;
    }

    /**
     * Simple bill item validation
     */
    public static boolean validateBillItem(BillItem billItem) {
        if (billItem == null) return false;
        if (billItem.getItemId() == null) return false;
        if (isNullOrEmpty(billItem.getItemCode())) return false;
        if (isNullOrEmpty(billItem.getItemName())) return false;
        if (billItem.getUnitPrice() == null || billItem.getUnitPrice().compareTo(BigDecimal.ZERO) < 0) return false;
        if (billItem.getQuantity() == null || billItem.getQuantity() <= 0) return false;
        return true;
    }

    /**
     * Check if string is null or empty
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}

