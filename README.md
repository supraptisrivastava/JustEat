# JustEat - Food Ordering Application

A full-stack food ordering platform built with React and Spring Boot, allowing customers to browse restaurants, place orders, and restaurant owners to manage their menus and orders.

## 🚀 Features

### Customer Features
- 🔐 User authentication (Register/Login)
- 🔑 Password reset via email
- 🔍 Search restaurants by name, cuisine, and location
- 📋 Browse restaurant menus with prices and descriptions
- 🛒 Add items to cart and place orders
- 📦 Track order status (Pending → Preparing → Ready → Completed)
- 📜 View order history and reorder
- ⭐ Personalized recommendations based on preferences
- 💾 Save favorite cuisines and dietary restrictions

### Restaurant Owner Features
- 🏪 Register and manage restaurants
- 📝 Full CRUD operations on menu items
- ⭐ Mark items as "Today's Special"
- 🔥 Auto-flagged "Mostly Ordered" popular items
- 📊 View and manage incoming orders
- 🔄 Update order status

### System Features
- 🔒 JWT-based authentication
- 👮 Role-based access control (Customer/Owner/Admin)
- 📖 Swagger API documentation
- 🐳 Docker support
- ☁️ Cloudinary image uploads

## 🛠️ Tech Stack

### Backend
- **Framework:** Spring Boot 4.0
- **Language:** Java 21
- **Database:** PostgreSQL
- **Security:** Spring Security + JWT
- **ORM:** Spring Data JPA / Hibernate
- **Documentation:** SpringDoc OpenAPI (Swagger)
- **Image Storage:** Cloudinary

### Frontend
- **Framework:** React 19
- **Build Tool:** Vite
- **Styling:** Tailwind CSS 4
- **HTTP Client:** Axios
- **Routing:** React Router v7
- **Notifications:** React Toastify

## 📁 Project Structure

```
just_eat/
├── JustEat/
│   ├── Backend/
│   │   ├── src/main/java/com/example/JustEat/
│   │   │   ├── controller/     # REST Controllers
│   │   │   ├── service/        # Business Logic
│   │   │   ├── repository/     # Data Access
│   │   │   ├── entity/         # JPA Entities
│   │   │   ├── dto/            # Data Transfer Objects
│   │   │   ├── mapper/         # Entity-DTO Mappers
│   │   │   ├── security/       # JWT & Security Config
│   │   │   ├── exception/      # Custom Exceptions
│   │   │   └── enums/          # Enumerations
│   │   └── src/test/           # Unit Tests
│   │
│   └── Frontend/JustEat_frontend/
│       └── src/
│           ├── api/            # Axios API calls
│           ├── auth/           # Auth services
│           ├── components/     # Reusable components
│           ├── context/        # React Context
│           ├── pages/          # Page components
│           └── routes/         # App routing
│
├── docker-compose.yml          # Docker orchestration
├── AI_USAGE.md                 # AI usage documentation
└── README.md                   # This file
```

## 🚀 Getting Started

### Prerequisites
- Java 21+
- Node.js 18+
- PostgreSQL 16+ (or Docker)
- Maven

### Option 1: Run with Docker (Recommended)

```bash
# Clone the repository
cd just_eat

# Start all services
docker-compose up --build

# Access the application
# Frontend: http://localhost
# Backend API: http://localhost:8090
# Swagger: http://localhost:8090/swagger-ui.html
```

### Option 2: Run Locally

#### Backend Setup
```bash
cd JustEat/Backend

# Create PostgreSQL database
createdb justeat

# Run the application
./mvnw spring-boot:run
```

#### Frontend Setup
```bash
cd JustEat/Frontend/JustEat_frontend

# Install dependencies
npm install

# Start development server
npm run dev
```

## 📚 API Documentation

Once the backend is running, access Swagger UI at:
```
http://localhost:8090/swagger-ui.html
```

### Key Endpoints

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/auth/register` | Register new user | No |
| POST | `/auth/login` | Login | No |
| GET | `/restaurants` | Get all restaurants | Yes |
| GET | `/restaurants/search` | Search restaurants | Yes |
| GET | `/restaurants/{id}/menu` | Get menu items | Yes |
| GET | `/restaurants/{id}/menu/popular` | Get popular items | Yes |
| POST | `/cart/add` | Add to cart | Customer |
| POST | `/order/place` | Place order | Customer |
| GET | `/order/history` | Order history | Customer |
| GET | `/order/owner` | Owner's orders | Owner |
| PATCH | `/order/{id}/status` | Update status | Owner |

## 🧪 Running Tests

```bash
cd JustEat/Backend

# Run all tests
./mvnw test

# Run with coverage
./mvnw test jacoco:report
```

## 🔧 Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `SPRING_DATASOURCE_URL` | Database URL | `jdbc:postgresql://localhost:5432/justeat` |
| `SPRING_DATASOURCE_USERNAME` | DB username | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | DB password | `root` |
| `CLOUDINARY_CLOUD_NAME` | Cloudinary name | - |
| `CLOUDINARY_API_KEY` | Cloudinary key | - |
| `CLOUDINARY_API_SECRET` | Cloudinary secret | - |

## 📊 Database Schema

### Core Entities
- **User** - Customer/Owner accounts with preferences
- **Restaurant** - Restaurant details with location & cuisines
- **MenuItem** - Menu items with pricing & availability
- **Cart/CartItem** - Shopping cart functionality
- **Order/OrderItem** - Order management
- **UserPreference** - Favorite cuisines & dietary restrictions

## 👥 User Roles

| Role | Capabilities |
|------|-------------|
| **CUSTOMER** | Browse, search, order, view history |
| **OWNER** | Manage restaurants, menus, orders |
| **ADMIN** | Full system access |

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is developed as part of a training/educational program.

## 📞 Support

For questions or issues, please open a GitHub issue.

---

**Built with ❤️ using Spring Boot & React**

