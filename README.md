# Pahana Edu Bookshop Management System

A simple web-based bookshop management system built with JavaEE backend and modern frontend technologies.

## Project Overview

Pahana Edu is a leading bookshop in Colombo City, serving hundreds of customers each month. This system replaces manual customer account management with a computerized online solution.

## Technology Stack

### Backend
- **JavaEE 8** - Enterprise Java platform
- **MySQL 8** - Database management system
- **Maven** - Build and dependency management

### Frontend
- **HTML5 & CSS3** - Markup and styling
- **Bootstrap 5** - Responsive UI framework
- **JavaScript (ES6+)** - Client-side logic
- **Single Page Application (SPA)** - Architecture pattern

## Architecture

### Backend - Simple Layered Architecture
```
src/main/java/com/icbt/cis6003/pahanaedubookshop/
├── controller/     # Simple servlet controllers
├── service/        # Business logic interfaces
├── dao/           # Data access interfaces
├── model/         # Entity models
└── util/          # Simple utility classes
```

### Frontend - SPA Architecture
```
web/
├── index.html          # Main SPA file
├── controller/         # JavaScript controllers
│   ├── app-controller.js
│   ├── customer-controller.js
│   ├── item-controller.js
│   ├── billing-controller.js
│   └── report-controller.js
└── WEB-INF/           # Simple web configuration
```

## Core Features

### 1. Customer Management
- Add new customer accounts
- Edit customer information
- View customer details
- Search customers by name, account number, or phone
- Customer statistics and reporting

### 2. Item/Inventory Management
- Add, update, delete book/item information
- Category-based organization (Textbooks, Reference, Stationery, Digital)
- Stock level monitoring
- Low stock alerts
- Inventory valuation

### 3. Billing System
- Create customer bills
- Add/remove items from bills
- Automatic tax calculation (10%)
- Bill finalization and printing
- Sales tracking and reporting

### 4. Reporting & Analytics
- Customer reports
- Inventory reports
- Sales reports
- Dashboard with key metrics
- Export functionality

### 5. Help & Documentation
- User guidance system
- System usage guidelines
- FAQ section

## Database Schema

### Tables
- `customers` - Customer account information
- `items` - Inventory items and books
- `bills` - Customer bills/invoices
- `bill_items` - Items in each bill

## Setup Instructions

### Prerequisites
- Java 8 or higher
- Apache Tomcat 9.0+
- MySQL 8.0+
- Maven 3.6+

### Database Setup
1. Create MySQL database:
```sql
CREATE DATABASE pahana_edu_bookshop;
```

2. Update database configuration in `DatabaseConnection.java`:
```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/pahana_edu_bookshop";
private static final String DB_USERNAME = "your_username";
private static final String DB_PASSWORD = "your_password";
```

3. Run the database schema script:
```sql
-- Execute the contents of src/main/resources/database-schema.sql
```

### Build and Deploy
1. Clone the repository
2. Build the project:
```bash
mvn clean compile
```

3. Package the WAR file:
```bash
mvn package
```

4. Deploy to Tomcat:
   - Copy `target/pahana-edu-bookshop.war` to Tomcat's `webapps` directory
   - Start Tomcat server

### Access the Application
- Open browser and navigate to: `http://localhost:8080/pahana-edu-bookshop`

## API Endpoints

### Customer API
- `GET /api/customers` - Get all customers
- `GET /api/customers/{id}` - Get customer by ID
- `POST /api/customers` - Create new customer
- `PUT /api/customers/{id}` - Update customer
- `DELETE /api/customers/{id}` - Delete customer
- `GET /api/customers?search={term}` - Search customers

### Item API
- `GET /api/items` - Get all items
- `GET /api/items/{id}` - Get item by ID
- `POST /api/items` - Create new item
- `PUT /api/items/{id}` - Update item
- `DELETE /api/items/{id}` - Delete item
- `GET /api/items/low-stock` - Get low stock items

### Billing API
- `GET /api/bills` - Get all bills
- `GET /api/bills/{id}` - Get bill by ID
- `POST /api/bills` - Create new bill
- `POST /api/bills/{id}/items` - Add item to bill
- `POST /api/bills/{id}/finalize` - Finalize bill

## Development Guidelines

### Code Structure
- Follow simple layered architecture principles
- Use basic error handling with simple messages
- Keep code simple and easy to understand

### Frontend Development
- Use modular JavaScript with separate controller files
- Follow SPA patterns for navigation
- Implement responsive design with Bootstrap
- Use semantic HTML and accessible UI components

### Database Operations
- Use simple database connections
- Follow basic database naming conventions
- Use the provided schema for table structure

## Testing

### Unit Testing
```bash
mvn test
```

### Integration Testing
- Test API endpoints with tools like Postman
- Verify database operations
- Test frontend functionality in multiple browsers

## Deployment

### Simple Deployment
1. Update database configuration in DatabaseConnection.java
2. Build and deploy WAR file to Tomcat
3. Access the application via web browser

## Contributing

1. Follow Java coding standards
2. Write comprehensive documentation
3. Include unit tests for new features
4. Use meaningful commit messages
5. Create pull requests for code review

## License

This project is developed for educational purposes as part of CIS 6003 coursework.

## Support

For technical support or questions:
- Email: admin@pahanaedu.com
- Documentation: See Help section in the application

## Version History

- **v1.0.0** - Initial release with core functionality
- **v1.1.0** - Enhanced reporting features (planned)
- **v1.2.0** - Mobile responsive improvements (planned)

## Acknowledgments

- ICBT Campus for project requirements
- Bootstrap team for UI framework
- MySQL community for database platform
- Apache Software Foundation for Tomcat server
