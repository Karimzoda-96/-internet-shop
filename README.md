# Azhar Shop Backend

Production-ready MVP backend for an online shop. The API supports public catalog browsing, guest checkout, admin JWT authorization, and admin management for categories, products, orders and banners.

## Stack

- Java 21
- Spring Boot 3.5
- Spring Web
- Spring Data JPA
- Spring Security
- JWT
- PostgreSQL
- Maven
- Lombok
- Bean Validation
- springdoc OpenAPI / Swagger UI
- Docker and docker-compose

## Modules

- `auth`: admin login and current user endpoint
- `categories`: public active categories and admin CRUD
- `products`: public catalog with pagination, sorting, search and filters; admin CRUD and product image URLs
- `orders`: guest order creation and admin order management
- `banners`: public active banners and admin management
- `dashboard`: admin metrics and latest orders

## Environment Variables

Copy `.env.example` to `.env` for Docker runs and adjust values:

```env
DB_URL=jdbc:postgresql://postgres:5432/azhar
DB_USERNAME=azhar
DB_PASSWORD=azhar
JWT_SECRET=change-this-secret-to-a-long-random-value-at-least-32-characters
JWT_EXPIRATION=86400000
SPRING_PROFILES_ACTIVE=prod
JPA_DDL_AUTO=update
SERVER_PORT=8080
```

For local non-Docker runs, use:

```env
DB_URL=jdbc:postgresql://localhost:5432/azhar
DB_USERNAME=azhar
DB_PASSWORD=azhar
JWT_SECRET=change-this-secret-to-a-long-random-value-at-least-32-characters
JWT_EXPIRATION=86400000
SPRING_PROFILES_ACTIVE=local
JPA_DDL_AUTO=update
```

## Local Run

Start PostgreSQL locally and create the database/user matching your env variables, then run:

```bash
./mvnw spring-boot:run
```

The API starts at:

```text
http://localhost:8080
```

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

## Docker Compose

```bash
cp .env.example .env
docker compose up --build
```

This starts:

- backend on `http://localhost:8080`
- PostgreSQL on `localhost:5432`

## Seed Data

Seed data is inserted only when missing and is safe to run repeatedly.

Admin user:

```text
email: admin@example.com
password: Admin123!
```

Seed also includes 4 categories, 12 products and 3 banners.

## Auth

Login:

```http
POST /api/auth/login
```

Body:

```json
{
  "email": "admin@example.com",
  "password": "Admin123!"
}
```

Use the returned token for admin endpoints:

```http
Authorization: Bearer <accessToken>
```

`/api/admin/**` requires `ADMIN`. Public category, product, banner and order creation endpoints do not require a token.

## Main Endpoints

- `POST /api/auth/login`
- `GET /api/auth/me`
- `GET /api/categories`
- `POST /api/admin/categories`
- `GET /api/products?page=0&size=20&search=phone&sort=price,asc`
- `POST /api/admin/products`
- `POST /api/admin/products/{id}/images`
- `POST /api/orders`
- `GET /api/admin/orders`
- `PUT /api/admin/orders/{id}/status`
- `GET /api/banners`
- `POST /api/admin/banners`
- `GET /api/admin/dashboard`

## Verification

Run tests:

```bash
./mvnw test
```

Build:

```bash
./mvnw clean package
```

Run through Docker:

```bash
docker compose up --build
```

Readiness criteria:

1. Maven build completes without errors.
2. The application starts and connects to PostgreSQL.
3. Swagger UI opens at `http://localhost:8080/swagger-ui.html`.
4. `POST /api/auth/login` returns a JWT for the seed admin.
5. `/api/admin/**` rejects requests without JWT and accepts valid admin JWT.
6. Public catalog and banner endpoints are accessible without JWT.
7. `POST /api/orders` creates an order and calculates totals on the backend.
8. Admin category, product, order and banner endpoints work through Swagger or curl.
9. Seed data is not duplicated after restart.
