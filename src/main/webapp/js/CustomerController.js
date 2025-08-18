// Customer Controller

const BASE_URL = "http://localhost:8080/PahanaEduBillingSystem_war_exploded";

const customerController = {
    customers: [],
    filteredCustomers: [],

    init() {
        this.setupEventListeners();
    },

    setupEventListeners() {
        // Search functionality
        const searchInput = document.getElementById('customerSearch');
        if (searchInput) {
            searchInput.addEventListener('input', (e) => {
                this.filterCustomers(e.target.value);
            });
        }
    },

    loadCustomers() {
        this.showLoading();
        
        fetch(BASE_URL + '/api/customers')
            .then(response => {
                if (response.status === 401) {
                    throw new Error('Authentication required');
                }
                if (response.status === 404) {
                    throw new Error('API endpoint not found');
                }
                if (!response.ok) {
                    throw new Error(`HTTP ${response.status}: ${response.statusText}`);
                }
                return response.json();
            })
            .then(data => {
                this.customers = data;
                this.filteredCustomers = [...data];
                this.renderCustomersTable();
                this.updateStats();
                this.hideLoading();
            })
            .catch(error => {
                console.error('Error loading customers:', error);
                this.showError(`Failed to load customers: ${error.message}`);
                this.hideLoading();
            });
    },

    filterCustomers(searchTerm) {
        if (!searchTerm.trim()) {
            this.filteredCustomers = [...this.customers];
        } else {
            const term = searchTerm.toLowerCase();
            this.filteredCustomers = this.customers.filter(customer => 
                customer.name.toLowerCase().includes(term) ||
                customer.email.toLowerCase().includes(term) ||
                customer.phone.includes(term) ||
                (customer.accountNumber && customer.accountNumber.toLowerCase().includes(term))
            );
        }
        this.renderCustomersTable();
        this.updateStats();
    },

    renderCustomersTable() {
        const tbody = document.getElementById('customersTable');
        
        if (this.filteredCustomers.length === 0) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="6" class="empty-state">
                        <i class="fas fa-users"></i>
                        <h4>No Customers Found</h4>
                        <p>Start by adding your first customer to the system</p>
                    </td>
                </tr>
            `;
            return;
        }

        tbody.innerHTML = '';

        this.filteredCustomers.forEach((customer, index) => {
            const row = document.createElement('tr');
            row.style.animationDelay = `${index * 0.1}s`;
            row.className = 'table-row-animate';
            
            row.innerHTML = `
                <td>
                    <span class="account-badge">
                        ${customer.accountNumber || customer.id}
                    </span>
                </td>
                <td>
                    <div class="customer-info">
                        <i class="fas fa-user-circle me-2 text-primary"></i>
                        <strong>${customer.name}</strong>
                    </div>
                </td>
                <td>
                    <div class="contact-info">
                        <i class="fas fa-envelope me-2 text-info"></i>
                        ${customer.email}
                    </div>
                </td>
                <td>
                    <div class="contact-info">
                        <i class="fas fa-phone me-2 text-success"></i>
                        ${customer.phone}
                    </div>
                </td>
                <td>
                    <div class="address-info">
                        <i class="fas fa-map-marker-alt me-2 text-warning"></i>
                        ${customer.address || 'N/A'}
                    </div>
                </td>
                <td>
                    <div class="action-buttons">
                        <button class="btn btn-sm btn-primary" onclick="customerController.editCustomer(${customer.id})" title="Edit Customer">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn btn-sm btn-danger" onclick="customerController.deleteCustomer(${customer.id})" title="Delete Customer">
                            <i class="fas fa-trash"></i>
                        </button>
                    </div>
                </td>
            `;
            tbody.appendChild(row);
        });

        // Add animation class
        setTimeout(() => {
            tbody.querySelectorAll('.table-row-animate').forEach(row => {
                row.classList.add('animate-in');
            });
        }, 100);
    },

    updateStats() {
        const countElement = document.getElementById('customerCount');
        if (countElement) {
            countElement.textContent = this.filteredCustomers.length;
        }
    },

    showLoading() {
        const tbody = document.getElementById('customersTable');
        tbody.innerHTML = `
            <tr>
                <td colspan="6" class="loading-row">
                    <div class="loading-spinner"></div>
                    <div>Loading customers...</div>
                </td>
            </tr>
        `;
    },

    hideLoading() {
        // Loading is hidden when table is rendered
    },

    showError(message) {
        showAlert(message, 'danger');
    },

    editCustomer(id) {
        const customer = this.customers.find(c => c.id === id);
        if (customer) {
            document.getElementById('customerId').value = customer.id;
            document.getElementById('customerName').value = customer.name;
            document.getElementById('customerEmail').value = customer.email;
            document.getElementById('customerPhone').value = customer.phone;
            document.getElementById('customerAddress').value = customer.address || '';
            
            const modal = new bootstrap.Modal(document.getElementById('customerModal'));
            modal.show();
        }
    },

    deleteCustomer(id) {
        const customer = this.customers.find(c => c.id === id);
        if (!customer) return;

        if (confirm(`Are you sure you want to delete customer "${customer.name}"? This action cannot be undone.`)) {
            fetch(BASE_URL + `/api/customers/${id}`, { method: 'DELETE' })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        showAlert('Customer deleted successfully', 'success');
                        this.loadCustomers();
                    } else {
                        showAlert('Failed to delete customer', 'danger');
                    }
                })
                .catch(error => {
                    console.error('Error deleting customer:', error);
                    showAlert('Failed to delete customer', 'danger');
                });
        }
    }
};

function showAddCustomerModal() {
    document.getElementById('customerForm').reset();
    document.getElementById('customerId').value = '';
    const modal = new bootstrap.Modal(document.getElementById('customerModal'));
    modal.show();
}

function saveCustomer() {
    const form = document.getElementById('customerForm');
    
    const customer = {
        id: document.getElementById('customerId').value,
        name: document.getElementById('customerName').value.trim(),
        email: document.getElementById('customerEmail').value.trim(),
        phone: document.getElementById('customerPhone').value.trim(),
        address: document.getElementById('customerAddress').value.trim()
    };

    // Enhanced validation
    if (!customer.name) {
        showAlert('Please enter customer name', 'danger');
        return;
    }

    if (!customer.email) {
        showAlert('Please enter email address', 'danger');
        return;
    }

    if (!validateEmail(customer.email)) {
        showAlert('Please enter a valid email address', 'danger');
        return;
    }

    if (!customer.phone) {
        showAlert('Please enter phone number', 'danger');
        return;
    }

    if (!validatePhone(customer.phone)) {
        showAlert('Please enter a valid phone number', 'danger');
        return;
    }

    const url = customer.id ? BASE_URL + `/api/customers/${customer.id}` : BASE_URL + '/api/customers';
    const method = customer.id ? 'PUT' : 'POST';

    fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(customer)
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            showAlert(customer.id ? 'Customer updated successfully' : 'Customer added successfully', 'success');
            customerController.loadCustomers();
            bootstrap.Modal.getInstance(document.getElementById('customerModal')).hide();
        } else {
            showAlert('Failed to save customer', 'danger');
        }
    })
    .catch(error => {
        console.error('Error saving customer:', error);
        showAlert('Failed to save customer', 'danger');
    });
}