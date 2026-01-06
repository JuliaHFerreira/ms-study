FROM eclipse-temurin:21
COPY ./target/user-0.0.1-SNAPSHOT.jar /app/user-0.0.1-SNAPSHOT.jar
WORKDIR /app
ENTRYPOINT ["java", "-jar", "user-0.0.1-SNAPSHOT.jar"]
EXPOSE 8081

#CRIANDO A IMAGEM DOCKER DA MINHA APLICAÇÃO