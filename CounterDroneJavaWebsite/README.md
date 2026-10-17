# AI-Based Counter-Drone Detection and Threat Management — Java Spring Boot

This is an academic demonstration website built with Java 17, Spring Boot, Thymeleaf, Spring Data JPA and a database.

## Features
- Demo login and session-based logout
- Dashboard with database-backed totals and recent detections
- Add and view simulated drone detections
- Transparent rule-based threat classification (HIGH / MEDIUM / LOW)
- Incident alert queue and mark-as-reviewed action
- Add, activate and deactivate surveillance zones
- Reports and statistics
- H2 database for quick local running; MySQL driver/configuration supported through environment variables
- Responsive pages for laptop and phone browsers

## Demo login
- Administrator: `admin` / `Drone@123`
- Operator: `operator` / `Secure@123`

These credentials are hard-coded for classroom demonstration only. Do not use this authentication design for real deployments.

## Run locally
Requirements: Java 17+ and Maven.

1. Extract the ZIP and open the `CounterDroneJavaWebsite` folder in VS Code or IntelliJ.
2. Open a terminal in that folder.
3. Run `mvn spring-boot:run`.
4. Open `http://localhost:8080` and sign in with the demo credentials.

The default local database is an H2 file at `./data/counterdrone`, so records normally survive app restarts on the same computer. H2 console is not enabled.

## MySQL setup
The project includes the MySQL JDBC driver. Set these environment variables to use an external MySQL database:

- `SPRING_DATASOURCE_URL=jdbc:mysql://HOST:3306/DATABASE?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true`
- `SPRING_DATASOURCE_USERNAME=YOUR_DATABASE_USER`
- `SPRING_DATASOURCE_PASSWORD=YOUR_DATABASE_PASSWORD`

Use the real host/database credentials from your MySQL provider. Never commit database passwords to GitHub. Hibernate creates/updates the tables using `spring.jpa.hibernate.ddl-auto=update` for this educational prototype.

## Render deployment
The repository includes a Dockerfile. Push the project to GitHub, then in Render choose **New + → Web Service**, connect the repository, select **Docker**, and deploy. Render sets `PORT`, which the app reads through `${PORT:8080}`. The default H2 database on a hosted instance may be ephemeral and should not be treated as durable storage. For persistent hosted data, provision a MySQL database from a database provider and set the three environment variables above in Render's Environment settings.

## Threat rules used for demonstration
- HIGH: restricted-area flag is checked, or distance is 300 metres or less.
- MEDIUM: otherwise, distance is 1,000 metres or less, or speed is above 80 km/h.
- LOW: none of those conditions match.

These values are illustrative classroom rules, not validated safety criteria. This is not a trained AI model and does not connect to real drone sensors or provide real counter-drone response capabilities.

## Pages
- `/dashboard`
- `/detections` and `/detections/new`
- `/alerts`
- `/zones`
- `/reports`
