FROM maven:3.9.11-eclipse-temurin-17 AS builder

WORKDIR /workspace

COPY pom.xml ./
RUN mvn -B -q dependency:go-offline

COPY src ./src
RUN mvn -B -q -DskipTests package

FROM eclipse-temurin:17-jre-jammy

RUN groupadd --system securemarket \
    && useradd --system --gid securemarket --home-dir /app securemarket

WORKDIR /app

COPY --from=builder --chown=securemarket:securemarket \
    /workspace/target/secure-market-*.jar /app/application.jar

USER securemarket

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/application.jar"]
