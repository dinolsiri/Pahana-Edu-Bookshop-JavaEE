package com.pahanaedu.dao;

import com.pahanaedu.model.Bill;
import com.pahanaedu.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class BillDAO {

    public boolean addBill(Bill bill) {
        String sql = "INSERT INTO bills (customer_id, bill_date, subtotal, tax_amount, total_amount) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bill.getCustomerId());
            stmt.setTimestamp(2, Timestamp.valueOf(bill.getBillDate()));
            stmt.setBigDecimal(3, bill.getSubtotal());
            stmt.setBigDecimal(4, bill.getTax());
            stmt.setBigDecimal(5, bill.getTotal());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Bill> getAllBills() {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT * FROM bills ORDER BY bill_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Bill bill = new Bill();
                bill.setId(rs.getInt("id"));
                bill.setCustomerId(rs.getInt("customer_id"));
                bill.setBillDate(rs.getTimestamp("bill_date").toLocalDateTime());
                bill.setSubtotal(rs.getBigDecimal("subtotal"));
                bill.setTax(rs.getBigDecimal("tax_amount"));
                bill.setTotal(rs.getBigDecimal("total_amount"));
                bills.add(bill);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bills;
    }

    public Bill getBillById(int id) {
        String sql = "SELECT * FROM bills WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Bill bill = new Bill();
                bill.setId(rs.getInt("id"));
                bill.setCustomerId(rs.getInt("customer_id"));
                bill.setBillDate(rs.getTimestamp("bill_date").toLocalDateTime());
                bill.setSubtotal(rs.getBigDecimal("subtotal"));
                bill.setTax(rs.getBigDecimal("tax_amount"));
                bill.setTotal(rs.getBigDecimal("total_amount"));
                return bill;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getBillCount() {
        String sql = "SELECT COUNT(*) as count FROM bills";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("count");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public BigDecimal getMonthlySales() {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) as total FROM bills WHERE MONTH(bill_date) = MONTH(CURRENT_DATE()) AND YEAR(bill_date) = YEAR(CURRENT_DATE())";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getBigDecimal("total");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return BigDecimal.ZERO;
    }

    public BigDecimal getTotalSales() {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) as total FROM bills";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getBigDecimal("total");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return BigDecimal.ZERO;
    }
}