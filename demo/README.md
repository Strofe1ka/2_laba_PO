# Лабораторная работа №5

**Тема:** Carsharing REST API — Spring Boot + PostgreSQL + JPA + Spring Security

---

# Carsharing — Spring Boot + PostgreSQL + JPA + Spring Security

## Project Topic

Carsharing REST API — a system for managing car rentals, users, rides, and payments. Users can start rides with available cars, end rides (cost calculated by distance × 10), and pay for completed rides.

## Security (Spring Security)

- **Basic Auth** — все защищённые эндпоинты требуют заголовок `Authorization: Basic <base64(username:password)>`
- **CSRF** — настроен `CookieCsrfTokenRepository` (при Basic Auth токен не требуется)
- **Роли**: `USER`, `ADMIN`
- **Регистрация** — `POST /register` (без авторизации). Первый зарегистрированный пользователь получает роль `ADMIN`
- **Пароль** — минимум 8 символов, заглавная, строчная, цифра, спецсимвол

## Main Entities

| Entity   | Description                                      |
|----------|--------------------------------------------------|
| **Car**  | Vehicle with brand, model, plate number, availability |
| **User** | Customer with first name, last name, email, username, password, role |
| **Ride** | Trip linking a user and a car, with start/end time, distance, cost |
| **Payment** | Payment record for a ride, with amount and paid status |

## Relationships

- **Ride** → **User** (`@ManyToOne`): each ride belongs to one user
- **Ride** → **Car** (`@ManyToOne`): each ride uses one car
- **Payment** → **Ride** (`@ManyToOne`): each payment is for one ride

## Constraints

- `Car.plateNumber` — unique
- `User.email` — unique
- IDs — auto-increment (`GenerationType.IDENTITY`)

## Available Endpoints

### Регистрация (без авторизации)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/register` | Регистрация нового пользователя (роль USER; первый — ADMIN) |

### Cars (GET — USER/ADMIN; POST/PUT/DELETE — ADMIN)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/cars` | Create car |
| GET | `/cars` | Get all cars (optional `?available=true`) |
| GET | `/cars/available` | List available cars |
| GET | `/cars/{id}` | Get car by ID |
| PUT | `/cars/{id}` | Update car |
| DELETE | `/cars/{id}` | Delete car |

### Users (только ADMIN)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/users` | Create user (username, password обязательны) |
| GET | `/users` | Get all users |
| GET | `/users/{id}` | Get user by ID |
| GET | `/users/{id}/income` | Get user income (sum of paid rides) |
| PUT | `/users/{id}` | Update user |
| DELETE | `/users/{id}` | Delete user |

### Rides (USER, ADMIN)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/rides` | Start ride |
| GET | `/rides` | Get all rides |
| GET | `/rides/{id}` | Get ride by ID |
| PUT | `/rides/{id}/end` | End ride |
| POST | `/rides/{id}/pay` | Pay for ride |
| DELETE | `/rides/{id}` | Delete ride |

### Payments (USER, ADMIN)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/payments` | Create payment |
| GET | `/payments` | Get all payments |
| GET | `/payments/{id}` | Get payment by ID |
| PUT | `/payments/{id}/pay` | Mark payment as paid |
| DELETE | `/payments/{id}` | Delete payment |

## Business Operations (5)

1. **Start a ride** — Check car availability, create Ride, mark car as occupied
2. **End a ride** — Compute cost (distance × 10), update ride, mark car as available
3. **Pay for a ride** — Create Payment and set paid = true
4. **Get user income** — Sum of all paid rides for a user
5. **List available cars** — Cars where `available = true`

All operations that change multiple entities use `@Transactional`.

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `DB_HOST` | PostgreSQL host | `localhost` |
| `DB_PORT` | PostgreSQL port | `5432` |
| `DB_NAME` | Database name | `carsharing` |
| `DB_USER` | Database user | `postgres` |
| `DB_PASSWORD` | Database password | `postgres` |

## Prerequisites

- Java 17+
- PostgreSQL
- Maven

## Database Setup

1. Скопируйте `src/main/resources/application-local.properties.example` в `application-local.properties` и укажите пароль БД (файл в .gitignore).
2. Start PostgreSQL.
3. Create the database:
```sql
CREATE DATABASE carsharing;
```

3. (Optional) Set environment variables:
```bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=carsharing
export DB_USER=postgres
export DB_PASSWORD=postgres
```

On Windows (PowerShell):
```powershell
$env:DB_HOST="localhost"
$env:DB_PORT="5432"
$env:DB_NAME="carsharing"
$env:DB_USER="postgres"
$env:DB_PASSWORD="postgres"
```

## Running the Project

```bash
mvn spring-boot:run
```

Or build and run the JAR:
```bash
mvn clean package
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

## Первый запуск

1. Запустите приложение
2. Вызовите `POST /register` с телом:
```json
{
  "firstName": "Админ",
  "lastName": "Системы",
  "email": "admin@carsharing.ru",
  "username": "admin",
  "password": "<ваш_пароль>"
}
```
3. Первый пользователь получит роль ADMIN. Используйте `admin` / ваш пароль для Basic Auth в Postman.

## Postman

Import `postman/Carsharing_API.postman_collection.json`:

- **Регистрация и аутентификация** — регистрация без авторизации
- **Full Scenario** — Create entities → Start ride → End ride → Pay → Verify (требуется Basic Auth)
- **Cars, Users, Rides, Payments** — CRUD с Basic Auth. Создайте Postman Environment с переменными `authUsername`, `authPassword`, `userPassword`.
