FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY CounterDroneJavaWebsite/pom.xml ./pom.xml
COPY CounterDroneJavaWebsite/src ./src
RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/counter-drone-website-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
