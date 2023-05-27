#
# Build stage
#
FROM maven:3.8.2-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

#
# Package stage
#
FROM adoptopenjdk:17-jdk-hotspot
COPY --from=build /target/diary-0.0.1-SNAPSHOT.jar demo.jar
ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","demo.jar"]