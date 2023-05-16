# Computer Management System

The Computer Management System is a Java-based application that allows system administrators to manage computers and their assignments to employees.

## Table of Contents
- [Introduction](#introduction)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Setup](#setup)
- [Usage](#usage)
- [Future Improvements](#future-improvements)
- [Contributing](#contributing)
- [License](#license)

## Introduction

The Computer Management System is designed to provide an efficient way for system administrators to track and manage computers within an organization. It allows administrators to add new computers and employees, assign computers to employees, retrieve computer information, and perform other related tasks.

## Features

- Add a new computer to the system
- Add a new employee to the system
- Assign a computer to an employee
- Retrieve information about a computer via computer id
- Retrieve all computers in the system
- Retrieve all computers assigned to a specific employee
- Remove a computer assignment from an employee

## Technologies Used

The Computer Management System is built using the following technologies:

- Java: The programming language used for the backend development.
- Spring Boot: The framework used to create the RESTful API and manage application components.
- Spring Data JPA: The library used for database access and management.
- Hibernate: The ORM (Object-Relational Mapping) tool for mapping Java objects to the database.
- MySQL: The relational database management system used for persistent storage of data.
- Lombok: The library used for reducing boilerplate code and enhancing productivity by generating getter, setter, and other common methods.
- JUnit: The testing framework used for unit testing.
- Mockito: The mocking framework used for creating test doubles in unit tests.

## Setup

To set up the Computer Management System locally, follow these steps:

1. Clone the repository: `git clone https://github.com/huseyinbarin/computer-management-system.git`
2. Navigate to the project directory: `cd computer-management-system`
3. System, you need to have MySQL database installed on your environment. Please make sure you have MySQL installed and running before proceeding with the setup. 
4. Make sure you have Docker installed on your environment.
5. Pull the Docker image for the messaging service by running the following command: `docker pull greenbone/exercise-admin-notification`
6. Once the image is downloaded, you can run the container using the following command: `docker run -d -p 8080:8080 greenbone/exercise-admin-notification`
7. Build the project: `mvn clean install`
8. Run the application: `mvn spring-boot:run`
9. The application will be accessible at: `http://localhost:8989`

## Usage

The Computer Management System provides a RESTful API for interacting with the system. You can use tools like cURL or Postman to make HTTP requests to the available endpoints.

Here are some examples of API endpoints:

- POST `/api/computers` - Add a new computer to the system.
- GET `/api/computers` - Retrieve all computers in the system.
- GET `/api/computers/{id}` - Retrieve information about a specific computer.
- PUT `/api/computers/{computerId}/assign/{employeeId}` - Assign a computer to an employee.
- GET `/api/computers/employee/id/{employeeId}` - Retrieve all computers assigned to a specific employee.
- DELETE `/api/computers/{id}` - Remove a computer assignment from an employee.


## Future Improvements

While the current version of the Computer Management System provides basic functionality, there are several areas that could be improved:

- Authentication and Authorization: Implement a secure authentication mechanism to protect the API endpoints and restrict access based on user roles and permissions.
- Validation and Error Handling: Enhance the input validation and error handling mechanisms to provide more detailed error messages and improve the overall user experience.
- Implement caching: Consider implementing caching in the future to improve performance and reduce database load.
- Logging and Monitoring: Implement logging and monitoring features to track system events and performance metrics, enabling better system management and issue troubleshooting.
- Frontend Development: Develop a user-friendly web interface or a dedicated client application to facilitate easier interaction with the system.
- Performance Optimization: Analyze and optimize the application's performance by identifying bottlenecks
