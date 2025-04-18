# Account Management System

A Spring Boot application that manages parent and child accounts with proper authentication and authorization.

## Features

- Parent and Child Account Management
- Secure Authentication using JWT
- Audit Trail for all operations
- Role-based Access Control
- RESTful API endpoints
- H2 Database for development

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- MySQL 8.0 or higher

## Setup Instructions

1. Clone the repository
2. Navigate to the project directory
3. Create a MySQL database named `account_management` (or let the application create it automatically)
4. Update the database credentials in `src/main/resources/application.properties` if needed:
   ```properties
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```
5. Run the following command to build the project:
   ```bash
   mvn clean install
   ```
6. Run the application:
   ```bash
   mvn spring-boot:run
   ```

## API Endpoints

### Parent Account
- POST /api/parent/register - Register a new parent account
- POST /api/parent/login - Login for parent account
- GET /api/parent/{id} - Get parent account details
- GET /api/parent/{id}/children - Get all child accounts
- PUT /api/parent/{id} - Update parent account

### Child Account
- POST /api/child/register - Register a new child account
- POST /api/child/login - Login for child account
- GET /api/child/{id} - Get child account details
- PUT /api/child/{id} - Update child account

## Security

- JWT-based authentication
- Password encryption using BCrypt
- Role-based authorization (PARENT/CHILD)
- Session management

## Database

The application uses MySQL database. Make sure you have MySQL installed and running on your system.

Default configuration:
- Database URL: jdbc:mysql://localhost:3306/account_management
- Username: root
- Password: root

You can modify these settings in the `application.properties` file.

## Audit Information

The system maintains the following audit information for all entities:
- Creation date and time
- Last modification date and time
- Created by
- Modified by
- System that generated the record
- Version number for optimistic locking
- Status

## Frontend Integration

The API is designed to be easily integrated with any frontend framework. The endpoints follow RESTful conventions and return JSON responses.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request 