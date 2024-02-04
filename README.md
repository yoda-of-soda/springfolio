# Java Spring Maven portfolio project
As the header says, this is a portfolio project demonstrating my skills with `Java` and `Spring` using `Maven` for dependencies.

The observant reader will notice that the secrets are revealed in `application.properties`, but that's no shame since the web service is only run *locally* using a local database using `Docker`.

## How to run it
1. Make sure to have the following installed:
    * Java (preferably version 21)
    * Docker
2. Clone this project
3. Start the docker container with the database using `sh db_start.sh`
4. Run the application with command `mvn spring-boot:run`

## Before you panic
In case it doesn't work, check the following:
* Does the ip of your new docker container match property `spring.datasource.url` in `application.properties`?
* Do you have a dependency issue? Try command `mvn clean install`
* Are you using Java version 21? Check it using command `java --version`