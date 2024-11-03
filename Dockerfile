FROM openjdk:17
ADD http://192.168.1.21:8081/repository/maven-releases/tn/esprit/spring/gestion-station-ski/1.0/gestion-station-ski-1.0.jar /app/app.jar
EXPOSE 8089
CMD ["java","-jar","/app/app.jar"]
