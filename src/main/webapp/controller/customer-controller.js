/**
 * Customer Controller
 * Handles all customer-related operations including CRUD operations
 */

class CustomerController {
    constructor() {
        this.customers = [];
        this.nextId = 1;
    }

    /**
     * Load sample customer data
     */
    loadSampleData() {
        this.customers = [
            {
                id: 1,
                accountNumber: 'CUST001',
                name: 'John Doe',
                address: '123 Main St, Colombo 01',
                phone: '+94 77 123 4567',
                email: 'john.doe@email.com',
                registrationDate: '2024-01-15'
            },
            {
                id: 2,
                accountNumber: 'CUST002',
                name: 'Jane Smith',
                address: '456 Galle Road, Colombo 03',
                phone: '+94 71 987 6543',
                email: 'jane.smith@email.com',
                registrationDate: '2024-02-20'
            },
            {
                id: 3,
                accountNumber: 'CUST003',
                name: 'Michael Johnson',
                address: '789 Kandy Road, Colombo 07',
                phone: '+94 76 555 1234',
                email: 'michael.j@email.com',
                registrationDate: '2024-03-10'
            }
        ];
        this.nextId = 4;
    }

    /**
     * Get all customers
     */
    getAllCustomers() {
        return this.customers;
    }

    /**
     * Get customer count
     */
    getCustomerCount() {
        return this.customers.length;
    }

    /**
     * Get recent customers
     */
    getRecentCustomers(limit = 5) {
        return this.customers
            .sort((a, b) => new Date(b.registrationDate) - new Date(a.registrationDate))
            .slice(0, limit);
    }

    /**
     * Get customer by ID
     */
    getCustomerById(id) {
        return this.customers.find(customer => customer.id === parseInt(id));
    }

    /**
     * Get customer by account number
     */
    getCustomerByAccountNumber(accountNumber) {
        return this.customers.find(customer => customer.accountNumber === accountNumber);
    }

    /**
     * Add new customer
     */
    addCustomer(customerData) {
        // Validate required fields
        if (!this.validateCustomerData(customerData)) {
            throw new Error('Invalid customer data');
        }

        // Check for duplicate account number
        if (this.getCustomerByAccountNumber(customerData.accountNumber)) {
            throw new Error('Account number already exists');
        }

        const newCustomer = {
            id: this.nextId++,
            ...customerData,
            registrationDate: new Date().toISOString().split('T')[0]
        };

        this.customers.push(newCustomer);
        return newCustomer;
    }

    /**
     * Update existing customer
     */
    updateCustomer(id, customerData) {
        const index = this.customers.findIndex(customer => customer.id === parseInt(id));
        if (index === -1) {
            throw new Error('Customer not found');
        }

        // Validate required fields
        if (!this.validateCustomerData(customerData)) {
            throw new Error('Invalid customer data');
        }

        // Check for duplicate account number (excluding current customer)
        const existingCustomer = this.getCustomerByAccountNumber(customerData.accountNumber);
        if (existingCustomer && existingCustomer.id !== parseInt(id)) {
            throw new Error('Account number already exists');
        }

        this.customers[index] = { ...this.customers[index], ...customerData };
        return this.customers[index];
    }

    /**
     * Delete customer
     */
    deleteCustomer(id) {
        const index = this.customers.findIndex(customer => customer.id === parseInt(id));
        if (index === -1) {
            throw new Error('Customer not found');
        }

        const deletedCustomer = this.customers.splice(index, 1)[0];
        return deletedCustomer;
    }

    /**
     * Search customers
     */
    searchCustomers(searchTerm) {
        if (!searchTerm) {
            return this.customers;
        }

        const term = searchTerm.toLowerCase();
        return this.customers.filter(customer => 
            customer.name.toLowerCase().includes(term) ||
            customer.accountNumber.toLowerCase().includes(term) ||
            customer.phone.includes(term) ||
            customer.email.toLowerCase().includes(term)
        );
    }

    /**
     * Validate customer data
     */
    validateCustomerData(customerData) {
        const required = ['accountNumber', 'name', 'address', 'phone'];
        return required.every(field => customerData[field] && customerData[field].trim() !== '');
    }

    /**
     * Load customers table in the UI
     */
    loadCustomersTable() {
        const tbody = document.getElementById('customersTableBody');
        if (!tbody) return;

        if (this.customers.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" class="text-center text-muted">No customers found</td></tr>';
            return;
        }
        
        let html = '';
        this.customers.forEach(customer => {
            html += `
                <tr>
                    <td>${customer.accountNumber}</td>
                    <td>${customer.name}</td>
                    <td>${customer.address}</td>
                    <td>${customer.phone}</td>
                    <td>${customer.registrationDate}</td>
                    <td>
                        <button class="btn btn-sm btn-outline-primary" onclick="editCustomer(${customer.id})">
                            <i class="bi bi-pencil"></i> Edit
                        </button>
                        <button class="btn btn-sm btn-outline-danger" onclick="deleteCustomer(${customer.id})">
                            <i class="bi bi-trash"></i> Delete
                        </button>
                    </td>
                </tr>
            `;
        });
        tbody.innerHTML = html;
    }

    /**
     * Show add customer modal
     */
    showAddCustomerModal() {
        document.getElementById('customerModalTitle').innerHTML = '<i class="bi bi-person-plus"></i> Add New Customer';
        document.getElementById('customerForm').reset();
        document.getElementById('customerId').value = '';
        new bootstrap.Modal(document.getElementById('customerModal')).show();
    }

    /**
     * Show edit customer modal
     */
    showEditCustomerModal(customerId) {
        const customer = this.getCustomerById(customerId);
        if (!customer) {
            app.showAlert('Customer not found', 'error');
            return;
        }
        
        document.getElementById('customerModalTitle').innerHTML = '<i class="bi bi-pencil"></i> Edit Customer';
        document.getElementById('customerId').value = customer.id;
        document.getElementById('customerAccountNumber').value = customer.accountNumber;
        document.getElementById('customerName').value = customer.name;
        document.getElementById('customerAddress').value = customer.address;
        document.getElementById('customerPhone').value = customer.phone;
        document.getElementById('customerEmail').value = customer.email || '';
        
        new bootstrap.Modal(document.getElementById('customerModal')).show();
    }

    /**
     * Save customer (add or update)
     */
    saveCustomer() {
        const form = document.getElementById('customerForm');
        if (!form.checkValidity()) {
            form.reportValidity();
            return;
        }
        
        const customerId = document.getElementById('customerId').value;
        const customerData = {
            accountNumber: document.getElementById('customerAccountNumber').value.trim(),
            name: document.getElementById('customerName').value.trim(),
            address: document.getElementById('customerAddress').value.trim(),
            phone: document.getElementById('customerPhone').value.trim(),
            email: document.getElementById('customerEmail').value.trim()
        };
        
        try {
            if (customerId) {
                // Update existing customer
                this.updateCustomer(customerId, customerData);
                app.showAlert('Customer updated successfully!', 'success');
            } else {
                // Add new customer
                this.addCustomer(customerData);
                app.showAlert('Customer added successfully!', 'success');
            }
            
            bootstrap.Modal.getInstance(document.getElementById('customerModal')).hide();
            this.loadCustomersTable();
            app.updateDashboard();
            
        } catch (error) {
            app.showAlert(error.message, 'danger');
        }
    }

    /**
     * Delete customer with confirmation
     */
    deleteCustomerWithConfirmation(customerId) {
        const customer = this.getCustomerById(customerId);
        if (!customer) {
            app.showAlert('Customer not found', 'error');
            return;
        }

        if (confirm(`Are you sure you want to delete customer "${customer.name}"?`)) {
            try {
                this.deleteCustomer(customerId);
                this.loadCustomersTable();
                app.updateDashboard();
                app.showAlert('Customer deleted successfully!', 'success');
            } catch (error) {
                app.showAlert(error.message, 'danger');
            }
        }
    }

    /**
     * Search customers and update table
     */
    searchCustomersAndUpdateTable() {
        const searchTerm = document.getElementById('customerSearch').value;
        const filteredCustomers = this.searchCustomers(searchTerm);
        
        const tbody = document.getElementById('customersTableBody');
        if (!tbody) return;

        if (filteredCustomers.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" class="text-center text-muted">No customers found matching your search</td></tr>';
            return;
        }
        
        let html = '';
        filteredCustomers.forEach(customer => {
            html += `
                <tr>
                    <td>${customer.accountNumber}</td>
                    <td>${customer.name}</td>
                    <td>${customer.address}</td>
                    <td>${customer.phone}</td>
                    <td>${customer.registrationDate}</td>
                    <td>
                        <button class="btn btn-sm btn-outline-primary" onclick="editCustomer(${customer.id})">
                            <i class="bi bi-pencil"></i> Edit
                        </button>
                        <button class="btn btn-sm btn-outline-danger" onclick="deleteCustomer(${customer.id})">
                            <i class="bi bi-trash"></i> Delete
                        </button>
                    </td>
                </tr>
            `;
        });
        tbody.innerHTML = html;
    }

    /**
     * Clear customer search
     */
    clearCustomerSearch() {
        document.getElementById('customerSearch').value = '';
        this.loadCustomersTable();
    }
}

// Global functions for backward compatibility with inline event handlers
function showAddCustomerModal() {
    if (app && app.customerController) {
        app.customerController.showAddCustomerModal();
    }
}

function editCustomer(customerId) {
    if (app && app.customerController) {
        app.customerController.showEditCustomerModal(customerId);
    }
}

function saveCustomer() {
    if (app && app.customerController) {
        app.customerController.saveCustomer();
    }
}

function deleteCustomer(customerId) {
    if (app && app.customerController) {
        app.customerController.deleteCustomerWithConfirmation(customerId);
    }
}

function searchCustomers() {
    if (app && app.customerController) {
        app.customerController.searchCustomersAndUpdateTable();
    }
}

function clearCustomerSearch() {
    if (app && app.customerController) {
        app.customerController.clearCustomerSearch();
    }
}
