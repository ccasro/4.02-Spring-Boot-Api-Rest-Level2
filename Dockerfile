# ===== ETAPA 1: BUILD =====
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copiamos dependencias primero (mejora cache)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos código fuente
COPY src ./src

# Construimos el JAR
RUN mvn clean package -DskipTests

# ===== ETAPA 2: RUNTIME =====
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copiamos solo el JAR final
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]