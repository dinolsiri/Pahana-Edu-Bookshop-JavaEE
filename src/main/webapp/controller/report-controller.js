/**
 * Report Controller
 * Handles all reporting and analytics operations
 */

class ReportController {
    constructor() {
        this.reportTypes = {
            CUSTOMER: 'customer',
            INVENTORY: 'inventory',
            SALES: 'sales',
            LOW_STOCK: 'low_stock',
            MONTHLY_SALES: 'monthly_sales'
        };
    }

    /**
     * Generate customer report
     */
    generateCustomerReport() {
        if (!app.customerController) {
            app.showAlert('Customer controller not available', 'error');
            return;
        }

        const customers = app.customerController.getAllCustomers();
        const reportContent = document.getElementById('reportContent');
        
        if (!reportContent) return;

        let html = `
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h5><i class="bi bi-people"></i> Customer Report</h5>
                <button class="btn btn-sm btn-outline-primary" onclick="exportReport('customer')">
                    <i class="bi bi-download"></i> Export
                </button>
            </div>
            <div class="row mb-3">
                <div class="col-md-3">
                    <div class="card bg-primary text-white">
                        <div class="card-body text-center">
                            <h4>${customers.length}</h4>
                            <small>Total Customers</small>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card bg-success text-white">
                        <div class="card-body text-center">
                            <h4>${this.getNewCustomersThisMonth()}</h4>
                            <small>New This Month</small>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card bg-info text-white">
                        <div class="card-body text-center">
                            <h4>${this.getActiveCustomers()}</h4>
                            <small>Active Customers</small>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card bg-warning text-white">
                        <div class="card-body text-center">
                            <h4>${this.getCustomerGrowthRate()}%</h4>
                            <small>Growth Rate</small>
                        </div>
                    </div>
                </div>
            </div>
            <div class="table-responsive">
                <table class="table table-striped table-hover">
                    <thead class="table-dark">
                        <tr>
                            <th>Account Number</th>
                            <th>Name</th>
                            <th>Phone</th>
                            <th>Registration Date</th>
                            <th>Total Purchases</th>
                            <th>Last Purchase</th>
                        </tr>
                    </thead>
                    <tbody>
        `;
        
        customers.forEach(customer => {
            const customerBills = this.getCustomerBills(customer.id);
            const totalPurchases = customerBills.reduce((sum, bill) => sum + bill.total, 0);
            const lastPurchase = customerBills.length > 0 ? 
                customerBills.sort((a, b) => new Date(b.date) - new Date(a.date))[0].date : 'Never';
            
            html += `
                <tr>
                    <td>${customer.accountNumber}</td>
                    <td>${customer.name}</td>
                    <td>${customer.phone}</td>
                    <td>${customer.registrationDate}</td>
                    <td>$${totalPurchases.toFixed(2)}</td>
                    <td>${lastPurchase}</td>
                </tr>
            `;
        });
        
        html += `
                    </tbody>
                </table>
            </div>
        `;
        
        reportContent.innerHTML = html;
    }

    /**
     * Generate inventory report
     */
    generateInventoryReport() {
        if (!app.itemController) {
            app.showAlert('Item controller not available', 'error');
            return;
        }

        const items = app.itemController.getAllItems();
        const lowStockItems = app.itemController.getLowStockItems();
        const reportContent = document.getElementById('reportContent');
        
        if (!reportContent) return;

        const totalValue = items.reduce((sum, item) => sum + (item.price * item.stock), 0);
        const categories = [...new Set(items.map(item => item.category))];

        let html = `
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h5><i class="bi bi-box"></i> Inventory Report</h5>
                <button class="btn btn-sm btn-outline-primary" onclick="exportReport('inventory')">
                    <i class="bi bi-download"></i> Export
                </button>
            </div>
            <div class="row mb-3">
                <div class="col-md-3">
                    <div class="card bg-primary text-white">
                        <div class="card-body text-center">
                            <h4>${items.length}</h4>
                            <small>Total Items</small>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card bg-warning text-white">
                        <div class="card-body text-center">
                            <h4>${lowStockItems.length}</h4>
                            <small>Low Stock Items</small>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card bg-success text-white">
                        <div class="card-body text-center">
                            <h4>$${totalValue.toFixed(2)}</h4>
                            <small>Total Value</small>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card bg-info text-white">
                        <div class="card-body text-center">
                            <h4>${categories.length}</h4>
                            <small>Categories</small>
                        </div>
                    </div>
                </div>
            </div>
        `;

        // Category breakdown
        html += `
            <div class="row mb-3">
                <div class="col-12">
                    <h6>Inventory by Category</h6>
                    <div class="row">
        `;

        categories.forEach(category => {
            const categoryItems = items.filter(item => item.category === category);
            const categoryValue = categoryItems.reduce((sum, item) => sum + (item.price * item.stock), 0);
            
            html += `
                <div class="col-md-3 mb-2">
                    <div class="card">
                        <div class="card-body text-center">
                            <h6>${category.charAt(0).toUpperCase() + category.slice(1)}</h6>
                            <p class="mb-1">${categoryItems.length} items</p>
                            <small class="text-muted">$${categoryValue.toFixed(2)}</small>
                        </div>
                    </div>
                </div>
            `;
        });

        html += `
                    </div>
                </div>
            </div>
            <div class="table-responsive">
                <table class="table table-striped table-hover">
                    <thead class="table-dark">
                        <tr>
                            <th>Item Code</th>
                            <th>Name</th>
                            <th>Category</th>
                            <th>Price</th>
                            <th>Stock</th>
                            <th>Value</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
        `;
        
        items.forEach(item => {
            const status = item.stock <= item.minStock ? 
                '<span class="badge bg-warning">Low Stock</span>' : 
                '<span class="badge bg-success">In Stock</span>';
            const value = item.price * item.stock;
            
            html += `
                <tr>
                    <td>${item.code}</td>
                    <td>${item.name}</td>
                    <td><span class="badge bg-secondary">${item.category}</span></td>
                    <td>$${item.price.toFixed(2)}</td>
                    <td>${item.stock}</td>
                    <td>$${value.toFixed(2)}</td>
                    <td>${status}</td>
                </tr>
            `;
        });
        
        html += `
                    </tbody>
                </table>
            </div>
        `;
        
        reportContent.innerHTML = html;
    }

    /**
     * Generate sales report
     */
    generateSalesReport() {
        if (!app.billingController) {
            app.showAlert('Billing controller not available', 'error');
            return;
        }

        const bills = app.billingController.getAllBills();
        const reportContent = document.getElementById('reportContent');
        
        if (!reportContent) return;

        const totalSales = bills.reduce((sum, bill) => sum + bill.total, 0);
        const todaySales = app.billingController.getTodaySales();
        const monthlyRevenue = app.billingController.getMonthlyRevenue();
        const averageOrderValue = bills.length > 0 ? totalSales / bills.length : 0;

        let html = `
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h5><i class="bi bi-currency-dollar"></i> Sales Report</h5>
                <button class="btn btn-sm btn-outline-primary" onclick="exportReport('sales')">
                    <i class="bi bi-download"></i> Export
                </button>
            </div>
            <div class="row mb-3">
                <div class="col-md-3">
                    <div class="card bg-success text-white">
                        <div class="card-body text-center">
                            <h4>$${totalSales.toFixed(2)}</h4>
                            <small>Total Sales</small>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card bg-primary text-white">
                        <div class="card-body text-center">
                            <h4>$${todaySales.toFixed(2)}</h4>
                            <small>Today's Sales</small>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card bg-info text-white">
                        <div class="card-body text-center">
                            <h4>$${monthlyRevenue.toFixed(2)}</h4>
                            <small>Monthly Revenue</small>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card bg-warning text-white">
                        <div class="card-body text-center">
                            <h4>$${averageOrderValue.toFixed(2)}</h4>
                            <small>Avg Order Value</small>
                        </div>
                    </div>
                </div>
            </div>
            <div class="table-responsive">
                <table class="table table-striped table-hover">
                    <thead class="table-dark">
                        <tr>
                            <th>Bill ID</th>
                            <th>Customer</th>
                            <th>Date</th>
                            <th>Items</th>
                            <th>Subtotal</th>
                            <th>Tax</th>
                            <th>Total</th>
                        </tr>
                    </thead>
                    <tbody>
        `;
        
        if (bills.length === 0) {
            html += '<tr><td colspan="7" class="text-center text-muted">No sales data available</td></tr>';
        } else {
            bills.sort((a, b) => new Date(b.date) - new Date(a.date)).forEach(bill => {
                html += `
                    <tr>
                        <td>#${bill.id}</td>
                        <td>${bill.customerName}</td>
                        <td>${bill.date}</td>
                        <td>${bill.items.length}</td>
                        <td>$${bill.subtotal.toFixed(2)}</td>
                        <td>$${bill.tax.toFixed(2)}</td>
                        <td><strong>$${bill.total.toFixed(2)}</strong></td>
                    </tr>
                `;
            });
        }
        
        html += `
                    </tbody>
                </table>
            </div>
        `;
        
        reportContent.innerHTML = html;
    }

    /**
     * Get customer bills
     */
    getCustomerBills(customerId) {
        if (!app.billingController) return [];
        return app.billingController.getBillsByCustomer(customerId);
    }

    /**
     * Get new customers this month
     */
    getNewCustomersThisMonth() {
        if (!app.customerController) return 0;
        
        const currentMonth = new Date().getMonth();
        const currentYear = new Date().getFullYear();
        
        return app.customerController.getAllCustomers().filter(customer => {
            const regDate = new Date(customer.registrationDate);
            return regDate.getMonth() === currentMonth && regDate.getFullYear() === currentYear;
        }).length;
    }

    /**
     * Get active customers (customers who made purchases)
     */
    getActiveCustomers() {
        if (!app.billingController || !app.customerController) return 0;
        
        const bills = app.billingController.getAllBills();
        const activeCustomerIds = [...new Set(bills.map(bill => bill.customerId))];
        return activeCustomerIds.length;
    }

    /**
     * Get customer growth rate
     */
    getCustomerGrowthRate() {
        // Mock calculation - in real app, this would compare with previous period
        return Math.floor(Math.random() * 20) + 5; // Random 5-25%
    }

    /**
     * Export report (placeholder function)
     */
    exportReport(reportType) {
        app.showAlert(`Export functionality for ${reportType} report will be implemented in the backend`, 'info');
    }
}

// Global functions for backward compatibility with inline event handlers
function generateCustomerReport() {
    if (app && app.reportController) {
        app.reportController.generateCustomerReport();
    }
}

function generateInventoryReport() {
    if (app && app.reportController) {
        app.reportController.generateInventoryReport();
    }
}

function generateSalesReport() {
    if (app && app.reportController) {
        app.reportController.generateSalesReport();
    }
}

function exportReport(reportType) {
    if (app && app.reportController) {
        app.reportController.exportReport(reportType);
    }
}
