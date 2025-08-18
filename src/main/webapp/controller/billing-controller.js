/**
 * Billing Controller
 * Handles all billing and transaction-related operations
 */

class BillingController {
    constructor() {
        this.bills = [];
        this.currentBillItems = [];
        this.nextBillId = 1;
        this.taxRate = 0.10; // 10% tax rate
    }

    /**
     * Get all bills
     */
    getAllBills() {
        return this.bills;
    }

    /**
     * Get recent bills
     */
    getRecentBills(limit = 5) {
        return this.bills
            .sort((a, b) => new Date(b.date) - new Date(a.date))
            .slice(0, limit);
    }

    /**
     * Get today's sales
     */
    getTodaySales() {
        const today = new Date().toISOString().split('T')[0];
        return this.bills
            .filter(bill => bill.date === today)
            .reduce((total, bill) => total + bill.total, 0);
    }

    /**
     * Get monthly revenue
     */
    getMonthlyRevenue() {
        const currentMonth = new Date().getMonth();
        const currentYear = new Date().getFullYear();
        
        return this.bills
            .filter(bill => {
                const billDate = new Date(bill.date);
                return billDate.getMonth() === currentMonth && billDate.getFullYear() === currentYear;
            })
            .reduce((total, bill) => total + bill.total, 0);
    }

    /**
     * Load customer and item dropdowns
     */
    loadDropdowns() {
        this.loadCustomerDropdown();
        this.loadItemDropdown();
    }

    /**
     * Load customer dropdown
     */
    loadCustomerDropdown() {
        const select = document.getElementById('billCustomer');
        if (!select || !app.customerController) return;

        select.innerHTML = '<option value="">Choose a customer...</option>';
        
        const customers = app.customerController.getAllCustomers();
        customers.forEach(customer => {
            select.innerHTML += `<option value="${customer.id}">${customer.name} (${customer.accountNumber})</option>`;
        });
    }

    /**
     * Load item dropdown
     */
    loadItemDropdown() {
        const select = document.getElementById('billItemSelect');
        if (!select || !app.itemController) return;

        select.innerHTML = '<option value="">Select an item...</option>';
        
        const items = app.itemController.getItemsInStock();
        items.forEach(item => {
            select.innerHTML += `<option value="${item.id}">${item.name} - $${item.price.toFixed(2)} (Stock: ${item.stock})</option>`;
        });
    }

    /**
     * Add item to current bill
     */
    addItemToBill() {
        const itemId = document.getElementById('billItemSelect').value;
        const quantity = parseInt(document.getElementById('billItemQuantity').value);
        
        if (!itemId || !quantity || quantity <= 0) {
            app.showAlert('Please select an item and enter a valid quantity', 'warning');
            return;
        }
        
        const item = app.itemController.getItemById(parseInt(itemId));
        if (!item) {
            app.showAlert('Item not found', 'error');
            return;
        }
        
        if (quantity > item.stock) {
            app.showAlert(`Insufficient stock. Available: ${item.stock}`, 'warning');
            return;
        }
        
        // Check if item already exists in bill
        const existingItemIndex = this.currentBillItems.findIndex(billItem => billItem.itemId == itemId);
        if (existingItemIndex !== -1) {
            const newQuantity = this.currentBillItems[existingItemIndex].quantity + quantity;
            if (newQuantity > item.stock) {
                app.showAlert(`Insufficient stock. Available: ${item.stock}, Already in bill: ${this.currentBillItems[existingItemIndex].quantity}`, 'warning');
                return;
            }
            this.currentBillItems[existingItemIndex].quantity = newQuantity;
            this.currentBillItems[existingItemIndex].total = newQuantity * item.price;
        } else {
            this.currentBillItems.push({
                itemId: item.id,
                name: item.name,
                price: item.price,
                quantity: quantity,
                total: item.price * quantity
            });
        }
        
        this.updateBillItemsTable();
        this.updateBillSummary();
        
        // Clear selection
        document.getElementById('billItemSelect').value = '';
        document.getElementById('billItemQuantity').value = '';
    }

    /**
     * Remove item from current bill
     */
    removeItemFromBill(index) {
        if (index >= 0 && index < this.currentBillItems.length) {
            this.currentBillItems.splice(index, 1);
            this.updateBillItemsTable();
            this.updateBillSummary();
        }
    }

    /**
     * Update bill items table
     */
    updateBillItemsTable() {
        const tbody = document.getElementById('billItemsTable');
        if (!tbody) return;
        
        if (this.currentBillItems.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" class="text-center text-muted">No items added</td></tr>';
            return;
        }
        
        let html = '';
        this.currentBillItems.forEach((billItem, index) => {
            html += `
                <tr>
                    <td>${billItem.name}</td>
                    <td>$${billItem.price.toFixed(2)}</td>
                    <td>${billItem.quantity}</td>
                    <td>$${billItem.total.toFixed(2)}</td>
                    <td>
                        <button class="btn btn-sm btn-outline-danger" onclick="removeItemFromBill(${index})">
                            <i class="bi bi-trash"></i>
                        </button>
                    </td>
                </tr>
            `;
        });
        tbody.innerHTML = html;
    }

    /**
     * Update bill summary
     */
    updateBillSummary() {
        const subtotal = this.currentBillItems.reduce((sum, item) => sum + item.total, 0);
        const tax = subtotal * this.taxRate;
        const total = subtotal + tax;
        
        const subtotalElement = document.getElementById('billSubtotal');
        const taxElement = document.getElementById('billTax');
        const totalElement = document.getElementById('billTotal');
        
        if (subtotalElement) subtotalElement.textContent = '$' + subtotal.toFixed(2);
        if (taxElement) taxElement.textContent = '$' + tax.toFixed(2);
        if (totalElement) totalElement.textContent = '$' + total.toFixed(2);
    }

    /**
     * Clear current bill
     */
    clearBill() {
        this.currentBillItems = [];
        this.updateBillItemsTable();
        this.updateBillSummary();
        
        const form = document.getElementById('billingForm');
        if (form) {
            form.reset();
            document.getElementById('billDate').value = new Date().toISOString().split('T')[0];
        }
    }

    /**
     * Generate and save bill
     */
    generateBill() {
        const customerId = document.getElementById('billCustomer').value;
        const billDate = document.getElementById('billDate').value;
        
        if (!customerId) {
            app.showAlert('Please select a customer', 'warning');
            return;
        }
        
        if (this.currentBillItems.length === 0) {
            app.showAlert('Please add items to the bill', 'warning');
            return;
        }
        
        const customer = app.customerController.getCustomerById(parseInt(customerId));
        if (!customer) {
            app.showAlert('Customer not found', 'error');
            return;
        }
        
        const subtotal = this.currentBillItems.reduce((sum, item) => sum + item.total, 0);
        const tax = subtotal * this.taxRate;
        const total = subtotal + tax;
        
        const bill = {
            id: this.nextBillId++,
            customerId: parseInt(customerId),
            customerName: customer.name,
            customerAccount: customer.accountNumber,
            date: billDate,
            items: [...this.currentBillItems],
            subtotal: subtotal,
            tax: tax,
            total: total,
            createdAt: new Date().toISOString()
        };
        
        // Save bill
        this.bills.push(bill);
        
        // Update item stock
        this.currentBillItems.forEach(billItem => {
            try {
                app.itemController.updateStock(billItem.itemId, -billItem.quantity);
            } catch (error) {
                console.error('Error updating stock:', error);
            }
        });
        
        // Show bill preview
        this.showBillPreview(bill);
        
        // Clear current bill
        this.clearBill();
        
        // Update UI
        app.updateDashboard();
        this.loadItemDropdown();
        if (app.itemController) {
            app.itemController.loadItemsTable();
        }
        
        app.showAlert('Bill generated successfully!', 'success');
    }

    /**
     * Show bill preview modal
     */
    showBillPreview(bill) {
        const content = `
            <div class="text-center mb-4">
                <h3>Pahana Edu Bookshop</h3>
                <p>Colombo City, Sri Lanka</p>
                <hr>
            </div>
            <div class="row mb-3">
                <div class="col-6">
                    <strong>Bill #:</strong> ${bill.id}<br>
                    <strong>Date:</strong> ${bill.date}
                </div>
                <div class="col-6 text-end">
                    <strong>Customer:</strong> ${bill.customerName}<br>
                    <strong>Account:</strong> ${bill.customerAccount}
                </div>
            </div>
            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th>Item</th>
                        <th>Price</th>
                        <th>Qty</th>
                        <th>Total</th>
                    </tr>
                </thead>
                <tbody>
                    ${bill.items.map(item => `
                        <tr>
                            <td>${item.name}</td>
                            <td>$${item.price.toFixed(2)}</td>
                            <td>${item.quantity}</td>
                            <td>$${item.total.toFixed(2)}</td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
            <div class="text-end">
                <p><strong>Subtotal: $${bill.subtotal.toFixed(2)}</strong></p>
                <p><strong>Tax (${(this.taxRate * 100)}%): $${bill.tax.toFixed(2)}</strong></p>
                <h4><strong>Total: $${bill.total.toFixed(2)}</strong></h4>
            </div>
            <div class="text-center mt-4">
                <p>Thank you for your business!</p>
            </div>
        `;
        
        const previewContent = document.getElementById('billPreviewContent');
        if (previewContent) {
            previewContent.innerHTML = content;
            new bootstrap.Modal(document.getElementById('billPreviewModal')).show();
        }
    }

    /**
     * Print bill
     */
    printBill() {
        const printContent = document.getElementById('billPreviewContent').innerHTML;
        const printWindow = window.open('', '_blank');
        printWindow.document.write(`
            <html>
                <head>
                    <title>Bill</title>
                    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
                    <style>
                        @media print {
                            .no-print { display: none; }
                        }
                    </style>
                </head>
                <body>
                    <div class="container mt-4">
                        ${printContent}
                    </div>
                </body>
            </html>
        `);
        printWindow.document.close();
        printWindow.print();
    }

    /**
     * Get bill by ID
     */
    getBillById(id) {
        return this.bills.find(bill => bill.id === parseInt(id));
    }

    /**
     * Get bills by customer
     */
    getBillsByCustomer(customerId) {
        return this.bills.filter(bill => bill.customerId === parseInt(customerId));
    }

    /**
     * Get bills by date range
     */
    getBillsByDateRange(startDate, endDate) {
        return this.bills.filter(bill => {
            const billDate = new Date(bill.date);
            return billDate >= new Date(startDate) && billDate <= new Date(endDate);
        });
    }
}

// Global functions for backward compatibility with inline event handlers
function addItemToBill() {
    if (app && app.billingController) {
        app.billingController.addItemToBill();
    }
}

function removeItemFromBill(index) {
    if (app && app.billingController) {
        app.billingController.removeItemFromBill(index);
    }
}

function clearBill() {
    if (app && app.billingController) {
        app.billingController.clearBill();
    }
}

function generateBill() {
    if (app && app.billingController) {
        app.billingController.generateBill();
    }
}

function printBill() {
    if (app && app.billingController) {
        app.billingController.printBill();
    }
}
