FROM maven:3.6-jdk-13 AS splendor-server-build
ADD target/SplendorServer.jar SplendorServer.jar
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean install

FROM openjdk:8-jdk-alpine
COPY --from=splendor-server-build /usr/src/app/target/SplendorServer.jar SplendorServer.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","SplendorServer.jar"]
