FROM maven AS build

WORKDIR /app
COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:21-jdk

ARG JAR_FILE=target/order-system-0.0.1-SNAPSHOT.jar
COPY --from=build /app/${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
