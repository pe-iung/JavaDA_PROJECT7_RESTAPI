# Trading Application

A Spring Boot application for managing trading operations, including bid lists, curve points, ratings, rules, and trades.

## Technical Stack

### Backend
- Java 21
- Spring Boot 3.1.0
- Spring Security 6
- Spring Data JPA
- Hibernate
- Maven

### Frontend
- Thymeleaf
- Bootstrap 5.3.3
- HTML5/CSS3

### Database
- MySQL (Production)
- H2 (Testing)

### Testing
- JUnit 5
- Mockito
- Spring Security Test
- JaCoCo for test coverage

### Security
- Spring Security
- BCrypt password encoding
- CSRF protection
- Session management

## General Solution Description

The application provides a secure platform for managing various trading operations. It follows a layered architecture:
- Presentation Layer (Controllers & Views)
- Business Layer (Services)
- Data Access Layer (Repositories)
- Domain Layer (Entities)

### Key Design Patterns
- MVC (Model-View-Controller)
- DTO (Data Transfer Objects)
- Repository Pattern
- Service Layer Pattern

## System Interfaces

### External Interfaces
- MySQL Database
- Web Browser Interface
- REST API Endpoints

### Internal Interfaces
- JPA Repositories
- Service Layer Interfaces
- Controller-Service Communication

## Main Features

### User Management
- User registration
- Role-based access control (USER/ADMIN)
- Profile management
- Password encryption

### Trading Operations
1. **Bid List Management**
   - Create new bids
   - View bid list
   - Update bid details
   - Delete bids

2. **Curve Points**
   - Add curve points
   - View curve list
   - Modify curve data
   - Remove curve points

3. **Rating Management**
   - Create ratings
   - View rating list
   - Update ratings
   - Delete ratings

4. **Rule Management**
   - Define trading rules
   - List all rules
   - Modify rule parameters
   - Remove rules

5. **Trade Management**
   - Record trades
   - View trade history
   - Update trade details
   - Delete trade records

## Database Structure

### Tables

#### users
- id (PK)
- username (UNIQUE)
- password
- fullname
- role

#### bidlist
- BidListId (PK)
- account
- type
- bidQuantity
- askQuantity
- bid
- ask
- benchmark
- ... (other trading fields)

#### curvepoint
- id (PK)
- curveId
- asOfDate
- term
- value
- creationDate

#### rating
- id (PK)
- moodysRating
- sandPRating
- fitchRating
- orderNumber

#### rulename
- id (PK)
- name
- description
- json
- template
- sqlStr
- sqlPart

#### trade
- tradeId (PK)
- account
- type
- buyQuantity
- sellQuantity
- buyPrice
- sellPrice
- ... (other trade fields)


## Security Architecture

### Authentication
- Form-based authentication
- Session-based user tracking

### Authorization
- Role-based access control (RBAC)
- URL-based security
- Method-level security
- Custom access denied handling


### User Roles
1. **USER**
   - View own profile
   - Manage trades
   - View bid lists
   - Access curve points

2. **ADMIN**
   - All USER permissions
   - User management
   - System configuration
   - Access all data

## Getting Started

### Prerequisites
- Java 21
- Maven
- MySQL

### Installation
1. Clone the repository
2.	Configure database
    spring.datasource.url=jdbc:mysql://localhost:3306/trading_db
    spring.datasource.username=your_username
    spring.datasource.password=your_password
3.	Build the project
    mvn clean install
4.	Run the application
    mvn spring-boot:run

### Testing
    Run tests with coverage
    mvn test

    View coverage report:
    target/site/jacoco/index.html
