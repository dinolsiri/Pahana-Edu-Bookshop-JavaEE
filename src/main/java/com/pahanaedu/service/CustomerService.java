package com.pahanaedu.service;

import com.pahanaedu.dao.CustomerDAO;
import com.pahanaedu.model.Customer;

import java.util.List;

public class CustomerService {
    
    private CustomerDAO customerDAO;

    public CustomerService() {
        this.customerDAO = new CustomerDAO();
    }

    public List<Customer> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }

    public Customer getCustomerById(int id) {
        return customerDAO.getCustomerById(id);
    }

    public boolean addCustomer(Customer customer) {
        // Generate account number
        String accountNumber = generateAccountNumber();
        customer.setAccountNumber(accountNumber);
        return customerDAO.addCustomer(customer);
    }

    public boolean updateCustomer(Customer customer) {
        return customerDAO.updateCustomer(customer);
    }

    public boolean deleteCustomer(int id) {
        return customerDAO.deleteCustomer(id);
    }

    public int getCustomerCount() {
        return customerDAO.getCustomerCount();
    }

    private String generateAccountNumber() {
        // Generate account number format: ACC + timestamp
        return "ACC" + System.currentTimeMillis();
    }
}