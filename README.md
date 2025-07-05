# 💳 Payment Service – High-Level Project Overview

This project is a **production-grade, microservices-based Payment Service** built using **Spring Boot** and **Apache Kafka**. The architecture is inspired by real-world payment systems and demonstrates how distributed services can work together to handle critical transactional flows in a **scalable**, **secure**, and **resilient** manner.

The system is composed of **four independent microservices**:
- **UserService**
- **WalletService**
- **TransactionService**
- **NotificationService**

Each service is responsible for its own domain and database, and communicates asynchronously via **Apache Kafka** to ensure decoupling, fault-tolerance, and real-time event propagation. The services use **Gradle**, **Java 21**, **Spring Boot 3.4.x**, and are secured using **Spring Security**.

<br>

### ✅ **Features:**

**👤 User Service**  
- Creates and manages user accounts with roles (e.g., ADMIN, USER).  
- Publishes Kafka events upon user registration for downstream services.

**👛 Wallet Service**  
- Manages wallets associated with users.  
- Listens to Kafka events to initialize wallets and update balances.

**💰 Transaction Service**  
- Initiates and updates transaction flows between wallets.  
- Produces and consumes Kafka events for state transitions and audit logging.

**📬 Notification Service**  
- Sends email or other notifications by listening to Kafka topics.  
- Decouples communication responsibilities from business logic.

**📡 Asynchronous Messaging (Kafka)**  
- Ensures loose coupling and reliable inter-service communication using topics like:
  - `user.creation.topic`
  - `transaction.initiated.topic`
  - `transaction.updated.topic`

**🔐 Role-Based Security**  
- Spring Security integration for endpoint protection (e.g., only authenticated users can initiate transactions).

**🧩 Microservices Design**  
- Each service is independently deployable and maintainable.  
- Each service includes its own database, Kafka producer/consumer, and REST APIs.

**🧪 Testing Support**  
- All services support unit and integration testing using Spring Boot Test, Kafka Test, and JUnit.

<br>

### 🛠 **Tech Stack:**

**🧑‍💻 Language:** Java 21  
**⚙️ Framework:** Spring Boot 3.4.x  
**📬 Messaging:** Apache Kafka (Spring for Apache Kafka)  
**🛢️ Database:** MySQL (for persistent storage via Spring Data JPA)  
**🔐 Security:** Spring Security  
**🧪 Testing:** JUnit, Spring Boot Test, Kafka Test  
**🔧 Build Tool:** Gradle  
**📦 Others:** Lombok, Jackson, Bean Validation, OpenFeign (for service-to-service HTTP communication)

<br>

### 📁 Codebase Structure (High-Level Overview)

All services follow a clean layered architecture that separates responsibilities and ensures code maintainability.

| Module / Package            | Purpose                                                                                  |
|----------------------------|-------------------------------------------------------------------------------------------|
| `controller/`              | REST endpoints exposed to clients or other services.                                     |
| `dto/`                     | Defines structured request/response objects (data transfer layer).                       |
| `model/`                   | JPA entity definitions representing database schema.                                     |
| `repository/`              | Spring Data JPA repositories for DB interaction.                                         |
| `service/`                 | Business logic implementation, Kafka interaction, and orchestration.                     |
| `consumer/`                | Kafka listeners that react to domain events and trigger appropriate workflows.           |
| `configuration/`           | Beans for Kafka producers/consumers, security, and general service configuration.        |
| `constants/`               | Application-level constants like topic names, status codes, and user roles.              |
| `resources/`               | Application configuration (`application.properties`) for each service.                   |
| `<ServiceName>Application.java` | Main Spring Boot entry point per microservice.                                       |

Each service runs independently on a different port, has its own DB, and Kafka communication channels.

<br>

### 🎯 **What This Project Demonstrates**

* 🚀 **Microservice Architecture**: Showcases how to design loosely coupled services that can scale independently.
* 📡 **Event-Driven Systems**: Kafka-based event publishing and consuming for real-time communication.
* 🔗 **Spring Boot Expertise**: Deep usage of core Spring modules like Security, JPA, Validation, Kafka.
* 🧱 **Layered Design Principles**: Follows modular patterns — DTOs, controllers, services, repositories, and config.
* ⚙️ **Security and Access Control**: Endpoints are protected using Spring Security roles.
* 🧠 **Production-Readiness**: Code is structured for real deployment, observability, and testing.
* ✅ **Modern Java Development**: Uses Java 21 and Spring Boot 3.4 with Gradle for modern backend development.

---

> 🔄 This project simulates a real-world digital wallet system that handles secure financial transactions and user onboarding, with Kafka-based real-time notifications — making it ideal for demonstrating **backend system design**, **microservices communication**, and **production-ready code quality**.

---
