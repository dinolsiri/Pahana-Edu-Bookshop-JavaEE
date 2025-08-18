-- Create database
CREATE DATABASE IF NOT EXISTS pahana_edu_bookshop_dinol;
USE pahana_edu_bookshop_dinol;

-- Drop tables if they exist (for clean setup)
DROP TABLE IF EXISTS bill_items;
DROP TABLE IF EXISTS bills;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS customers;

-- Create customers table
CREATE TABLE customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    address TEXT NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    registration_date DATE NOT NULL DEFAULT (CURRENT_DATE),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_account_number (account_number),
    INDEX idx_name (name),
    INDEX idx_phone (phone),
    INDEX idx_registration_date (registration_date)
);

-- Create items table
CREATE TABLE items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    category ENUM('TEXTBOOK', 'REFERENCE', 'STATIONERY', 'DIGITAL') NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    min_stock INT NOT NULL DEFAULT 5,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_code (code),
    INDEX idx_name (name),
    INDEX idx_category (category),
    INDEX idx_stock (stock),
    INDEX idx_price (price)
);

-- Create bills table
CREATE TABLE bills (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    customer_name VARCHAR(100) NOT NULL,
    customer_account_number VARCHAR(20) NOT NULL,
    bill_date DATE NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    tax_amount DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    total_amount DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    tax_rate DECIMAL(5, 4) NOT NULL DEFAULT 0.1000,
    status ENUM('DRAFT', 'FINALIZED', 'PAID', 'CANCELLED') NOT NULL DEFAULT 'DRAFT',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE RESTRICT,
    INDEX idx_customer_id (customer_id),
    INDEX idx_bill_date (bill_date),
    INDEX idx_status (status),
    INDEX idx_total_amount (total_amount),
    INDEX idx_created_at (created_at)
);

-- Create bill_items table
CREATE TABLE bill_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bill_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    item_code VARCHAR(20) NOT NULL,
    item_name VARCHAR(200) NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    quantity INT NOT NULL,
    total DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (bill_id) REFERENCES bills(id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE RESTRICT,
    INDEX idx_bill_id (bill_id),
    INDEX idx_item_id (item_id),
    UNIQUE KEY unique_bill_item (bill_id, item_id)
);

-- Insert sample data

-- Sample customers
INSERT INTO customers (account_number, name, address, phone, email, registration_date) VALUES
('CUST001', 'John Doe', '123 Main Street, Colombo 01', '+94 77 123 4567', 'john.doe@email.com', '2024-01-15'),
('CUST002', 'Jane Smith', '456 Galle Road, Colombo 03', '+94 71 987 6543', 'jane.smith@email.com', '2024-02-20'),
('CUST003', 'Michael Johnson', '789 Kandy Road, Colombo 07', '+94 76 555 1234', 'michael.j@email.com', '2024-03-10'),
('CUST004', 'Sarah Williams', '321 Negombo Road, Colombo 11', '+94 75 888 9999', 'sarah.w@email.com', '2024-03-25'),
('CUST005', 'David Brown', '654 Dehiwala Road, Colombo 06', '+94 78 777 6666', 'david.brown@email.com', '2024-04-05');

-- Sample items
INSERT INTO items (code, name, category, price, stock, min_stock, description) VALUES
('BOOK001', 'Mathematics Grade 10', 'TEXTBOOK', 25.99, 50, 10, 'Grade 10 Mathematics textbook following national curriculum'),
('BOOK002', 'English Literature Grade 11', 'TEXTBOOK', 22.50, 30, 5, 'English Literature textbook for Grade 11 students'),
('BOOK003', 'Science Grade 9', 'TEXTBOOK', 28.75, 8, 10, 'Comprehensive Science textbook for Grade 9'),
('BOOK004', 'History of Sri Lanka', 'TEXTBOOK', 32.00, 25, 8, 'Complete history of Sri Lanka for secondary education'),
('REF001', 'Oxford English Dictionary', 'REFERENCE', 45.00, 15, 5, 'Comprehensive English dictionary'),
('REF002', 'Mathematical Tables', 'REFERENCE', 18.50, 20, 10, 'Mathematical reference tables and formulas'),
('REF003', 'Atlas of the World', 'REFERENCE', 35.75, 12, 6, 'Detailed world atlas with maps and geographical information'),
('STAT001', 'Blue Pen Pack (10 pcs)', 'STATIONERY', 3.99, 100, 20, 'Pack of 10 blue ballpoint pens'),
('STAT002', 'A4 Notebook', 'STATIONERY', 2.50, 75, 25, 'A4 size ruled notebook, 200 pages'),
('STAT003', 'Pencil Set', 'STATIONERY', 5.25, 60, 15, 'Set of 12 HB pencils with eraser'),
('STAT004', 'Geometry Set', 'STATIONERY', 8.75, 40, 10, 'Complete geometry set with compass, protractor, and rulers'),
('DIG001', 'Educational Software License', 'DIGITAL', 15.00, 50, 10, 'Annual license for educational software suite'),
('DIG002', 'E-book Collection', 'DIGITAL', 25.00, 100, 20, 'Digital collection of educational e-books');

-- Sample bills
INSERT INTO bills (customer_id, customer_name, customer_account_number, bill_date, subtotal, tax_amount, total_amount, status) VALUES
(1, 'John Doe', 'CUST001', '2024-08-15', 51.48, 5.15, 56.63, 'FINALIZED'),
(2, 'Jane Smith', 'CUST002', '2024-08-16', 45.25, 4.53, 49.78, 'FINALIZED'),
(3, 'Michael Johnson', 'CUST003', '2024-08-17', 73.50, 7.35, 80.85, 'FINALIZED');

-- Sample bill items
INSERT INTO bill_items (bill_id, item_id, item_code, item_name, unit_price, quantity, total) VALUES
-- Bill 1 items
(1, 1, 'BOOK001', 'Mathematics Grade 10', 25.99, 1, 25.99),
(1, 2, 'BOOK002', 'English Literature Grade 11', 22.50, 1, 22.50),
(1, 8, 'STAT001', 'Blue Pen Pack (10 pcs)', 3.99, 1, 3.99),

-- Bill 2 items
(2, 5, 'REF001', 'Oxford English Dictionary', 45.00, 1, 45.00),
(3, 9, 'STAT002', 'A4 Notebook', 2.50, 1, 2.50),

-- Bill 3 items
(3, 4, 'BOOK004', 'History of Sri Lanka', 32.00, 1, 32.00),
(3, 6, 'REF002', 'Mathematical Tables', 18.50, 1, 18.50),
(3, 7, 'REF003', 'Atlas of the World', 35.75, 1, 35.75),
(3, 11, 'STAT004', 'Geometry Set', 8.75, 1, 8.75);

-- Create views for reporting

-- Customer summary view
CREATE VIEW customer_summary AS
SELECT 
    c.id,
    c.account_number,
    c.name,
    c.phone,
    c.registration_date,
    COUNT(b.id) as total_bills,
    COALESCE(SUM(b.total_amount), 0) as total_purchases,
    MAX(b.bill_date) as last_purchase_date
FROM customers c
LEFT JOIN bills b ON c.id = b.customer_id AND b.status = 'FINALIZED'
GROUP BY c.id, c.account_number, c.name, c.phone, c.registration_date;

-- Item summary view
CREATE VIEW item_summary AS
SELECT 
    i.id,
    i.code,
    i.name,
    i.category,
    i.price,
    i.stock,
    i.min_stock,
    CASE 
        WHEN i.stock = 0 THEN 'OUT_OF_STOCK'
        WHEN i.stock <= i.min_stock THEN 'LOW_STOCK'
        ELSE 'IN_STOCK'
    END as status,
    (i.price * i.stock) as total_value,
    COALESCE(SUM(bi.quantity), 0) as total_sold
FROM items i
LEFT JOIN bill_items bi ON i.id = bi.item_id
LEFT JOIN bills b ON bi.bill_id = b.id AND b.status = 'FINALIZED'
GROUP BY i.id, i.code, i.name, i.category, i.price, i.stock, i.min_stock;

-- Sales summary view
CREATE VIEW sales_summary AS
SELECT 
    DATE(b.bill_date) as sale_date,
    COUNT(b.id) as total_bills,
    SUM(b.subtotal) as total_subtotal,
    SUM(b.tax_amount) as total_tax,
    SUM(b.total_amount) as total_amount,
    AVG(b.total_amount) as average_bill_amount
FROM bills b
WHERE b.status = 'FINALIZED'
GROUP BY DATE(b.bill_date)
ORDER BY sale_date DESC;

-- Create stored procedures for common operations

DELIMITER //

-- Procedure to update item stock
CREATE PROCEDURE UpdateItemStock(
    IN p_item_id BIGINT,
    IN p_quantity_change INT
)
BEGIN
    DECLARE current_stock INT;
    DECLARE new_stock INT;
    
    -- Get current stock
    SELECT stock INTO current_stock FROM items WHERE id = p_item_id;
    
    -- Calculate new stock
    SET new_stock = current_stock + p_quantity_change;
    
    -- Ensure stock doesn't go negative
    IF new_stock < 0 THEN
        SET new_stock = 0;
    END IF;
    
    -- Update the stock
    UPDATE items SET stock = new_stock WHERE id = p_item_id;
END //

-- Procedure to finalize a bill
CREATE PROCEDURE FinalizeBill(
    IN p_bill_id BIGINT
)
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE v_item_id BIGINT;
    DECLARE v_quantity INT;
    
    -- Cursor to get all items in the bill
    DECLARE item_cursor CURSOR FOR
        SELECT item_id, quantity FROM bill_items WHERE bill_id = p_bill_id;
    
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    START TRANSACTION;
    
    -- Update bill status
    UPDATE bills SET status = 'FINALIZED' WHERE id = p_bill_id;
    
    -- Reduce stock for each item
    OPEN item_cursor;
    read_loop: LOOP
        FETCH item_cursor INTO v_item_id, v_quantity;
        IF done THEN
            LEAVE read_loop;
        END IF;
        
        CALL UpdateItemStock(v_item_id, -v_quantity);
    END LOOP;
    CLOSE item_cursor;
    
    COMMIT;
END //

DELIMITER ;

-- Create indexes for better performance
CREATE INDEX idx_bills_customer_date ON bills(customer_id, bill_date);
CREATE INDEX idx_bill_items_bill_item ON bill_items(bill_id, item_id);
CREATE INDEX idx_items_category_stock ON items(category, stock);

-- Grant permissions (adjust as needed for your setup)
-- GRANT SELECT, INSERT, UPDATE, DELETE ON pahana_edu_bookshop.* TO 'bookshop_user'@'localhost';

-- Display table information
SHOW TABLES;

-- Display sample data counts
SELECT 'Customers' as table_name, COUNT(*) as record_count FROM customers
UNION ALL
SELECT 'Items' as table_name, COUNT(*) as record_count FROM items
UNION ALL
SELECT 'Bills' as table_name, COUNT(*) as record_count FROM bills
UNION ALL
SELECT 'Bill Items' as table_name, COUNT(*) as record_count FROM bill_items;
