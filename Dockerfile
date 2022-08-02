FROM adoptopenjdk/openjdk11:ubi
ADD target/userapp-1.0.jar userapp.jar
ENTRYPOINT ["java","-jar","userapp.jar"]