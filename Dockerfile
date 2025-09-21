LABEL authors="Francisco"

# Build
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests clean package

# Runtime
FROM eclipse-temurin:21-jre
WORKDIR /app
# ajuste o nome do jar gerado, se for diferente
COPY --from=build /app/target/*.jar /app/app.jar

# Cloud Run expõe a porta via variável PORT
ENV PORT=8080
EXPOSE 8080

CMD ["sh", "-c", "java -Dserver.port=${PORT} -jar /app/app.jar"]
