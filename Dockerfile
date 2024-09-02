
FROM openjdk:17-jdk-slim AS build

WORKDIR /build

COPY . /build

RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /build/target/UserRegisterTest-0.0.1-SNAPSHOT.jar /app/UserRegisterTest-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/UserRegisterTest-0.0.1-SNAPSHOT.jar"]
