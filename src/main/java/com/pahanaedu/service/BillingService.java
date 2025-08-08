package com.pahanaedu.service;

import com.pahanaedu.dao.BillDAO;
import com.pahanaedu.model.Bill;

import java.math.BigDecimal;
import java.util.List;

public class BillingService {
    
    private BillDAO billDAO;

    public BillingService() {
        this.billDAO = new BillDAO();
    }

    public boolean createBill(Bill bill) {
        return billDAO.addBill(bill);
    }

    public List<Bill> getAllBills() {
        return billDAO.getAllBills();
    }

    public Bill getBillById(int id) {
        return billDAO.getBillById(id);
    }

    public int getBillCount() {
        return billDAO.getBillCount();
    }

    public BigDecimal getMonthlySales() {
        return billDAO.getMonthlySales();
    }

    public BigDecimal getTotalSales() {
        return billDAO.getTotalSales();
    }
}