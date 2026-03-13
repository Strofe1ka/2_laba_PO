# Carsharing — Spring Boot + PostgreSQL + JPA

## Project Topic

Carsharing REST API — a system for managing car rentals, users, rides, and payments. Users can start rides with available cars, end rides (cost calculated by distance × 10), and pay for completed rides.

## Main Entities

| Entity   | Description                                      |
|----------|--------------------------------------------------|
| **Car**  | Vehicle with brand, model, plate number, availability |
| **User** | Customer with first name, last name, email       |
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

### Cars
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/cars` | Create car |
| GET | `/cars` | Get all cars (optional `?available=true`) |
| GET | `/cars/available` | List available cars |
| GET | `/cars/{id}` | Get car by ID |
| PUT | `/cars/{id}` | Update car |
| DELETE | `/cars/{id}` | Delete car |

### Users
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/users` | Create user |
| GET | `/users` | Get all users |
| GET | `/users/{id}` | Get user by ID |
| GET | `/users/{id}/income` | Get user income (sum of paid rides) |
| PUT | `/users/{id}` | Update user |
| DELETE | `/users/{id}` | Delete user |

### Rides
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/rides` | Start ride |
| GET | `/rides` | Get all rides |
| GET | `/rides/{id}` | Get ride by ID |
| PUT | `/rides/{id}/end` | End ride |
| POST | `/rides/{id}/pay` | Pay for ride |
| DELETE | `/rides/{id}` | Delete ride |

### Payments
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

1. Start PostgreSQL.
2. Create the database:
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

## Test Data

On first run, a `CommandLineRunner` loads:

- 4 cars (Toyota Camry, Honda Civic, BMW X5, Mercedes E-Class)
- 4 users
- 3 rides (with start/end times and costs)
- 3 payments (2 paid, 1 unpaid)

## Postman

Import `postman/Carsharing_API.postman_collection.json`:

- **Full Scenario** — Create entities → Start ride → End ride → Pay → Verify income and available cars
- **Cars, Users, Rides, Payments** — CRUD and business operation requests with example bodies
