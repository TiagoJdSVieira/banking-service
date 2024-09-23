##################
# Build image
##################
FROM maven:3.8.6-eclipse-temurin-17 AS build-img

# Set the working directory inside the container
WORKDIR /build

# Copy the pom.xml file and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the entire project and build it
COPY . .
RUN mvn clean package -DskipTests

##################
# Build image
##################
FROM eclipse-temurin:17-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the built jar file from the build stage
COPY --from=build-img /build/target/*.jar ./app.jar

# Defining default environment
ENV SPRING_PROFILES_ACTIVE=docker

# Expose the port your application will run on
EXPOSE 8080

# Set the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]