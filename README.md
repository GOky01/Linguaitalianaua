# LinguaUa

This is an app that allows users to translate ukrainian words to italian and train the language at all. Requirements For building and running the application you need:

1.git clone following link https://github.com/GOky01/Linguaitalianaua.git on your local machine

2.or download project ZIP image and unzip it to any directory you wish

3.finally you better to open your project and run it with some IDE for java

ALSO you must have: 1.JDK 1.8 2.Maven 3

Running the application locally There are several ways to run a Spring Boot application on your local machine. One way is to execute the main method in the **LinguaItalianaUaApplication.java** class from your IDE.

Alternatively you can use the Spring Boot Maven plugin like so:

mvn spring-boot:run

You should make next steps:
1.Open in terminal your mvn wrapper class

2.Then input command mvn spring-boot:run

3.and press Ctrl+Enter.

Used dependencies:
mysql-connector-java and spring-boot-starter-data-jpa for db
lombok for auto configuration
spring-boot-starter-thymeleaf to work with MVC pattern 
and so more

Besides, you should use MySQL Workbench Client or MySQL Docker image to work with database

And you should create user with password indicated in your application.properties file:
E.g:
User:goky
Password:2001a2001a

Last but not least, run **DataBuilder.java** class in order to populate your db with some data, which are getting from json file
