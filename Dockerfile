#Start with a base image containing Java runtime
FROM openjdk:11-alpine

#Create work folder
RUN mkdir /work
RUN chmod 555 /work

# Set environment variable
ENV SECRETSVOL $SECRETSVOL

# Make port 8087 available to the world outside this container
EXPOSE 8087
ARG JAR_FILE=app/target/*.jar
# Copying the application's jar file inside the container
COPY ${JAR_FILE} /work/app.jar

# Copy SSL Related Assets to the container
#COPY app/src/main/resources/ssl/ /work/ssl

USER nobody
# Run the jar file
CMD java -jar /work/app.jar

