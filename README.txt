main-service with embedded UI

How it works:
- Static files (index.html, styles.css, car.jpg) are served from Spring Boot's resources/static directory.
- Application runs on port 8000 (change in application.properties if needed).
- The UI uses relative path '/api/register' so no CORS is required when UI and API are served from the same host:port.
- Keep your car.jpg in src/main/resources/static so the background loads: src/main/resources/static/car.jpg

Build & run (locally):
1. Place your car.jpg in src/main/resources/static/
2. mvn -B -DskipTests package
3. java -jar target/*.jar
   or: mvn spring-boot:run

Docker:
- Dockerfile included exposes port 8000.

Downstream services:
- main-service still calls vehicle-service (8081), customer-service (8082), record-service (8083).
  Make sure those services are running and accessible from the main service.
