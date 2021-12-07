# Task Management Tool for Beyond homework

Spring Boot application for managing users and their tasks.

## Project details
- Java: 8
- Spring Boot: 2.5.7
- Build: Maven
- Dependencies:
  - Spring Security
  - Spring Web
  - Flyway
  - H2 database
  - Lombok
  - JWT
  - JUnit 4, Mockito

## Profiles
Created 3 profiles for the project:
- Default: Uses the production database (tmt). Scheduler is enabled. Migration, security is disabled.
- PROD: Uses the production database (tmt). Scheduler, migration is enabled.
- TEST: Uses test database (tmtTest), data_test.sql init the database with test data. Scheduler, migration, security disabled.

(The databases are H2 in-memory databases)

## Testing
Created integration tests for controllers and unit tests for the service layers with JUnit 4 and Mockito. The tests are only runnable in the TEST profile. The controller tests are created with the usage of MockMvc which starts a Servlet container to test endpoints with actual data from the database.

## Security - Authentication
Implemented a token-based authentication with JSON Web Tokens (JWT) library. I've disabled authentication in all profiles to make testing easier but it can be enabled within the application-*.properties files.
```
security.enabled=true
```
If you enable it, you won't be able to access the REST endpoints until successful authentication. Use the following cURL command to login with any existing user (admin is exists in "tmt" database):
```
curl -L -X POST 'http://localhost:8080/login' -H 'Content-Type: application/json' --data-raw '{
    "username":"admin",
    "password":"admin"
}'
```
The /login endpoint returns with a JWT token:
```
{
    "jwtToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbXSwiZXhwIjoxNjM4OTI4MDA2LCJpYXQiOjE2Mzg5MTAwMDZ9.-799cruzlvQNCUcfoQ9x9UfrlW08ZhfxPAnXZUME7xpC3q9ocYRM6ThijoXqVLKiN00Beq9x1nD3rDO4_9eK9g"
}
```
This token has to be in the request header in order to reach all the other REST endpoints. Example request:
```
curl -L -X GET 'http://localhost:8080/api/user' -H 'Accept: application/json' -H 'Content-Type: application/json' -H 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbXSwiZXhwIjoxNjM4OTI4MDA2LCJpYXQiOjE2Mzg5MTAwMDZ9.-799cruzlvQNCUcfoQ9x9UfrlW08ZhfxPAnXZUME7xpC3q9ocYRM6ThijoXqVLKiN00Beq9x1nD3rDO4_9eK9g'
```
## Logging
Created a custom DB logging functionality which uses the LOGS table to persist TRACE, INFO, DEBUG, WARN, ERROR messages and exceptions. These log levels can be changed in the application-*.properties files.
```
logger.loglevel=DEBUG
```

## Migration
The project uses the Flyway database-migration tool for version controlling. The migration files can be found in the resources/db/migration path. New migration can created under /migration folder with the following naming convention: ```V2__migration_description.sql```

## Containerization
Added a Dockerfile to the project which build and package the project with ```maven:3-3-jdk-8``` and run the app with ```openjdk:8-jdk-alpine```. The Dockerfile runs the application with PROD profile therefore it's starts the migration on the "tmt" database.

## Scheduler
Created the scheduler for the bonus task. It runs every 5 minutes and check if there's any pending tasks which dateTime attribute passed the current time.




