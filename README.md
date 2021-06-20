# MyRestAPI
A simple API demo project. (with mssql, redis, rabbitMQ)

# Introduction
This project was generated with spring boot 2 and hibernate 5.
It's a really easy questionnaire system only for the demo of my Rest API and coding style.
### IDE
IntelliJ IDEA
### Build
Maven
### Language
JAVA
### Run
JAR
### DB
MSSQL
### API Documentation
Swagger 3

# Detail
1. There are some file upload demo on it.
2. It has been connected to Redis for cache to save some object that will be called continuously and session to save user info.
3. I tried to combine a message broker. Finally, I choose rabbitMQ rather than redis because it can make message persistent.
