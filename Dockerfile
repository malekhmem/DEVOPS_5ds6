FROM openjdk:17
ADD http://192.168.136.137:8081/repository/maven-releases/tn/esprit/spring/gestion-station-ski/2.0/gestion-station-ski-2.0.jar /app/app.jar
EXPOSE 8089
CMD ["java","-jar","/app/app.jar"]