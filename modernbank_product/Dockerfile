FROM openjdk:17-jdk-slim

# Add Author info
#LABEL maintainer="euideok@amazon.com"

# Add a volume to /tmp
VOLUME /tmp

# Make port 8080 available to the world outside this container
EXPOSE 8076

# The application's jar file
ARG JAR_FILE=build/libs/modernbank_product-0.0.1-SNAPSHOT.jar

# Add the application's jar to the container
ADD ${JAR_FILE} modernbank_product.jar

# Run the jar file
ENTRYPOINT ["java","-jar","/modernbank_product.jar"]
