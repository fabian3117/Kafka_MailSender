#FROM openjdk:17-jdk-alpine AS builder
#WORKDIR /app
#COPY src .

FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080 9092

ENTRYPOINT ["java","-jar","/app.jar"]

#COPY --from=builder /app/build/libs/*.jar app.jar
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","/app/app.jar"]

#RUN gradlew clean build
#RUN ./gradlew wrapper
#RUN ./gradlew clean build

#FROM openjdk:17-jdk-alpine
#WORKDIR /app
#COPY --from=build /build/libs/demo2-0.0.1-SNAPSHOT.jar app.jar
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","/app/app.jar"]
