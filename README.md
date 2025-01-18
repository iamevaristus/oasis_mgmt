# Oasis Task Management Application

## Overview

The **Oasis Task Management Application** is a full-stack web application that allows users to manage their tasks efficiently. Built with **Angular** on the frontend and **Spring Boot** on the backend, it provides features for task creation, update, deletion, and search, along with user authentication and profile management. The application stores data in a **PostgreSQL** database and ensures secure access through authentication and authorization mechanisms.

---

## Features

### Frontend (Angular)
- **User Authentication**:
    - Register, log in, and log out using email and password.
- **Dashboard**:
    - View a list of tasks upon logging in.
- **Task Management**:
    - Create, update, and delete tasks.
    - Search for tasks by title and category.
    - Tasks include title, description, due date, priority, status, and optional category.
- **User Profile**:
    - View and update profile information, including name and email.
- **Partially Implemented**:
    - Task filtering and sorting by status and priority.
    - Changing user profile picture.

### Backend (Spring Boot)
- **RESTful API**:
    - Endpoints for user management (registration, authentication, profile updates).
    - Endpoints for CRUD operations on tasks.
- **Data Storage**:
    - PostgreSQL database integration for users and tasks.
- **Security**:
    - Authentication and authorization using JWT.
- **Validation**:
    - Input validation for user registration and task creation/update.
- **Error Handling**:
    - Proper error handling and informative error responses.

---

## Prerequisites

- **Backend**:
    - Java 17+
    - PostgreSQL
- **Frontend**:
    - Node.js
    - Angular CLI

---

## Environment Setup

### Backend Configuration
Add the following environment variables to your system or in a `.env` file:

```
DB_PASSWORD=Your DB password
DB_URL=Your DB url
DB_USERNAME=postgres
JWT_EXPIRATION_TIME=31652400000 ### Or whatever you prefer
JWT_SECRET_KEY=35d82b3737b72bcc9f56308110a36b60631dd2d6597b3f21f7363dde9a8c294cd79640b8ea4f687f9b31a132e4cd947a433d504f68ffa808851ade5b4da49768 ### or whatever you prefer
SERVER_PORT=8080 ### or whatever you prefer
```

### Database
Create a PostgreSQL database named `oasis` and ensure the credentials match the environment variables above.

---

## Installation

### Backend
1. Clone the repository:
   ```bash
   git clone <repository_url> ### You can find the repository url from the Http button above
   cd oasis/backend
   ```
2. Build the backend:
   ```bash
   ./mvnw clean install
   ```
3. Run the backend:
   ```bash
   ./mvnw spring-boot:run
   ```

### Frontend
1. Navigate to the frontend directory:
   ```bash
   cd oasis/frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the Angular development server:
   ```bash
   ng serve
   ```

---

## API Endpoints

### Authentication
1. **Login**:
   ```bash
   curl -X POST --location "http://localhost:8080/api/v1/auth/login" \
   -H "Content-Type: application/json" \
   -d '{
         "email_address": "user@example.com",
         "password": "password"
       }'
   ```

2. **Sign Up**:
   ```bash
   curl -X POST --location "http://localhost:8080/api/v1/auth/signup" \
   -H "Content-Type: application/json" \
   -d '{
         "first_name": "John",
         "last_name": "Doe",
         "email_address": "user@example.com",
         "password": "password"
       }'
   ```

3. **Update Profile**:
   ```bash
   curl -X PATCH --location "http://localhost:8080/api/v1/account/update" \
   -H "Content-Type: application/json" \
   -d '{
         "first_name": "Jane",
         "last_name": "Doe",
         "email_address": "jane.doe@example.com"
       }'
   ```

### Task Management
1. **Fetch Tasks**:
   ```bash
   curl -X GET "http://localhost:8080/api/v1/task?page=0&size=10"
   ```

2. **Create Task**:
   ```bash
   curl -X POST "http://localhost:8080/api/v1/task/create" \
   -H "Content-Type: application/json" \
   -d '{
         "title": "Sample Task",
         "description": "This is a sample task.",
         "priority": "HIGH",
         "status": "PENDING",
         "category": "Work",
         "due_date": "2025-01-20"
       }'
   ```

3. **Update Task**:
   ```bash
   curl -X PATCH "http://localhost:8080/api/v1/task/update/{id}" \
   -H "Content-Type: application/json" \
   -d '{
         "title": "Updated Task",
         "description": "This is an updated task.",
         "priority": "MEDIUM",
         "status": "IN_PROGRESS",
         "category": "Personal",
         "due_date": "2025-01-25"
       }'
   ```

4. **Delete Task**:
   ```bash
   curl -X DELETE "http://localhost:8080/api/v1/task/delete/{id}?category=Work"
   ```

5. **Search Tasks**:
   ```bash
   curl -X GET "http://localhost:8080/api/v1/task/search?query=Sample&category=Work&page=0&size=10"
   ```

---

## Usage

1. Run the backend and frontend servers as described in the [Installation](#installation) section.
2. Open the frontend in your browser at `http://localhost:4200`.
3. Use the following features:
    - Register, log in, and manage tasks.
    - Search for tasks by title and category.
    - Update or delete tasks.

---

## Limitations
- Frontend does not currently support:
    - Changing the user profile picture.
    - Filtering and sorting tasks by status and priority.

---

## JavaScript Design

The application uses Angular for the frontend, focusing on modular and reusable components. Key features include:
- **Reactive Forms**: For form handling and validation.
- **Angular Services**: For managing API requests and state.
- **Routing**: Provides navigation between login, dashboard, and other views.

---

## Contribution

To contribute:
1. Fork the repository.
2. Create a new branch for your feature/fix.
3. Push your changes and create a pull request.

---

## License

This project is not licensed under the any license yet.

---

## Contact

For support, contact:
- **Developer Email**: evaristusadimonyemma@gmail.com
- **Email**: info@oasismgt.net
- **Phone**: +2347013234484

Push your code to GitHub and add **"Oasisdevcloud"** as a collaborator.# oasis_mgmt
# oasis_mgmt
# oasis_mgmt
