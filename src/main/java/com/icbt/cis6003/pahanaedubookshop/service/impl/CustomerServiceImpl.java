package com.icbt.cis6003.pahanaedubookshop.service.impl;

import com.icbt.cis6003.pahanaedubookshop.dao.CustomerDAO;
import com.icbt.cis6003.pahanaedubookshop.dao.impl.CustomerDAOImpl;
import com.icbt.cis6003.pahanaedubookshop.model.Customer;
import com.icbt.cis6003.pahanaedubookshop.service.CustomerService;
import com.icbt.cis6003.pahanaedubookshop.util.ValidationUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Simple implementation of CustomerService
 */
public class CustomerServiceImpl implements CustomerService {
    
    private final CustomerDAO customerDAO;
    
    public CustomerServiceImpl() {
        this.customerDAO = new CustomerDAOImpl();
    }

    @Override
    public Customer createCustomer(Customer customer) {
        // Validate customer data
        if (!ValidationUtil.validateCustomer(customer)) {
            throw new RuntimeException("Invalid customer data");
        }
        
        // Check if account number already exists
        if (customerDAO.existsByAccountNumber(customer.getAccountNumber())) {
            throw new RuntimeException("Account number already exists: " + customer.getAccountNumber());
        }
        
        // Set registration date if not provided
        if (customer.getRegistrationDate() == null) {
            customer.setRegistrationDate(LocalDate.now());
        }
        
        return customerDAO.save(customer);
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        if (customer.getId() == null) {
            throw new RuntimeException("Customer ID is required for update");
        }
        
        // Validate customer data
        if (!ValidationUtil.validateCustomer(customer)) {
            throw new RuntimeException("Invalid customer data");
        }
        
        // Check if customer exists
        if (!customerDAO.existsById(customer.getId())) {
            throw new RuntimeException("Customer not found with ID: " + customer.getId());
        }
        
        // Check if account number conflicts with another customer
        Optional<Customer> existingCustomer = customerDAO.findByAccountNumber(customer.getAccountNumber());
        if (existingCustomer.isPresent() && !existingCustomer.get().getId().equals(customer.getId())) {
            throw new RuntimeException("Account number already exists: " + customer.getAccountNumber());
        }
        
        return customerDAO.update(customer);
    }

    @Override
    public boolean deleteCustomer(Long customerId) {
        if (customerId == null) {
            throw new RuntimeException("Customer ID is required");
        }
        
        // Check if customer exists
        if (!customerDAO.existsById(customerId)) {
            throw new RuntimeException("Customer not found with ID: " + customerId);
        }
        
        // In a real application, you would check if customer has associated bills
        // For now, we'll allow deletion
        
        return customerDAO.delete(customerId);
    }

    @Override
    public Optional<Customer> getCustomerById(Long customerId) {
        if (customerId == null) {
            return Optional.empty();
        }
        return customerDAO.findById(customerId);
    }

    @Override
    public Optional<Customer> getCustomerByAccountNumber(String accountNumber) {
        if (ValidationUtil.isNullOrEmpty(accountNumber)) {
            return Optional.empty();
        }
        return customerDAO.findByAccountNumber(accountNumber);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerDAO.findAll();
    }

    @Override
    public List<Customer> searchCustomers(String searchTerm) {
        if (ValidationUtil.isNullOrEmpty(searchTerm)) {
            return getAllCustomers();
        }
        return customerDAO.search(searchTerm.trim());
    }

    @Override
    public List<Customer> getCustomersByRegistrationDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new RuntimeException("Start date and end date are required");
        }
        
        if (startDate.isAfter(endDate)) {
            throw new RuntimeException("Start date cannot be after end date");
        }
        
        return customerDAO.findByRegistrationDateBetween(startDate, endDate);
    }

    @Override
    public List<Customer> getRecentCustomers(int limit) {
        if (limit <= 0) {
            limit = 5; // Default limit
        }
        if (limit > 100) {
            limit = 100; // Maximum limit
        }
        
        return customerDAO.findRecentCustomers(limit);
    }

    @Override
    public List<Customer> getCustomersWithPagination(int page, int size) {
        if (page < 0) {
            page = 0;
        }
        if (size <= 0) {
            size = 20; // Default page size
        }
        if (size > 100) {
            size = 100; // Maximum page size
        }
        
        int offset = page * size;
        return customerDAO.findWithPagination(offset, size);
    }

    @Override
    public long getTotalCustomerCount() {
        return customerDAO.count();
    }

    @Override
    public long getNewCustomersThisMonth() {
        return customerDAO.countByCurrentMonth();
    }

    @Override
    public long getNewCustomersToday() {
        LocalDate today = LocalDate.now();
        List<Customer> todaysCustomers = customerDAO.findByRegistrationDate(today);
        return todaysCustomers.size();
    }

    @Override
    public boolean isAccountNumberAvailable(String accountNumber) {
        if (ValidationUtil.isNullOrEmpty(accountNumber)) {
            return false;
        }
        return !customerDAO.existsByAccountNumber(accountNumber);
    }

    @Override
    public String generateNextAccountNumber() {
        // Simple account number generation
        long count = customerDAO.count();
        return String.format("CUST%03d", count + 1);
    }

    @Override
    public boolean validateCustomer(Customer customer) {
        return ValidationUtil.validateCustomer(customer);
    }

    @Override
    public CustomerStatistics getCustomerStatistics() {
        long totalCustomers = getTotalCustomerCount();
        long newThisMonth = getNewCustomersThisMonth();
        long newToday = getNewCustomersToday();
        
        // Calculate growth rate (simple calculation)
        double growthRate = 0.0;
        if (totalCustomers > 0) {
            growthRate = (double) newThisMonth / totalCustomers * 100;
        }
        
        return new CustomerStatistics(totalCustomers, newThisMonth, newToday, growthRate);
    }

    @Override
    public boolean canDeleteCustomer(Long customerId) {
        if (customerId == null) {
            return false;
        }
        
        // Check if customer exists
        if (!customerDAO.existsById(customerId)) {
            return false;
        }
        
        // In a real application, check if customer has bills
        // For now, allow deletion
        return true;
    }

    @Override
    public List<Customer> getCustomersByPhone(String phone) {
        if (ValidationUtil.isNullOrEmpty(phone)) {
            throw new RuntimeException("Phone number is required");
        }
        return customerDAO.findByPhone(phone);
    }

    @Override
    public List<Customer> getCustomersByName(String name) {
        if (ValidationUtil.isNullOrEmpty(name)) {
            return getAllCustomers();
        }
        return customerDAO.findByNameContaining(name.trim());
    }
}
