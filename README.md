# Sample Apache Ignite - Spring Boot application
The purpose of this project is to demonstrate [Ignite as a Cache Provider for Spring Cache](https://apacheignite-mix.readme.io/docs/spring-caching) in one package.
It also contains sample code on [Apache Ignite's Spring Data integration](https://apacheignite-mix.readme.io/docs/spring-data).

# How to build and run
Apache Ignite Version : 2.8.1

Java 8 lambdas are used here and there and thus the project can be compiled only with JDK 8 `javac`.
To compile just do `mvn clean install`.
To run a three node cluster execute the following:
```
java -DIGNITE_QUIET=false -DIGNITE_JETTY_PORT=8081 -jar target/ignite-cache*.jar
```

You can also use the Swagger-ui.html to test the application.

![alt text](A.png)

![alt text](B.png)

for more detailed technical information please check my post : https://medium.com/swlh/spring-cache-with-apache-ignite-def103cae35

