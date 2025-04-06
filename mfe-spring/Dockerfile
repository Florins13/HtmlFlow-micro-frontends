FROM openjdk:17-jdk-alpine

WORKDIR /app

# Copy the Maven wrapper and make it executable
COPY .mvn .mvn
COPY mvnw .
RUN chmod +x mvnw

# Copy the pom and source
COPY pom.xml .
COPY src ./src

# Use the Maven wrapper to build
RUN ./mvnw clean package -DskipTests

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "target/mfe-spring-0.0.1-SNAPSHOT.jar"]