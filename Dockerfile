FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY CounterDroneJavaWebsite/pom.xml ./pom.xml
COPY CounterDroneJavaWebsite/src ./src
RUN --mount=type=cache,target=/root/.m2 sh -c "printf '%s\\n' '<settings xmlns=\"http://maven.apache.org/SETTINGS/1.2.0\"><mirrors><mirror><id>google-central-mirror</id><mirrorOf>central</mirrorOf><url>https://maven-central.storage-download.googleapis.com/maven2/</url></mirror></mirrors></settings>' > /tmp/settings.xml && mvn -s /tmp/settings.xml -B -ntp -Daether.transport.http.retryHandler.count=5 -Daether.transport.http.retryHandler.interval=15000 clean package -DskipTests"

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/counter-drone-website-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
