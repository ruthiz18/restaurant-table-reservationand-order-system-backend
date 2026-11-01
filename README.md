# Restaurant Table Reservation and Order System

| | |
|---|---|
| **Names** | Ruth Izukondi |
| **Student ID** | 26880 |
| **Project** | Restaurant Table Reservation and Order System |

## Steps to run the API

1. **Install** JDK 25, Maven and PostgreSQL, then clone the repository:
   ```
   git clone https://github.com/ruthiz18/restaurant-table-reservationand-order-system-backend.git
   cd restaurant-table-reservationand-order-system-backend
   ```
2. **Create the database:**
   ```
   CREATE DATABASE restaurant_db;
   ```
3. **Configure credentials** in `src/main/resources/application.properties`
   (`spring.datasource.url`, `spring.datasource.username`, `spring.datasource.password`).
   Defaults: `jdbc:postgresql://localhost:5432/restaurant_db`, user `postgres`.
   Tables are created automatically (`spring.jpa.hibernate.ddl-auto=update`).
4. **Start the API** from the project root:
   ```
   mvn spring-boot:run
   ```
5. The API is now at `http://localhost:8080`.

## Testing with Postman

1. Import `restaurant-table-reservationand-order-system-midterm.postman_collection.json`.
2. Run the folder **0. Seed (Step 0, run once)** with the Collection Runner. It loads the Step 0 data and stores the ids as collection variables.
3. Run the requests **A2, B1–B3, C1–C4, D1–D4, E1–E3, F1, F2** and the two **Bonus** requests in order. There is one request per requirement, named by its ID. A1 (entities and relationships) has no endpoint; it is visible in the PostgreSQL tables.

## Endpoints

| Base path | Operations |
|---|---|
| `/api/shifts` | `POST /save`, `GET /all`, `GET /unused`, `GET /{id}`, `PUT /update/{id}`, `DELETE /delete/{id}` |
| `/api/dining-tables` | `POST /save`, `GET /all`, `GET /free`, `GET /{id}`, `PUT /update/{id}`, `DELETE /delete/{id}` |
| `/api/waiters` | `POST /save`, `GET /all`, `GET /by-shift?name=`, `PUT /{waiterId}/shifts/{shiftId}`, `PATCH /{waiterId}/dining-table?tableNumber=`, `GET /{id}`, `PUT /update/{id}`, `DELETE /delete/{id}` |
| `/api/guests` | `POST /save`, `GET /all`, `GET /by-last-name?lastName=`, `GET /of-waiter/{waiterId}`, `GET /{id}`, `PUT /update/{id}`, `DELETE /delete/{id}` |
| `/api/reservations` | `POST /save`, `GET /all`, `GET /by-status?status=`, `GET /between?start=&end=`, `PATCH /{id}/status?status=`, `GET /stats/by-status`, `PATCH /cancel-day?waiterId=&date=`, `GET /page`, `GET /{id}`, `PUT /update/{id}`, `DELETE /delete/{id}` |
| `/api/table-orders` | `POST /save?reservationId=`, `GET /all`, `GET /guest/{phone}`, `GET /{id}`, `DELETE /delete/{id}` |
| `/api/dashboard` | `GET /summary` |
