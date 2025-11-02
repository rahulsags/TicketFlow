# TicketFlow Backend

Spring Boot backend for the TicketFlow application.

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+

## Setup

### 1. Database Setup

Create a PostgreSQL database:

```sql
CREATE DATABASE ticketflow;
```

### 2. Configuration

Update `src/main/resources/application.yml` with your database credentials:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ticketflow
    username: your_username
    password: your_password
```

### 3. JWT Secret

Generate a secure JWT secret and update in `application.yml`:

```yaml
jwt:
  secret: your-secure-secret-key-here
```

### 4. Email Configuration (Optional)

For email notifications, configure your SMTP settings:

```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: your-email@gmail.com
    password: your-app-password
```

## Running the Application

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Default Users

The application creates default users on first run:

- **Admin**: username: `admin`, password: `admin123`
- **Support Agent**: username: `agent`, password: `agent123`
- **User**: username: `user`, password: `user123`

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login

### Tickets
- `POST /api/tickets` - Create ticket
- `GET /api/tickets/my-tickets` - Get my tickets
- `GET /api/tickets/assigned` - Get assigned tickets
- `GET /api/tickets/all` - Get all tickets
- `GET /api/tickets/{id}` - Get ticket by ID
- `PATCH /api/tickets/{id}/status` - Update ticket status
- `PATCH /api/tickets/{id}/assign` - Assign ticket
- `POST /api/tickets/{id}/comments` - Add comment
- `GET /api/tickets/{id}/comments` - Get ticket comments
- `POST /api/tickets/{id}/rate` - Rate ticket
- `GET /api/tickets/search` - Search tickets

### Files
- `POST /api/files/upload` - Upload file
- `GET /api/files/ticket/{ticketId}` - Get ticket attachments
- `GET /api/files/download/{attachmentId}` - Download file

### Admin
- `GET /api/admin/users` - Get all users
- `POST /api/admin/users` - Create user
- `PATCH /api/admin/users/{id}/role` - Update user role
- `DELETE /api/admin/users/{id}` - Delete user
- `PATCH /api/admin/users/{id}/toggle-status` - Toggle user status

## Testing

```bash
mvn test
```
