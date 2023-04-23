# Use a imagem oficial do OpenJDK como base
FROM openjdk:11-jre-slim

# Defina o diretório de trabalho
WORKDIR /app

# Copie o arquivo JAR gerado pelo Maven
COPY target/my-app-1.0-SNAPSHOT.jar /app/my-app.jar

# Expõe a porta em que a aplicação será executada
EXPOSE 9090

# Define o comando padrão para executar a aplicação
CMD ["java", "-jar", "my-app.jar"]
