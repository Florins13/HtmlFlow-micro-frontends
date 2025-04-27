# Use an official OpenJDK runtime as the base image
FROM openjdk:17-alpine

# Set the working directory inside the container
WORKDIR /app

ENV LANGUAGE='en_US:en'
COPY .mvn .mvn
COPY mvnw .


# Copy the pom and source
COPY pom.xml .
COPY src ./src
COPY --chmod=755 mvnw .

RUN ./mvnw clean package -DskipTests -Dgpg.skip -X
#for now lets copy the target and run it
#COPY target ./target

# Expose the port Quarkus listens on (usually 8080)
EXPOSE 8082

# Run the Quarkus application
ENTRYPOINT ["java", "-jar", "target/quarkus-app/quarkus-run.jar"]