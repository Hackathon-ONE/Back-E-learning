# Usar Java 17
FROM openjdk:17-jdk-alpine

# Crear directorio de la app
WORKDIR /app

# Copiar el JAR generado por Maven (asegúrate del nombre exacto)
COPY target/*.jar app.jar

# Exponer el puerto que usa tu app (8080 por defecto)
EXPOSE 8080

# Ejecutar la app
ENTRYPOINT ["java", "-jar", "app.jar"]
