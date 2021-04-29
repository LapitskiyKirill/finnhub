FROM openjdk:11
COPY target/finnhub-2.4.4.jar finnhub-2.4.4.jar
ENTRYPOINT ["java","-jar","/finnhub-2.4.4.jar"]