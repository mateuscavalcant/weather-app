FROM eclipse-temurin:17-jre-jammy as runtime

# Define o diretório de trabalho
WORKDIR /app

# Copia o JAR da aplicação
COPY target/*.jar app.jar

# Cria um usuário seguro
RUN useradd -m appuser
USER appuser

# Expõe a porta do Spring Boot
EXPOSE 8080

# Comando de entrada
CMD ["java", "-jar", "app.jar"]
