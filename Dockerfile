# Build stage
FROM gradle:8.14.3-jdk17 AS builder
WORKDIR /home/gradle/project
COPY --chown=gradle:gradle . .

# ✅ BuildKit 캐시 + 테스트 스킵
RUN --mount=type=cache,target=/home/gradle/.gradle \
    gradle clean build -x test --no-daemon

# Run stage
FROM openjdk:17-jdk-slim
WORKDIR /app

# ✅ 빌드된 JAR를 와일드카드로 복사 (이름 바뀌어도 안전)
COPY --from=builder /home/gradle/project/build/libs/*SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
