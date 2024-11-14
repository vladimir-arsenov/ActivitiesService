FROM gradle:7.5.1-jdk17 AS builder
WORKDIR /app

# Копируем Gradle файлы для кэширования зависимостей
COPY build.gradle.kts settings.gradle.kts /app/
# Для build.gradle можно также использовать без .kts, если у вас Groovy вместо Kotlin DSL

# Загружаем зависимости
RUN gradle dependencies --no-daemon

# Копируем остальные файлы и создаем jar
COPY . .
RUN gradle bootJar --no-daemon

# Второй слой: образ с JRE 17 для запуска jar файла
FROM openjdk:17-jdk-slim
WORKDIR /app

# Копируем сгенерированный jar файл из предыдущего шага
COPY --from=builder /app/build/libs/*.jar app.jar

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]