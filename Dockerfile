# Use the official OpenJDK base image
FROM openjdk:19-jdk-slim
# Use the official OpenJDK base image

# Metadata as described above
LABEL maintainer="constantin.nimigean@gmail.com"
LABEL version="1.0"
LABEL description="Docker image for nserver-service Spring Boot application"

# Set the current working directory inside the image
WORKDIR /app
EXPOSE 8081
# Copy maven executable to the image
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Asigură-te că mvnw are permisiuni de execuție
RUN chmod +x mvnw

# Build all the dependencies in preparation to go offline.
RUN ./mvnw dependency:go-offline -B


# Copy the project source
COPY src src

# Package the application
RUN ./mvnw package -DskipTests

# Specify the start command and entry point of the Spring Boot application
ENTRYPOINT ["java","-jar","/app/target/serv1-0.0.1-SNAPSHOT.jar"]