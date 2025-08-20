package com.icbt.cis6003.pahanaedubookshop.dao.impl;

import com.icbt.cis6003.pahanaedubookshop.dao.CustomerDAO;
import com.icbt.cis6003.pahanaedubookshop.model.Customer;
import com.icbt.cis6003.pahanaedubookshop.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Simple implementation of CustomerDAO
 */
public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public Customer save(Customer customer) {
        String sql = "INSERT INTO customers (account_number, name, address, phone, email, registration_date) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, customer.getAccountNumber());
            stmt.setString(2, customer.getName());
            stmt.setString(3, customer.getAddress());
            stmt.setString(4, customer.getPhone());
            stmt.setString(5, customer.getEmail());
            stmt.setDate(6, Date.valueOf(customer.getRegistrationDate() != null ? customer.getRegistrationDate() : LocalDate.now()));
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Creating customer failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    customer.setId(generatedKeys.getLong(1));
                } else {
                    throw new RuntimeException("Creating customer failed, no ID obtained.");
                }
            }
            
            return customer;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving customer: " + e.getMessage(), e);
        }
    }

    @Override
    public Customer update(Customer customer) {
        String sql = "UPDATE customers SET account_number = ?, name = ?, address = ?, phone = ?, email = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, customer.getAccountNumber());
            stmt.setString(2, customer.getName());
            stmt.setString(3, customer.getAddress());
            stmt.setString(4, customer.getPhone());
            stmt.setString(5, customer.getEmail());
            stmt.setLong(6, customer.getId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Updating customer failed, customer not found.");
            }
            
            return customer;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating customer: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM customers WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting customer: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Customer> findById(Long id) {
        String sql = "SELECT * FROM customers WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCustomer(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding customer by ID: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Customer> findByAccountNumber(String accountNumber) {
        String sql = "SELECT * FROM customers WHERE account_number = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, accountNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCustomer(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding customer by account number: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Customer> findAll() {
        String sql = "SELECT * FROM customers ORDER BY name";
        List<Customer> customers = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
            return customers;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all customers: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Customer> findByNameContaining(String name) {
        String sql = "SELECT * FROM customers WHERE name LIKE ? ORDER BY name";
        List<Customer> customers = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + name + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    customers.add(mapResultSetToCustomer(rs));
                }
            }
            return customers;
        } catch (SQLException e) {
            throw new RuntimeException("Error searching customers by name: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Customer> findByPhone(String phone) {
        String sql = "SELECT * FROM customers WHERE phone = ?";
        List<Customer> customers = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, phone);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    customers.add(mapResultSetToCustomer(rs));
                }
            }
            return customers;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding customers by phone: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Customer> findByRegistrationDateBetween(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT * FROM customers WHERE registration_date BETWEEN ? AND ? ORDER BY registration_date DESC";
        List<Customer> customers = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    customers.add(mapResultSetToCustomer(rs));
                }
            }
            return customers;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding customers by date range: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Customer> findByRegistrationDate(LocalDate date) {
        return findByRegistrationDateBetween(date, date);
    }

    @Override
    public List<Customer> search(String searchTerm) {
        String sql = "SELECT * FROM customers WHERE name LIKE ? OR account_number LIKE ? OR phone LIKE ? ORDER BY name";
        List<Customer> customers = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    customers.add(mapResultSetToCustomer(rs));
                }
            }
            return customers;
        } catch (SQLException e) {
            throw new RuntimeException("Error searching customers: " + e.getMessage(), e);
        }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM customers";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error counting customers: " + e.getMessage(), e);
        }
    }

    @Override
    public long countByCurrentMonth() {
        String sql = "SELECT COUNT(*) FROM customers WHERE MONTH(registration_date) = MONTH(CURRENT_DATE()) AND YEAR(registration_date) = YEAR(CURRENT_DATE())";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error counting current month customers: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean existsByAccountNumber(String accountNumber) {
        String sql = "SELECT COUNT(*) FROM customers WHERE account_number = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, accountNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Error checking account number existence: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM customers WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Error checking customer existence: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Customer> findWithPagination(int offset, int limit) {
        String sql = "SELECT * FROM customers ORDER BY name LIMIT ? OFFSET ?";
        List<Customer> customers = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    customers.add(mapResultSetToCustomer(rs));
                }
            }
            return customers;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding customers with pagination: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Customer> findRecentCustomers(int limit) {
        String sql = "SELECT * FROM customers ORDER BY registration_date DESC LIMIT ?";
        List<Customer> customers = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    customers.add(mapResultSetToCustomer(rs));
                }
            }
            return customers;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding recent customers: " + e.getMessage(), e);
        }
    }

    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.getLong("id"));
        customer.setAccountNumber(rs.getString("account_number"));
        customer.setName(rs.getString("name"));
        customer.setAddress(rs.getString("address"));
        customer.setPhone(rs.getString("phone"));
        customer.setEmail(rs.getString("email"));
        
        Date regDate = rs.getDate("registration_date");
        if (regDate != null) {
            customer.setRegistrationDate(regDate.toLocalDate());
        }
        
        return customer;
    }
}
