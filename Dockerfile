FROM gradle:jdk17-corretto AS build
WORKDIR /app
COPY . .
RUN ./gradlew build -x test

FROM amazoncorretto:17-alpine-jdk
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
