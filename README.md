[![N|Solid](https://iportalen.dk/images/blue-logo-full.png)](https://iportalen.dk/images/blue-logo-full)

# Atwork (By iportalen ApS)
___
Atwork provides a Check in/out service for companies and organisations
___

## Requirements
  - Java JDK 8

## Technologies
  - Maven
  - Spring Boot v2.0.3.RELEASE
  - Spring Security v5.0.6.RELEASE (Managed by spring-boot)
  - Mysql v5.1.46 (Managed by spring-boot)
  - Spring Data JPA (Managed by spring-boot)
  - Freemarker v2.3.28 (Managed by spring-boot)
  - Liquibase v3.5.5 (Managed by spring-boot)
  - Firebase Admin SDK v6.3.0
  - Lombok v1.16.22 (Managed by spring-boot)
  - Magic

## Guidelines

### Running in dev mode
To be able to run the project you need to set some environment variables:



ATWORK_DB_USERNAME = your db username
ATWORK_DB_PASSWORD = your db username password
ATWORK_DB_URL = your db url

   - mvn -Dspring.profiles.active=dev spring-boot:run
