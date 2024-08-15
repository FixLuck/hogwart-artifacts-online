FROM eclipse-temurin:17-jre as builder
WORKDIR application
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM eclipse-temurin:17-jre
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]

FROM mcr.microsoft.com/mssql/server:2019-latest
ENV SA_PASSWORD=sa123456
ENV ACCEPT_EULA=Y
COPY ./userdb.bak /var/opt/mssql/backup/

CMD /opt/mssql/bin/sqlservr & \
    /opt/mssql-tools/bin/sqlcmd -S localhost -U SA -P "sa123456" -Q "RESTORE DATABASE UserDB FROM DISK='/var/opt/mssql/backup/userdb.bak' WITH REPLACE"

EXPOSE 8080

