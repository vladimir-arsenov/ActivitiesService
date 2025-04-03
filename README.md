
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?style=for-the-badge&logo=postgresql&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-CC0200?style=for-the-badge&logo=flyway&logoColor=white)
![Kafka](https://img.shields.io/badge/Kafka-231F20?style=for-the-badge&logo=apache-kafka&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Quartz Scheduler](https://img.shields.io/badge/Quartz%20Scheduler-4A4A4A?style=for-the-badge&logoColor=white)
![Resilience4j](https://img.shields.io/badge/Resilience4j-6DB33F?style=for-the-badge&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white)
![OpenAPI](https://img.shields.io/badge/OpenAPI-6BA539?style=for-the-badge&logo=openapi-initiative&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)
![Caching](https://img.shields.io/badge/Caching-FF6F00?style=for-the-badge&logoColor=white)




# ActivitiesService

Это проект на базе Spring Boot, который позволяет пользователям организовывать мероприятия по интересам и присоединяться к ним.

## Структура проекта

- **Activity Service**: Основной сервис для создания заявки на проведения совместных активностей, а также для поиска и присоедниния к мероприятиям. Использует YandexMaps API для проверки адресов и Notification Service для отправки уведомлений пользователям (для этого применяется Kafka)
- **Notification Service**: Сервис для отправки уведомлений по email.

