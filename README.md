# ExpenseWise Backend (Spring Boot)

This is the backend REST API for ExpenseWise, a simple personal expense tracking application.  
The backend is built with Spring Boot, JPA/Hibernate, and PostgreSQL, and exposes clean CRUD endpoints for Users, Categories, and Expenses.

---

## Tech Stack

- **Java 17**
- **Spring Boot 3**
- **Spring Web**
- **Spring Data JPA**
- **PostgreSQL**
- **Flyway** (database migration)
- **Lombok**
- **Gradle**

---

## Project Structure

- controller/ (REST controllers)
- service/    (Service layer) (business logic)
- repository/ (JPA repositories)
- entity/     (JPA entities)
- ExpensewiseApplication.java
- application.properties   (DB config)

## Database Schema

### User
- `id` (PK)
- `username`
- `email`

### Category
- `id` (PK)
- `name`

### Expense
- `id` (PK)
- `amount`
- `date`
- `description`
- `username`
- `category_id` (FK → Category)
- `user_id` (FK → User)

## API Endpoints

### Users
| Method | Endpoint          | Description |
|--------|-------------------|-------------|
| GET    | `/api/users`      | Get all users |
| GET    | `/api/users/{id}` | Get user by ID |
| POST   | `/api/users`      | Create new user |
| PUT    | `/api/users/{id}` | Update user |
| DELETE | `/api/users/{id}` | Delete user |

---

### Categories
| Method | Endpoint                  | Description |
|--------|----------------------------|-------------|
| GET    | `/api/categories`          | Get all categories |
| GET    | `/api/categories/{id}`     | Get a category |
| POST   | `/api/categories`          | Create category |
| PUT    | `/api/categories/{id}`     | Update category |
| DELETE | `/api/categories/{id}`     | Delete category |

---

### Expenses
| Method | Endpoint                  | Description |
|--------|----------------------------|-------------|
| GET    | `/api/expenses`            | Get all expenses |
| GET    | `/api/expenses/{id}`       | Get expense by ID |
| POST   | `/api/expenses`            | Create expense |
| PUT    | `/api/expenses/{id}`       | Update expense |
| DELETE | `/api/expenses/{id}`       | Delete expense |

---

## Running the Backend

### 1. Set environment variables  
Update your PostgreSQL connection:

```

spring.datasource.url=jdbc:postgresql://localhost:5432/expensewise
spring.datasource.username=postgres
spring.datasource.password=alice123

```

### 2. Run the project

```

./gradlew bootRun

````

Swagger or Postman can be used to test all endpoints.

---

## Sample Test Data (JSON)

### Create a User
```json
{
  "username": "alice",
  "email": "alice@example.com"
}
````

### Create a Category

```json
{
  "name": "Food"
}
```

### Create an Expense

```json
{
  "amount": 18.50,
  "description": "Lunch",
  "date": "2025-01-05",
  "username": "alice",
  "categoryId": 1,
  "userId": 1
}
```
