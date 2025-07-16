#  Rewards Program API

This is a Spring Boot-based RESTful API for a customer rewards program. It tracks customer transactions and calculates reward points based on transaction amounts. The system provides endpoints to retrieve transaction history and reward points for individual customers.

##  Features

- Retrieve all customer transactions
- Filter transactions by customer and month
- View recent transactions (last 3 months)
- Calculate and summarize reward points per customer

##  Project Structure

- **Controller**: `RewardsController` — Handles HTTP requests and maps them to service methods.
- **Service**: `RewardService` — Contains business logic for transaction retrieval and reward calculation.
- **DTO**: `RewardSummaryDTO` — Data Transfer Object for reward summaries.
- **Model**: `Transaction` — Represents a customer transaction.
- **Repository**: `TransactionRepository` — Interface for database operations (assumed to be a Spring Data JPA repository).

##  Reward Calculation Logic

Reward points are calculated based on the amount spent in a transaction. Typically, the rules might be:

- 2 points for every dollar spent over $100
- 1 point for every dollar spent over $50 up to $100

##  API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/transactions` | Get all transactions from the last 3 months |
| `GET` | `/transactions?customerId=101`| Get all transactions for a customer.
| `GET` | `/transactions?customerId=101&month=2025-06` |  Get Transactions for customer based on customerId and specific month by passing them as optional parameters |
| `GET` | `/transactions/rewards/{customerId}` | Get rewards (monthly and total points) for a customer |

##  Technologies Used

- Java 17+
- Spring Boot
- Spring Web
- Spring Data JPA
- H2 / MySQL (assumed for persistence)
- Maven 

##  Getting Started

1. Clone the repository
2. Configure your database in `application.properties`
3. Run the application using your IDE or `mvn spring-boot:run`
4. Access the API at `http://localhost:8080/transactions/`

##  Sample Json Responses

1.**Endpoint: GET /transactions **
==========================================
**Response Example:**
```[
 {
  "id": 1,
  "customerId": 101,
  "amount": 120.0,
  "transactionDate": "2025-06-15"
 },
 {
  "id": 2,
  "customerId": 102,
  "amount": 75.0,
  "transactionDate": "2025-05-10"
 }
]```

2.**Endpoint: GET /transactions?customerId=101**
GET /transactions?customerId=101&month=2025-06
=======================================================
**Response Example:**
[
  {
    "id": 1,
    "customerId": 101,
    "amount": 120.0,
    "transactionDate": "2025-06-15"
  }
]

3.**Endpoint: GET /transactions/rewards/{customerId}**
======================================================
**Response Example:**
{
  "customerId": 101,
  "monthlyPoints": {
    "JUNE": 90,
    "MAY": 25
  },
  "totalPoints": 115
}


