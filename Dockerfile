FROM gradle:8-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle shadowJar --no-daemon

FROM openjdk:11 AS runner
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/MechatechKt*all.jar /app/MechatechKt.jar

ENTRYPOINT ["java","-jar","/app/MechatechKt.jar"]