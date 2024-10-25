# Use an official OpenJDK runtime as a parent image
FROM openjdk:11-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the jar file into the container at /app
ADD ./target/psyche-nest-1.0-SNAPSHOT.jar /app/psyche-nest.jar

ENTRYPOINT [ "sh", "-c", "java -Dlog4j2.formatMsgNoLookups=true -Dspring.profiles.active=$PROJECT_ENV -jar psyche-nest.jar"]

# Make port 8080 available to the world outside this container
EXPOSE 8080
