# AI-Based Counter-Drone Detection and Threat Management — Java Website

This is a starter web application for your academic microproject. It uses **Java Spring Boot** for the backend and Thymeleaf HTML templates for the browser UI.

## Included
- Login page with demo username/password
- Session-based login and logout
- Dashboard with sample drone detections, threat levels, incident counts, sensor status, and zone status
- Responsive dark command-center design
- Simulated/sample data only

## Demo credentials
- Administrator: `admin` / `Drone@123`
- Operator: `operator` / `Secure@123`

## Requirements
- Java 17 or newer
- Maven 3.6+ (or use Maven support in your IDE)
- Internet access the first time Maven downloads dependencies

## Run in VS Code / IntelliJ
1. Extract the ZIP.
2. Open the `CounterDroneJavaWebsite` folder.
3. Run `mvn spring-boot:run` in the terminal.
4. Open `http://localhost:8080` in your browser.
5. Sign in using one of the demo accounts above.

Alternatively, run `CounterDroneApplication.java` from your Java IDE after Maven dependencies are loaded.

## Important
This is a demonstration starter, not a deployed public website. `localhost:8080` works on the computer running the application. A public URL requires hosting/deployment. The login credentials are hard-coded for demonstration; do not use this authentication setup for a real security system. MySQL persistence, password hashing, role enforcement, and the remaining project modules need to be added for a production-ready system.
