FROM openjdk:21-jdk-bullseye as BUILD

WORKDIR /app

COPY src /app/src
COPY pom.xml /app
COPY .mvn /app/.mvn
COPY mvnw /app/mvnw

RUN ./mvnw clean package -DskipTests

FROM openjdk:21-jdk-bullseye as RUNTIME

COPY --from=BUILD /app/target/jaiminho-jar-with-dependencies.jar /rinha.jar

EXPOSE 8080

ENTRYPOINT [ "java","-jar","-Xmx165M","-Xms50M","-Xss156k","./rinha.jar" ]
