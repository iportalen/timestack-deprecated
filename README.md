[![N|Solid](https://iportalen.dk/images/blue-logo-full.png)](https://iportalen.dk/images/blue-logo-full)

# Timestack (By iportalen ApS)
___
Timestack provides a Check in/out service for companies and organisations
___

## Requirements
  - Java JDK 8
  - Maven 3

## Technologies
  - Java 8
  - Maven 3
  - Spring Boot v2.0.3.RELEASE
  - Spring Security v5.0.6.RELEASE (Managed by spring-boot)
  - Mysql v5.1.46 (Managed by spring-boot)
  - Spring Data JPA (Managed by spring-boot)
  - Freemarker v2.3.28 (Managed by spring-boot)
  - Liquibase v3.5.5 (Managed by spring-boot)
  - Firebase Admin SDK v6.3.0
  - Lombok v1.16.22 (Managed by spring-boot)
  - Magic...

## Guidelines

### Configuration
In order to run Timestack you need to follow the following steps.

#### Project configuration
> This project configuration is for development builds. Replace **dev** with **prod** to make a prod configuration.
1. Copy **src/main/resources/application-env.yaml.model** as **src/main/resources/application-dev.yaml**
2. Populate the newly created file your own configurations.

#### Firebase configuration 
1. Create a service account at <https://firebase.google.com/docs/admin/setup> 
2. Download the corresponding json file and save it to your project under **src/main/resources/firebase/somename.json**

#### Bonus info
For development builds/runs you don't need sms gateway properties. In fact they won't be used even if you set them.

#### Running the project

```sh
mvn spring-boot:run -Pdev
```

You're all set!
