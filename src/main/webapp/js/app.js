// Global variables
let currentUser = null;
let currentSection = 'dashboard';

// Initialize application
document.addEventListener('DOMContentLoaded', function() {
    // Skip login and go directly to dashboard
    currentUser = { email: 'admin@pahanaedu.com', name: 'Admin User' };
    showMainContent();
    loadDashboard();
});

// Comment out or modify checkSession to skip authentication
function checkSession() {
    // Skip session check for now - go directly to dashboard
    currentUser = { email: 'admin@pahanaedu.com', name: 'Admin User' };
    showMainContent();
    loadDashboard();
}

// Show/hide sections
function showLoginSection() {
    document.getElementById('loginSection').style.display = 'block';
    document.getElementById('mainContent').style.display = 'none';
    document.getElementById('navMenu').style.display = 'none';
}

function showMainContent() {
    document.getElementById('loginSection').style.display = 'none';
    document.getElementById('mainContent').style.display = 'block';
    document.getElementById('navMenu').style.display = 'flex';
}

function showSection(sectionName) {
    // Hide all sections
    const sections = ['dashboard', 'customers', 'items', 'billing', 'reports', 'help'];
    sections.forEach(section => {
        const element = document.getElementById(section + 'Section');
        if (element) {
            element.style.display = 'none';
        }
    });

    // Show selected section
    const targetSection = document.getElementById(sectionName + 'Section');
    if (targetSection) {
        targetSection.style.display = 'block';
        currentSection = sectionName;

        // Load section-specific data
        switch (sectionName) {
            case 'dashboard':
                loadDashboard();
                break;
            case 'customers':
                customerController.loadCustomers();
                break;
            case 'items':
                itemController.loadItems();
                break;
            case 'billing':
                billingController.initBilling();
                break;
            case 'reports':
                reportController.loadReports();
                break;
        }
    }
}

function loadDashboard() {
    // Load dashboard statistics
    Promise.all([
        fetch('/api/customers/count').then(r => r.json()),
        fetch('/api/items/count').then(r => r.json()),
        fetch('/api/bills/monthly-sales').then(r => r.json()),
        fetch('/api/bills/count').then(r => r.json())
    ]).then(([customers, items, sales, bills]) => {
        document.getElementById('totalCustomers').textContent = customers.count || 0;
        document.getElementById('totalItems').textContent = items.count || 0;
        document.getElementById('monthlySales').textContent = '$' + (sales.total || 0).toFixed(2);
        document.getElementById('totalBills').textContent = bills.count || 0;
    }).catch(console.error);
}

function logout() {
    if (confirm('Are you sure you want to logout?')) {
        fetch('/api/auth/logout', {method: 'POST'})
            .then(() => {
                currentUser = null;
                showLoginSection();
            });
    }
}

// Utility functions
function showAlert(message, type = 'success') {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} alert-dismissible fade show`;
    alertDiv.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;

    const container = document.querySelector('.container');
    container.insertBefore(alertDiv, container.firstChild);

    setTimeout(() => {
        alertDiv.remove();
    }, 5000);
}

function formatCurrency(amount) {
    return '$' + parseFloat(amount).toFixed(2);
}

function validateEmail(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email);
}

function validatePhone(phone) {
    const re = /^[\+]?[1-9][\d]{0,15}$/;
    return re.test(phone.replace(/\s/g, ''));
}