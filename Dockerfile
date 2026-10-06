FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy AS runtime
WORKDIR /app
RUN groupadd -r appgroup && useradd -r -g appgroup appuser
COPY --from=build --chown=appuser:appgroup /app/target/*.jar app.jar
USER appuser:appgroup
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "app.jar"]