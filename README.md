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

### Running in dev mode
Copy **src/main/resources/application-env.yaml.model** as **src/main/resources/application-dev.yaml** and add your own configurations.
You need to setup firebase configuration as well. Go to <https://firebase.google.com/docs/admin/setup> to create a service account and download the corresponding json file and save it to your project under **src/main/resources/firebase/somename.json**
For development builds/runs you don't need sms gateway properties. In fact they won't be used even if you set them.

When done setting up the configuration run:
```sh
mvn spring-boot:run -Pdev
```

You're all set!
