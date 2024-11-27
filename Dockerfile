FROM openjdk:21-jdk

COPY target/kukmee.jar .

EXPOSE 8080

ENTRYPOINT ["java","-jar","/kukmee.jar"]