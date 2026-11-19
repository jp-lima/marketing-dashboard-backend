# syntax=docker/dockerfile:1

# Etapa 1: build da aplicação com Maven Wrapper
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Copia os arquivos do Maven primeiro para aproveitar o cache de dependências
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline -B

# Copia o código-fonte e faz o build
COPY src ./src
RUN ./mvnw clean package -DskipTests -B

# Etapa 2: imagem final apenas com o JRE
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copia o jar gerado na etapa anterior
COPY --from=build /app/target/*.jar app.jar

# O Render define a porta pela variável de ambiente PORT
ENV PORT=8080
EXPOSE 8080

# Inicia a aplicação escutando na porta definida pelo Render
ENTRYPOINT ["sh", "-c", "java -jar /app/app.jar --server.port=${PORT}"]
