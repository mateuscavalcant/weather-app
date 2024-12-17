FROM eclipse-temurin:17-jre-jammy as runtime

# Define um diretório de trabalho para a aplicação
WORKDIR /app

# Copia o JAR da aplicação diretamente para o diretório de trabalho
COPY target/*.jar app.jar

# Usa um usuário não root para maior segurança
RUN useradd -m appuser
USER appuser

# Ccomando de entrada para executar a aplicação
CMD ["java", "-jar", "app.jar"]
