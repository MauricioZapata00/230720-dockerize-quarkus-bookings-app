# Create the docker image of the application

To create the docker image in your local machine you
need to do the following steps:
* First, at the same high of the project, run the next
command:
```shell script
./gradlew build -Dquarkus.package.type=legacy-jar
```
This is for build the container image run before 
create it
* Next we can create the image with the following
command:
```shell script
docker build -f Dockerfile -t quarkus-dummies-project-booking .
```
This will create an image of the compilation with the name of 
*"quarkus-dummies-project-booking"*.

**Remember this step because here is where we are naming the application
with the name previous mentioned**. If you want to modify the name you are
free to go. But remember to update the application image name inside
docker-compose file

# Configure application with Postgres using docker-compose

To run the application in your local machine environment, 
you need to have installed  [docker](https://www.docker.com/)
and [docker compose](https://docs.docker.com/compose/install/).

Once you have installed docker and docker-compose, then
you can navigate to the project folder and run the next command:

```shell script
docker-compose -f docker-compose.yaml up -d
```

This will create two containers, one with the quarkus application
named *"quarkus-booking"* and the other named *"postgres-15.3-alpine3.18"*

For the Postgres configuration you have the next
configuration:
```yaml
image: postgres:15.3-alpine3.18
container_name: postgres-15.3-alpine3.18
environment:
  - POSTGRES_USER=LocalDeveloper
  - POSTGRES_PASSWORD=G8gbm8gZXN
  - POSTGRES_DB=LocalDB
ports:
  - '5432:5432'
```
Which will create the container based on the [postgres image](https://hub.docker.com/layers/library/postgres/15.3-alpine3.18/images/sha256-5b9805a36dfe65c019fbdf3f33fd57f7082a554cc9af79a135e3ec632344bd07?context=explore)
available in dockerhub and will create the database configuration with
the properties you can see below:
* db_username = LocalDeveloper
* db_password = G8gbm8gZXN
* db_name = LocalDB

For the application configuration you may find with this:
```yaml
depends_on: [db]
image: quarkus-dummies-project-booking
container_name: quarkus-booking
environment:
  - QUARKUS_DATASOURCE_REACTIVE_URL=vertx-reactive:postgresql://db:5432/LocalDB
  - QUARKUS_DATASOURCE_USERNAME=LocalDeveloper
  - QUARKUS_DATASOURCE_PASSWORD=G8gbm8gZXN
  - QUARKUS_HIBERNATE-ORM_DATABASE_GENERATION=update
ports:
  - '8100:8100'
```
Remember the image name must be the same that you had created previously.
 If you have a different application image name, you will have to change
the image name for the one you have created.

Here the only difference between the application.properties file of the
project itself and the container's environment variables is that the *QUARKUS_DATASOURCE_REACTIVE_URL* 
is connecting to the container running the postgres database. If you try 
to connect to the database IP *localhost:5432* you may find an issue like
[this](https://stackoverflow.com/questions/72138430/i-have-a-java-spring-boot-with-postgresql-within-docker-compose-org-postgresql)

**Ports are mapped from the 8100 of your local machine to the 8100 of docker**
just because I do not like to use the default quarkus port (8080)

## Curls to test the dockerize application

* Get all bookings
```shell
curl --location 'http://localhost:8100/booking'
```

* Create new booking
```shell
curl --location 'http://localhost:8100/booking' \
--header 'Content-Type: application/json' \
--data '{
    "name": "Booking name",
    "latitude": 25.12376,
    "longitude": 121.25355,
    "dateTime": 1689874991652
}'
```

* Update booking
```shell
curl --location --request PUT 'http://localhost:8100/booking/1' \
--header 'Content-Type: application/json' \
--data '{
    "name": "Booking name",
    "latitude": -48.44177,
    "longitude": -176.77486,
    "dateTime": 1689875669661
}'
```
# Bibliography and documentation based

* [Quarkus CLI](https://www.youtube.com/watch?v=BL67jwPYvRs)
* [Hash Interactive](https://hashinteractive.com/blog/docker-compose-up-with-postgres-quick-tips/)
* [Geshan Manandhar](https://geshan.com.np/blog/2021/12/docker-postgres/)
* [JDBC Driver - PostgreSQL](https://quarkus.io/extensions/io.quarkus/quarkus-jdbc-postgresql)
* [Data sources in Quarkus](https://quarkus.io/guides/datasource)
* [Reactive SQL Clients in Quarkus](https://es.quarkus.io/guides/reactive-sql-clients)
* [Quarkus Hibernate ORM and Jakarta Persistence](https://es.quarkus.io/guides/hibernate-orm)
* [Using Hibernate Reactive](https://es.quarkus.io/guides/hibernate-reactive)
* [Random.org](https://www.random.org/geographic-coordinates/) just to play 
around and generate random latitude and longitude values.

# DummiesProject


This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./gradlew quarkusDev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./gradlew build
```
It produces the `quarkus-run.jar` file in the `build/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/quarkus-app/lib/` directory.

The application is now runnable using `java -jar build/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./gradlew build -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar build/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./gradlew build -Dquarkus.package.type=native
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./gradlew build -Dquarkus.package.type=native -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./build/code-with-quarkus-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/gradle-tooling.

## Provided Code

### RESTEasy Reactive

Easily start your Reactive RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
