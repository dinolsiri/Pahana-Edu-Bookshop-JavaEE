/**
 * Item Controller
 * Handles all item/inventory-related operations including CRUD operations
 */

class ItemController {
    constructor() {
        this.items = [];
        this.nextId = 1;
        this.categories = ['textbook', 'reference', 'stationery', 'digital'];
    }

    /**
     * Load sample item data
     */
    loadSampleData() {
        this.items = [
            {
                id: 1,
                code: 'BOOK001',
                name: 'Mathematics Grade 10',
                category: 'textbook',
                price: 25.99,
                stock: 50,
                minStock: 10,
                description: 'Grade 10 Mathematics textbook'
            },
            {
                id: 2,
                code: 'BOOK002',
                name: 'English Literature',
                category: 'textbook',
                price: 22.50,
                stock: 30,
                minStock: 5,
                description: 'English Literature reference book'
            },
            {
                id: 3,
                code: 'STAT001',
                name: 'Blue Pen Pack',
                category: 'stationery',
                price: 3.99,
                stock: 100,
                minStock: 20,
                description: 'Pack of 10 blue pens'
            },
            {
                id: 4,
                code: 'BOOK003',
                name: 'Science Grade 11',
                category: 'textbook',
                price: 28.75,
                stock: 8,
                minStock: 10,
                description: 'Grade 11 Science textbook'
            },
            {
                id: 5,
                code: 'REF001',
                name: 'Oxford Dictionary',
                category: 'reference',
                price: 45.00,
                stock: 15,
                minStock: 5,
                description: 'Oxford English Dictionary'
            }
        ];
        this.nextId = 6;
    }

    /**
     * Get all items
     */
    getAllItems() {
        return this.items;
    }

    /**
     * Get item count
     */
    getItemCount() {
        return this.items.length;
    }

    /**
     * Get items in stock
     */
    getItemsInStock() {
        return this.items.filter(item => item.stock > 0);
    }

    /**
     * Get low stock items
     */
    getLowStockItems() {
        return this.items.filter(item => item.stock <= item.minStock);
    }

    /**
     * Get item by ID
     */
    getItemById(id) {
        return this.items.find(item => item.id === parseInt(id));
    }

    /**
     * Get item by code
     */
    getItemByCode(code) {
        return this.items.find(item => item.code === code);
    }

    /**
     * Add new item
     */
    addItem(itemData) {
        // Validate required fields
        if (!this.validateItemData(itemData)) {
            throw new Error('Invalid item data');
        }

        // Check for duplicate item code
        if (this.getItemByCode(itemData.code)) {
            throw new Error('Item code already exists');
        }

        const newItem = {
            id: this.nextId++,
            ...itemData,
            price: parseFloat(itemData.price),
            stock: parseInt(itemData.stock),
            minStock: parseInt(itemData.minStock || 5)
        };

        this.items.push(newItem);
        return newItem;
    }

    /**
     * Update existing item
     */
    updateItem(id, itemData) {
        const index = this.items.findIndex(item => item.id === parseInt(id));
        if (index === -1) {
            throw new Error('Item not found');
        }

        // Validate required fields
        if (!this.validateItemData(itemData)) {
            throw new Error('Invalid item data');
        }

        // Check for duplicate item code (excluding current item)
        const existingItem = this.getItemByCode(itemData.code);
        if (existingItem && existingItem.id !== parseInt(id)) {
            throw new Error('Item code already exists');
        }

        this.items[index] = { 
            ...this.items[index], 
            ...itemData,
            price: parseFloat(itemData.price),
            stock: parseInt(itemData.stock),
            minStock: parseInt(itemData.minStock || 5)
        };
        return this.items[index];
    }

    /**
     * Delete item
     */
    deleteItem(id) {
        const index = this.items.findIndex(item => item.id === parseInt(id));
        if (index === -1) {
            throw new Error('Item not found');
        }

        const deletedItem = this.items.splice(index, 1)[0];
        return deletedItem;
    }

    /**
     * Update item stock
     */
    updateStock(id, quantity) {
        const item = this.getItemById(id);
        if (!item) {
            throw new Error('Item not found');
        }

        if (item.stock + quantity < 0) {
            throw new Error('Insufficient stock');
        }

        item.stock += quantity;
        return item;
    }

    /**
     * Search items
     */
    searchItems(searchTerm, category = '') {
        let filteredItems = this.items;

        if (searchTerm) {
            const term = searchTerm.toLowerCase();
            filteredItems = filteredItems.filter(item => 
                item.name.toLowerCase().includes(term) ||
                item.code.toLowerCase().includes(term) ||
                item.description.toLowerCase().includes(term)
            );
        }

        if (category) {
            filteredItems = filteredItems.filter(item => item.category === category);
        }

        return filteredItems;
    }

    /**
     * Validate item data
     */
    validateItemData(itemData) {
        const required = ['code', 'name', 'category', 'price', 'stock'];
        const isValid = required.every(field => itemData[field] !== undefined && itemData[field] !== '');
        
        if (!isValid) return false;

        // Validate price and stock are numbers
        if (isNaN(parseFloat(itemData.price)) || parseFloat(itemData.price) < 0) return false;
        if (isNaN(parseInt(itemData.stock)) || parseInt(itemData.stock) < 0) return false;

        // Validate category
        if (!this.categories.includes(itemData.category)) return false;

        return true;
    }

    /**
     * Load items table in the UI
     */
    loadItemsTable() {
        const tbody = document.getElementById('itemsTableBody');
        if (!tbody) return;

        if (this.items.length === 0) {
            tbody.innerHTML = '<tr><td colspan="7" class="text-center text-muted">No items found</td></tr>';
            return;
        }
        
        let html = '';
        this.items.forEach(item => {
            const status = item.stock <= item.minStock ? 
                '<span class="badge bg-warning">Low Stock</span>' : 
                '<span class="badge bg-success">In Stock</span>';
                
            html += `
                <tr>
                    <td>${item.code}</td>
                    <td>${item.name}</td>
                    <td><span class="badge bg-secondary">${item.category}</span></td>
                    <td>$${item.price.toFixed(2)}</td>
                    <td>${item.stock}</td>
                    <td>${status}</td>
                    <td>
                        <button class="btn btn-sm btn-outline-primary" onclick="editItem(${item.id})">
                            <i class="bi bi-pencil"></i> Edit
                        </button>
                        <button class="btn btn-sm btn-outline-danger" onclick="deleteItem(${item.id})">
                            <i class="bi bi-trash"></i> Delete
                        </button>
                    </td>
                </tr>
            `;
        });
        tbody.innerHTML = html;
    }

    /**
     * Show add item modal
     */
    showAddItemModal() {
        document.getElementById('itemModalTitle').innerHTML = '<i class="bi bi-plus-square"></i> Add New Item';
        document.getElementById('itemForm').reset();
        document.getElementById('itemId').value = '';
        new bootstrap.Modal(document.getElementById('itemModal')).show();
    }

    /**
     * Show edit item modal
     */
    showEditItemModal(itemId) {
        const item = this.getItemById(itemId);
        if (!item) {
            app.showAlert('Item not found', 'error');
            return;
        }
        
        document.getElementById('itemModalTitle').innerHTML = '<i class="bi bi-pencil"></i> Edit Item';
        document.getElementById('itemId').value = item.id;
        document.getElementById('itemCode').value = item.code;
        document.getElementById('itemName').value = item.name;
        document.getElementById('itemCategory').value = item.category;
        document.getElementById('itemPrice').value = item.price;
        document.getElementById('itemStock').value = item.stock;
        document.getElementById('itemMinStock').value = item.minStock;
        document.getElementById('itemDescription').value = item.description || '';
        
        new bootstrap.Modal(document.getElementById('itemModal')).show();
    }

    /**
     * Save item (add or update)
     */
    saveItem() {
        const form = document.getElementById('itemForm');
        if (!form.checkValidity()) {
            form.reportValidity();
            return;
        }
        
        const itemId = document.getElementById('itemId').value;
        const itemData = {
            code: document.getElementById('itemCode').value.trim(),
            name: document.getElementById('itemName').value.trim(),
            category: document.getElementById('itemCategory').value,
            price: document.getElementById('itemPrice').value,
            stock: document.getElementById('itemStock').value,
            minStock: document.getElementById('itemMinStock').value,
            description: document.getElementById('itemDescription').value.trim()
        };
        
        try {
            if (itemId) {
                // Update existing item
                this.updateItem(itemId, itemData);
                app.showAlert('Item updated successfully!', 'success');
            } else {
                // Add new item
                this.addItem(itemData);
                app.showAlert('Item added successfully!', 'success');
            }
            
            bootstrap.Modal.getInstance(document.getElementById('itemModal')).hide();
            this.loadItemsTable();
            app.updateDashboard();
            
        } catch (error) {
            app.showAlert(error.message, 'danger');
        }
    }

    /**
     * Delete item with confirmation
     */
    deleteItemWithConfirmation(itemId) {
        const item = this.getItemById(itemId);
        if (!item) {
            app.showAlert('Item not found', 'error');
            return;
        }

        if (confirm(`Are you sure you want to delete item "${item.name}"?`)) {
            try {
                this.deleteItem(itemId);
                this.loadItemsTable();
                app.updateDashboard();
                app.showAlert('Item deleted successfully!', 'success');
            } catch (error) {
                app.showAlert(error.message, 'danger');
            }
        }
    }

    /**
     * Search items and update table
     */
    searchItemsAndUpdateTable() {
        const searchTerm = document.getElementById('itemSearch').value;
        const categoryFilter = document.getElementById('categoryFilter').value;
        const filteredItems = this.searchItems(searchTerm, categoryFilter);
        
        const tbody = document.getElementById('itemsTableBody');
        if (!tbody) return;

        if (filteredItems.length === 0) {
            tbody.innerHTML = '<tr><td colspan="7" class="text-center text-muted">No items found matching your criteria</td></tr>';
            return;
        }
        
        let html = '';
        filteredItems.forEach(item => {
            const status = item.stock <= item.minStock ? 
                '<span class="badge bg-warning">Low Stock</span>' : 
                '<span class="badge bg-success">In Stock</span>';
                
            html += `
                <tr>
                    <td>${item.code}</td>
                    <td>${item.name}</td>
                    <td><span class="badge bg-secondary">${item.category}</span></td>
                    <td>$${item.price.toFixed(2)}</td>
                    <td>${item.stock}</td>
                    <td>${status}</td>
                    <td>
                        <button class="btn btn-sm btn-outline-primary" onclick="editItem(${item.id})">
                            <i class="bi bi-pencil"></i> Edit
                        </button>
                        <button class="btn btn-sm btn-outline-danger" onclick="deleteItem(${item.id})">
                            <i class="bi bi-trash"></i> Delete
                        </button>
                    </td>
                </tr>
            `;
        });
        tbody.innerHTML = html;
    }

    /**
     * Clear item search
     */
    clearItemSearch() {
        document.getElementById('itemSearch').value = '';
        document.getElementById('categoryFilter').value = '';
        this.loadItemsTable();
    }
}

// Global functions for backward compatibility with inline event handlers
function showAddItemModal() {
    if (app && app.itemController) {
        app.itemController.showAddItemModal();
    }
}

function editItem(itemId) {
    if (app && app.itemController) {
        app.itemController.showEditItemModal(itemId);
    }
}

function saveItem() {
    if (app && app.itemController) {
        app.itemController.saveItem();
    }
}

function deleteItem(itemId) {
    if (app && app.itemController) {
        app.itemController.deleteItemWithConfirmation(itemId);
    }
}

function searchItems() {
    if (app && app.itemController) {
        app.itemController.searchItemsAndUpdateTable();
    }
}

function clearItemSearch() {
    if (app && app.itemController) {
        app.itemController.clearItemSearch();
    }
}
