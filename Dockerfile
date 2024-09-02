# Usa una imagen base de OpenJDK 17
FROM openjdk:17-jdk-slim AS build

# Establece un directorio de trabajo dentro del contenedor
WORKDIR /build

# Copia los archivos de tu proyecto al directorio de trabajo
COPY . /build

# Ejecuta Maven para compilar y empaquetar el proyecto (asumiendo que usas Maven)
RUN ./mvnw clean package -DskipTests

# Etapa de producción
FROM openjdk:17-jdk-slim

# Establece un directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el JAR generado en la etapa de construcción
COPY --from=build /build/target/UserRegisterTest-0.0.1-SNAPSHOT.jar /app/UserRegisterTest-0.0.1-SNAPSHOT.jar

# Expone el puerto en el que tu aplicación va a correr
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "/app/UserRegisterTest-0.0.1-SNAPSHOT.jar"]
