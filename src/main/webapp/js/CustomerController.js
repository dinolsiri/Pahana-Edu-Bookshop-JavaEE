// Customer Controller
const customerController = {
    customers: [],

    loadCustomers() {
        fetch('/api/customers')
            .then(response => response.json())
            .then(data => {
                this.customers = data;
                this.renderCustomersTable();
            })
            .catch(error => {
                console.error('Error loading customers:', error);
                showAlert('Failed to load customers', 'danger');
            });
    },

    renderCustomersTable() {
        const tbody = document.getElementById('customersTable');
        tbody.innerHTML = '';

        this.customers.forEach(customer => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${customer.accountNumber || customer.id}</td>
                <td>${customer.name}</td>
                <td>${customer.email}</td>
                <td>${customer.phone}</td>
                <td>${customer.address || 'N/A'}</td>
                <td>
                    <button class="btn btn-sm btn-primary" onclick="customerController.editCustomer(${customer.id})">Edit</button>
                    <button class="btn btn-sm btn-danger" onclick="customerController.deleteCustomer(${customer.id})">Delete</button>
                </td>
            `;
            tbody.appendChild(row);
        });
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
        if (confirm('Are you sure you want to delete this customer?')) {
            fetch(`/api/customers/${id}`, { method: 'DELETE' })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        showAlert('Customer deleted successfully');
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
    const formData = new FormData(form);
    
    const customer = {
        id: document.getElementById('customerId').value,
        name: document.getElementById('customerName').value,
        email: document.getElementById('customerEmail').value,
        phone: document.getElementById('customerPhone').value,
        address: document.getElementById('customerAddress').value
    };

    // Validation
    if (!customer.name || !customer.email || !customer.phone) {
        showAlert('Please fill in all required fields', 'danger');
        return;
    }

    if (!validateEmail(customer.email)) {
        showAlert('Please enter a valid email address', 'danger');
        return;
    }

    if (!validatePhone(customer.phone)) {
        showAlert('Please enter a valid phone number', 'danger');
        return;
    }

    const url = customer.id ? `/api/customers/${customer.id}` : '/api/customers';
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
            showAlert(customer.id ? 'Customer updated successfully' : 'Customer added successfully');
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