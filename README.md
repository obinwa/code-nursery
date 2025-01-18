# Online Code Editor Backend

## Project Overview
This project provides the backend services for an online code editor, designed to support multiple programming languages and offer real-time collaboration. It handles tasks such as code execution, file management, user authentication, and collaboration features like live editing and version control.

## Features
- **Multi-Language Support:** Execute and run code in various programming languages.
- **Real-Time Collaboration:** Users can collaborate in real-time with live updates.
- **User Authentication:** Secure user authentication and session management.
- **File Management:** Manage files and projects within the editor.
- **Version Control:** Basic version control to track changes and rollback if needed.
- **Code Execution:** Secure and isolated environments for executing code snippets.

## Tech Stack
- **Spring Boot:** Backend framework.
- **Java:** Primary programming language.
- **PostgreSQL:** Relational database for persistent storage.
- **Redis:** In-memory cache for session management and real-time updates.
- **Docker:** Containerization for isolated code execution environments.
- **WebSockets:** Real-time communication for live collaboration.

## Getting Started

### Prerequisites
- Java 17
- Maven or Gradle
- Docker
- PostgreSQL
- Redis

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/obinwa/code-nursery.git
   cd code-nursery
   ```
2. Configure the application properties:
   Update `src/main/resources/application.properties` with your database and Redis configurations.

3. Build the project:
   ```bash
   ./mvnw clean install
   ```

4. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
## API Documentation
The API documentation is available at `/swagger-ui.html` once the application is running. It includes endpoints for user authentication, project management, code execution, and more.

## Contributing
1. Fork the repository.
2. Create your feature branch:
   ```bash
   git checkout -b feature/YourFeature
   ```
3. Commit your changes:
   ```bash
   git commit -m 'Add some feature'
   ```
4. Push to the branch:
   ```bash
   git push origin feature/YourFeature
   ```
5. Open a pull request.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact
For any inquiries or support, please contact [chidiebere0nyeagbajoshua@gmail.com].

