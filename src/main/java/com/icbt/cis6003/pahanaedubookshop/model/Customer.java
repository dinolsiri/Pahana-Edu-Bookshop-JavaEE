package com.icbt.cis6003.pahanaedubookshop.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Customer entity representing a bookshop customer
 */
public class Customer {
    private Long id;
    private String accountNumber;
    private String name;
    private String address;
    private String phone;
    private String email;
    private LocalDate registrationDate;

    // Default constructor
    public Customer() {
        this.registrationDate = LocalDate.now();
    }

    // Constructor with required fields
    public Customer(String accountNumber, String name, String address, String phone) {
        this();
        this.accountNumber = accountNumber;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    // Full constructor
    public Customer(Long id, String accountNumber, String name, String address, 
                   String phone, String email, LocalDate registrationDate) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.registrationDate = registrationDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    // Validation methods
    public boolean isValid() {
        return accountNumber != null && !accountNumber.trim().isEmpty() &&
               name != null && !name.trim().isEmpty() &&
               address != null && !address.trim().isEmpty() &&
               phone != null && !phone.trim().isEmpty();
    }

    // Business methods
    public String getDisplayName() {
        return name + " (" + accountNumber + ")";
    }

    // Override methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id) &&
               Objects.equals(accountNumber, customer.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountNumber);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", accountNumber='" + accountNumber + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", registrationDate=" + registrationDate +
                '}';
    }
}
