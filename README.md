# TicketFlow - Full Stack Ticketing System

A comprehensive IT support ticketing system built with Spring Boot and Next.js.

## 🎯 Features

### Must-Have Features ✅
- ✅ Authentication & Authorization (JWT-based)
- ✅ Role-based access control (User, Admin, Support Agent)
- ✅ User Dashboard (raise tickets, view status, add comments)
- ✅ Ticket Management (lifecycle: Open → In Progress → Resolved → Closed)
- ✅ Admin Panel (user management, ticket oversight)
- ✅ Access Control (role-based permissions)

### Good-to-Have Features 🌟
- ✅ Email Notifications
- ✅ Search & Filter tickets
- ✅ Ticket Prioritization (Low, Medium, High, Urgent)
- ✅ File Attachments
- ✅ Rate Ticket Resolution (1-5 stars with feedback)

## 🛠️ Tech Stack

### Backend
- **Java 17**
- **Spring Boot 3.2.0**
  - Spring Security (JWT Authentication)
  - Spring Data JPA
  - Spring Mail
- **PostgreSQL** (Database)
- **Maven** (Build tool)

### Frontend
- **Next.js 14** (React framework)
- **TypeScript**
- **Tailwind CSS** (Styling)
- **Zustand** (State management)
- **React Hook Form** (Form handling)
- **Axios** (HTTP client)

## 📋 Prerequisites

- **Java 17** or higher
- **Node.js 18** or higher
- **PostgreSQL 12** or higher
- **Maven 3.6+**
- **npm** or **yarn**

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/rahulsags/TicketFlow.git
cd TicketFlow
```

### 2. Database Setup

Create a PostgreSQL database:

```sql
CREATE DATABASE ticketflow;
```

### 3. Backend Setup

Navigate to the backend directory:

```bash
cd backend
```

Update `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ticketflow
    username: your_postgres_username
    password: your_postgres_password
```

**Generate a secure JWT secret:**

You can use an online tool or run:
```bash
node -e "console.log(require('crypto').randomBytes(32).toString('base64'))"
```

Update the JWT secret in `application.yml`:

```yaml
jwt:
  secret: your-generated-secret-here
```

**Configure Email (Optional):**

For email notifications, update SMTP settings in `application.yml`:

```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: your-email@gmail.com
    password: your-app-password
```

**Build and run the backend:**

```bash
# Build
mvn clean install

# Run
mvn spring-boot:run
```

Backend will start on `http://localhost:8080`

### 4. Frontend Setup

Navigate to the frontend directory:

```bash
cd ../frontend
```

Install dependencies:

```bash
npm install
# or
yarn install
```

Create `.env.local` file (already created with default values):

```env
NEXT_PUBLIC_API_URL=http://localhost:8080/api
```

Run the development server:

```bash
npm run dev
# or
yarn dev
```

Frontend will start on `http://localhost:3000`

## 👥 Default User Accounts

The application creates default users on first run:

| Role | Username | Password | Email |
|------|----------|----------|-------|
| Admin | admin | admin123 | admin@ticketflow.com |
| Support Agent | agent | agent123 | agent@ticketflow.com |
| User | user | user123 | user@ticketflow.com |

## 📱 Application Structure

### Backend Structure

```
backend/
├── src/main/java/com/ticketflow/
│   ├── config/          # Configuration classes
│   ├── controller/      # REST controllers
│   ├── dto/             # Data Transfer Objects
│   ├── exception/       # Exception handling
│   ├── model/           # JPA entities
│   ├── repository/      # Data repositories
│   ├── security/        # Security configuration
│   └── service/         # Business logic
└── src/main/resources/
    └── application.yml  # Application configuration
```

### Frontend Structure

```
frontend/
├── src/
│   ├── app/            # Next.js app directory
│   │   ├── dashboard/  # Dashboard pages
│   │   ├── admin/      # Admin pages
│   │   ├── login/      # Auth pages
│   │   └── layout.tsx  # Root layout
│   ├── components/     # Reusable components
│   ├── services/       # API services
│   ├── store/          # State management
│   ├── types/          # TypeScript types
│   └── lib/            # Utilities
└── public/             # Static assets
```

## 🔑 Key Features Explained

### Role-Based Access Control

- **User**: Can create and manage their own tickets
- **Support Agent**: Can view all tickets, update status, and be assigned tickets
- **Admin**: Full access to all features including user management

### Ticket Lifecycle

1. **OPEN** - Ticket created, awaiting assignment
2. **IN_PROGRESS** - Ticket assigned and being worked on
3. **RESOLVED** - Issue fixed, awaiting user confirmation
4. **CLOSED** - Ticket completed and closed

### Priority Levels

- **LOW** - Minor issues, low impact
- **MEDIUM** - Standard priority
- **HIGH** - Important issues requiring attention
- **URGENT** - Critical issues requiring immediate action

## 🧪 API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login and get JWT token

### Tickets
- `POST /api/tickets` - Create new ticket
- `GET /api/tickets/my-tickets` - Get user's tickets
- `GET /api/tickets/assigned` - Get assigned tickets (Agent/Admin)
- `GET /api/tickets/all` - Get all tickets (Agent/Admin)
- `GET /api/tickets/{id}` - Get ticket details
- `PATCH /api/tickets/{id}/status` - Update ticket status
- `PATCH /api/tickets/{id}/assign` - Assign ticket
- `POST /api/tickets/{id}/comments` - Add comment
- `GET /api/tickets/{id}/comments` - Get ticket comments
- `POST /api/tickets/{id}/rate` - Rate ticket resolution
- `GET /api/tickets/search` - Search tickets

### Files
- `POST /api/files/upload` - Upload attachment
- `GET /api/files/ticket/{ticketId}` - Get ticket attachments
- `GET /api/files/download/{attachmentId}` - Download file

### Admin
- `GET /api/admin/users` - Get all users
- `POST /api/admin/users` - Create user
- `PATCH /api/admin/users/{id}/role` - Update user role
- `DELETE /api/admin/users/{id}` - Delete user
- `PATCH /api/admin/users/{id}/toggle-status` - Enable/disable user

## 🔒 Security

- JWT-based authentication
- Password encryption with BCrypt
- Role-based authorization
- CORS configuration for frontend
- Secure file upload/download

## 📊 Database Schema

Main entities:
- **Users** - User accounts with roles
- **Tickets** - Support tickets
- **Comments** - Ticket comments
- **Attachments** - File attachments
- **Ratings** - Ticket resolution ratings

## 🎨 UI Features

- Responsive design (mobile-friendly)
- Modern, clean interface with Tailwind CSS
- Real-time notifications (toast messages)
- File upload with drag-and-drop
- Advanced search and filtering
- Priority color coding
- Status badges

## 🐛 Troubleshooting

### Backend Issues

**Database connection failed:**
- Verify PostgreSQL is running
- Check database credentials in `application.yml`
- Ensure database `ticketflow` exists

**Port 8080 already in use:**
- Change port in `application.yml`: `server.port: 8081`
- Update frontend `.env.local` accordingly

### Frontend Issues

**Module not found errors:**
```bash
rm -rf node_modules package-lock.json
npm install
```

**API connection failed:**
- Verify backend is running on port 8080
- Check CORS configuration in backend
- Verify `.env.local` has correct API URL

## 📝 Development Tips

### Adding New Features

1. **Backend**: Create entity → repository → service → controller
2. **Frontend**: Create service → component → page
3. Test with different user roles
4. Update README with new endpoints/features

### Testing Users

Use the default accounts to test different scenarios:
- Login as **admin** to test user management
- Login as **agent** to test ticket assignment
- Login as **user** to test ticket creation

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📄 License

This project is for educational purposes.

## 👨‍💻 Author

**Rahul**
- GitHub: [@rahulsags](https://github.com/rahulsags)

## 🙏 Acknowledgments

- Spring Boot Documentation
- Next.js Documentation
- Tailwind CSS
- All open-source contributors

---

**Happy Coding! 🚀**