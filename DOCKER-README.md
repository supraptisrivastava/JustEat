# JustEat - Docker Setup

## Prerequisites

- Docker Desktop installed and running
- Docker Compose V2

## Quick Start

### 1. Clone and navigate to project
```bash
cd just_eat
```

### 2. Create environment file (optional but recommended)
```bash
cp .env.example .env
# Edit .env with your actual credentials
```

### 3. Build and start all services
```bash
docker-compose up --build
```

### 4. Access the application
- **Frontend:** http://localhost
- **Backend API:** http://localhost:8090
- **Swagger UI:** http://localhost:8090/swagger-ui.html

## Docker Commands

### Start services (detached mode)
```bash
docker-compose up -d
```

### Stop services
```bash
docker-compose down
```

### Stop services and remove volumes (clears database)
```bash
docker-compose down -v
```

### View logs
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f postgres
```

### Rebuild specific service
```bash
docker-compose build backend
docker-compose build frontend
```

### Restart specific service
```bash
docker-compose restart backend
```

## Services

| Service | Port | Description |
|---------|------|-------------|
| frontend | 80 | React app served via Nginx |
| backend | 8090 | Spring Boot REST API |
| postgres | 5432 | PostgreSQL database |

## Environment Variables

Create a `.env` file in the root directory:

```env
# Database
POSTGRES_PASSWORD=your_secure_password

# Cloudinary
CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_api_key
CLOUDINARY_API_SECRET=your_api_secret

# Mail
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password
```

## Development vs Production

### Development (with hot reload)
For development, you may want to run services locally:
```bash
# Only run database in Docker
docker-compose up postgres -d

# Run backend locally
cd JustEat/Backend
./mvnw spring-boot:run

# Run frontend locally
cd JustEat/Frontend/JustEat_frontend
npm run dev
```

### Production
For production, use the full docker-compose setup and consider:
1. Using proper secrets management
2. Setting up SSL/TLS certificates
3. Using a reverse proxy
4. Adding proper logging and monitoring

## Troubleshooting

### Backend won't connect to database
Wait for PostgreSQL to be healthy. Check logs:
```bash
docker-compose logs postgres
```

### Frontend can't reach backend
Ensure backend is running and healthy:
```bash
docker-compose ps
curl http://localhost:8090/actuator/health
```

### Clear everything and start fresh
```bash
docker-compose down -v
docker system prune -f
docker-compose up --build
```

## Architecture

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   Frontend  │────▶│   Backend   │────▶│  PostgreSQL │
│   (Nginx)   │     │(Spring Boot)│     │             │
│   Port 80   │     │  Port 8090  │     │  Port 5432  │
└─────────────┘     └─────────────┘     └─────────────┘
```

