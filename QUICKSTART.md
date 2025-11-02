# Quick Start Guide for TicketFlow

## Prerequisites Check

Before starting, ensure you have:
- [ ] Java 17+ installed (`java -version`)
- [ ] Node.js 18+ installed (`node -v`)
- [ ] PostgreSQL 12+ installed and running
- [ ] Maven 3.6+ installed (`mvn -v`)
- [ ] Git installed

## Setup Steps

### Step 1: Database Setup (5 minutes)

1. Open PostgreSQL command line or pgAdmin
2. Create database:
```sql
CREATE DATABASE ticketflow;
```
3. Verify creation:
```sql
\l  -- or LIST DATABASES in pgAdmin
```

### Step 2: Backend Setup (10 minutes)

1. Navigate to backend folder:
```bash
cd backend
```

2. Update database credentials in `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    username: YOUR_POSTGRES_USERNAME
    password: YOUR_POSTGRES_PASSWORD
```

3. Generate JWT secret (run in terminal):
```bash
node -e "console.log(require('crypto').randomBytes(32).toString('base64'))"
```

4. Update JWT secret in `application.yml`:
```yaml
jwt:
  secret: YOUR_GENERATED_SECRET
```

5. Build and run:
```bash
mvn clean install
mvn spring-boot:run
```

6. Verify backend is running:
- Open browser: http://localhost:8080
- Should see "Whitelabel Error Page" (normal for root endpoint)

### Step 3: Frontend Setup (5 minutes)

1. Open new terminal, navigate to frontend:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Verify `.env.local` exists with:
```
NEXT_PUBLIC_API_URL=http://localhost:8080/api
```

4. Start development server:
```bash
npm run dev
```

5. Open browser: http://localhost:3000

### Step 4: Test the Application (5 minutes)

1. **Login as User**
   - Username: `user`
   - Password: `user123`
   - Create a ticket
   - Add comments

2. **Login as Support Agent**
   - Username: `agent`
   - Password: `agent123`
   - View all tickets
   - Assign ticket to yourself
   - Update ticket status

3. **Login as Admin**
   - Username: `admin`
   - Password: `admin123`
   - Access admin panel
   - View users
   - Create new user

## Common Issues and Solutions

### Issue: Database connection refused
**Solution**: 
- Check if PostgreSQL is running
- Verify port 5432 is not blocked
- Check credentials in application.yml

### Issue: Port 8080 already in use
**Solution**:
- Kill process using port: `netstat -ano | findstr :8080` (Windows)
- Or change port in application.yml

### Issue: Frontend can't connect to backend
**Solution**:
- Verify backend is running on port 8080
- Check .env.local has correct API URL
- Clear browser cache

### Issue: npm install fails
**Solution**:
```bash
rm -rf node_modules package-lock.json
npm cache clean --force
npm install
```

## Quick Commands Reference

### Backend
```bash
# Build
mvn clean install

# Run
mvn spring-boot:run

# Run on different port
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081

# Skip tests
mvn clean install -DskipTests
```

### Frontend
```bash
# Install dependencies
npm install

# Development mode
npm run dev

# Build for production
npm run build

# Run production build
npm start

# Lint code
npm run lint
```

### Database
```sql
-- Create database
CREATE DATABASE ticketflow;

-- Connect to database
\c ticketflow

-- List tables
\dt

-- Drop database (careful!)
DROP DATABASE ticketflow;
```

## Project Structure at a Glance

```
TicketingFlow/
├── backend/                  # Spring Boot application
│   ├── src/main/java/       # Java source code
│   ├── src/main/resources/  # Configuration files
│   └── pom.xml              # Maven dependencies
│
├── frontend/                # Next.js application
│   ├── src/                 # Source code
│   ├── public/              # Static files
│   └── package.json         # npm dependencies
│
└── README.md                # Main documentation
```

## Default Credentials

| Role          | Username | Password  |
|---------------|----------|-----------|
| Admin         | admin    | admin123  |
| Support Agent | agent    | agent123  |
| User          | user     | user123   |

## Verification Checklist

After setup, verify:
- [ ] Backend running on http://localhost:8080
- [ ] Frontend running on http://localhost:3000
- [ ] Can login with default credentials
- [ ] Can create a ticket
- [ ] Can view tickets
- [ ] Can add comments
- [ ] Email notifications working (if configured)

## Next Steps

1. **Customize Configuration**
   - Update email settings for notifications
   - Change default passwords
   - Configure file upload size limits

2. **Explore Features**
   - Create tickets with different priorities
   - Test file attachments
   - Try search and filtering
   - Rate resolved tickets

3. **Development**
   - Read API documentation
   - Explore code structure
   - Add custom features
   - Deploy to production

## Getting Help

- Check README.md for detailed documentation
- Review PROJECT_SUMMARY.md for architecture details
- Check backend/README.md for backend-specific info
- Check frontend/README.md for frontend-specific info

## Production Deployment Notes

Before deploying to production:
1. Change all default passwords
2. Use environment-specific configurations
3. Set up proper database backups
4. Configure SSL/TLS certificates
5. Set up monitoring and logging
6. Use production-grade email service
7. Configure file storage (S3, etc.)
8. Set up reverse proxy (nginx)
9. Enable rate limiting
10. Configure proper CORS settings

---

**You're all set! Happy coding! 🚀**

For issues or questions, check the troubleshooting section in README.md
