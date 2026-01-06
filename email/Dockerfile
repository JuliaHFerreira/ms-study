FROM eclipse-temurin:21
COPY ./target/email-0.0.1-SNAPSHOT.jar /app/email-0.0.1-SNAPSHOT.jar
WORKDIR /app
ENTRYPOINT ["java", "-jar", "email-0.0.1-SNAPSHOT.jar"]
EXPOSE 8082

#CRIANDO A IMAGEM DOCKER DA MINHA APLICAÇÃO