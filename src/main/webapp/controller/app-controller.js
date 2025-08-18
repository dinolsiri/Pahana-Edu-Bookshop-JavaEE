/**
 * Main Application Controller
 * Handles application initialization, navigation, and global state management
 */

class AppController {
    constructor() {
        this.currentSection = 'dashboard';
        this.isInitialized = false;
    }

    /**
     * Initialize the application
     */
    init() {
        if (this.isInitialized) return;
        
        console.log('Initializing Pahana Edu Bookshop Application...');
        
        // Set current date for billing
        const billDateElement = document.getElementById('billDate');
        if (billDateElement) {
            billDateElement.value = new Date().toISOString().split('T')[0];
        }
        
        // Initialize controllers
        this.customerController = new CustomerController();
        this.itemController = new ItemController();
        this.billingController = new BillingController();
        this.reportController = new ReportController();
        
        // Load initial data
        this.loadInitialData();
        
        // Set up event listeners
        this.setupEventListeners();
        
        // Update dashboard
        this.updateDashboard();
        
        this.isInitialized = true;
        console.log('Application initialized successfully');
    }

    /**
     * Load initial sample data
     */
    loadInitialData() {
        // This would typically load from backend API
        // For now, we'll use sample data
        this.customerController.loadSampleData();
        this.itemController.loadSampleData();
    }

    /**
     * Set up global event listeners
     */
    setupEventListeners() {
        // Navigation event listeners
        document.querySelectorAll('.sidebar .nav-link').forEach(link => {
            link.addEventListener('click', (e) => {
                e.preventDefault();
                const section = link.getAttribute('onclick')?.match(/showSection\('(.+?)'\)/)?.[1];
                if (section) {
                    this.showSection(section);
                }
            });
        });

        // Search event listeners
        const customerSearch = document.getElementById('customerSearch');
        if (customerSearch) {
            customerSearch.addEventListener('keypress', (e) => {
                if (e.key === 'Enter') {
                    this.customerController.searchCustomers();
                }
            });
        }

        const itemSearch = document.getElementById('itemSearch');
        if (itemSearch) {
            itemSearch.addEventListener('keypress', (e) => {
                if (e.key === 'Enter') {
                    this.itemController.searchItems();
                }
            });
        }
    }

    /**
     * Show specific section and update navigation
     */
    showSection(sectionId) {
        // Hide all sections
        document.querySelectorAll('.page-section').forEach(section => {
            section.classList.remove('active');
        });
        
        // Show selected section
        const targetSection = document.getElementById(sectionId);
        if (targetSection) {
            targetSection.classList.add('active');
        }
        
        // Update navigation
        document.querySelectorAll('.sidebar .nav-link').forEach(link => {
            link.classList.remove('active');
        });
        
        // Find and activate the correct nav link
        const activeLink = document.querySelector(`.sidebar .nav-link[onclick*="'${sectionId}'"]`);
        if (activeLink) {
            activeLink.classList.add('active');
        }
        
        // Load section-specific data
        this.loadSectionData(sectionId);
        this.currentSection = sectionId;
    }

    /**
     * Load data specific to the current section
     */
    loadSectionData(sectionId) {
        switch(sectionId) {
            case 'dashboard':
                this.updateDashboard();
                break;
            case 'customers':
                this.customerController.loadCustomersTable();
                break;
            case 'items':
                this.itemController.loadItemsTable();
                break;
            case 'billing':
                this.billingController.loadDropdowns();
                break;
            case 'reports':
                // Reports are loaded on demand
                break;
        }
    }

    /**
     * Update dashboard statistics and recent data
     */
    updateDashboard() {
        const totalCustomers = this.customerController.getCustomerCount();
        const totalItems = this.itemController.getItemCount();
        const todaySales = this.billingController.getTodaySales();
        const monthlyRevenue = this.billingController.getMonthlyRevenue();

        // Update statistics
        this.updateElement('totalCustomers', totalCustomers);
        this.updateElement('totalItems', totalItems);
        this.updateElement('todaySales', `$${todaySales.toFixed(2)}`);
        this.updateElement('monthlyRevenue', `$${monthlyRevenue.toFixed(2)}`);

        // Update recent data
        this.loadRecentCustomers();
        this.loadRecentTransactions();
    }

    /**
     * Load recent customers for dashboard
     */
    loadRecentCustomers() {
        const recentCustomersDiv = document.getElementById('recentCustomers');
        if (!recentCustomersDiv) return;

        const recentCustomers = this.customerController.getRecentCustomers(3);
        
        if (recentCustomers.length === 0) {
            recentCustomersDiv.innerHTML = '<p class="text-muted text-center">No recent customers found</p>';
            return;
        }
        
        let html = '';
        recentCustomers.forEach(customer => {
            html += `
                <div class="d-flex justify-content-between align-items-center mb-2">
                    <div>
                        <strong>${customer.name}</strong><br>
                        <small class="text-muted">${customer.accountNumber}</small>
                    </div>
                    <small class="text-muted">${customer.registrationDate}</small>
                </div>
            `;
        });
        recentCustomersDiv.innerHTML = html;
    }

    /**
     * Load recent transactions for dashboard
     */
    loadRecentTransactions() {
        const recentTransactionsDiv = document.getElementById('recentTransactions');
        if (!recentTransactionsDiv) return;

        const recentBills = this.billingController.getRecentBills(3);
        
        if (recentBills.length === 0) {
            recentTransactionsDiv.innerHTML = '<p class="text-muted text-center">No recent transactions found</p>';
            return;
        }

        let html = '';
        recentBills.forEach(bill => {
            html += `
                <div class="d-flex justify-content-between align-items-center mb-2">
                    <div>
                        <strong>Bill #${bill.id}</strong><br>
                        <small class="text-muted">${bill.customerName}</small>
                    </div>
                    <div class="text-end">
                        <strong>$${bill.total.toFixed(2)}</strong><br>
                        <small class="text-muted">${bill.date}</small>
                    </div>
                </div>
            `;
        });
        recentTransactionsDiv.innerHTML = html;
    }

    /**
     * Utility method to update element content
     */
    updateElement(id, content) {
        const element = document.getElementById(id);
        if (element) {
            element.textContent = content;
        }
    }

    /**
     * Show alert message
     */
    showAlert(message, type = 'info') {
        const alertDiv = document.createElement('div');
        alertDiv.className = `alert alert-${type} alert-dismissible fade show position-fixed`;
        alertDiv.style.cssText = 'top: 20px; right: 20px; z-index: 9999; min-width: 300px;';
        alertDiv.innerHTML = `
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        `;
        
        document.body.appendChild(alertDiv);
        
        setTimeout(() => {
            if (alertDiv.parentNode) {
                alertDiv.parentNode.removeChild(alertDiv);
            }
        }, 5000);
    }

    /**
     * Handle logout
     */
    logout() {
        if (confirm('Are you sure you want to logout?')) {
            this.showAlert('Logged out successfully!', 'info');
            // In a real application, this would redirect to login page
            // window.location.href = '/login';
        }
    }
}

// Global app instance
let app;

// Initialize application when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    app = new AppController();
    app.init();
});

// Global functions for backward compatibility with inline event handlers
function showSection(sectionId) {
    if (app) {
        app.showSection(sectionId);
    }
}

function logout() {
    if (app) {
        app.logout();
    }
}
