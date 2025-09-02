# Back-E-learning  
Plataforma de aprendizaje electrónico

---

## 📁 Estructura de carpetas sugerida

```
src
└─ main
   ├─ java
   │  └─ learning
   │        └─ platform
   │           ├─ config                 # Configuración general, seguridad, CORS, JWT, Swagger
   │           │   ├─ SecurityConfig.java
   │           │   ├─ JwtFilter.java
   │           │   └─ SwaggerConfig.java
   │           │
   │           ├─ controller             # REST controllers (endpoints)
   │           │   ├─ AuthController.java
   │           │   ├─ UserController.java
   │           │   ├─ CourseController.java
   │           │   ├─ LessonController.java
   │           │   ├─ EnrollmentController.java
   │           │   ├─ ProgressController.java
   │           │   └─ PaymentController.java
   │           │
   │           ├─ dto                    # Data Transfer Objects
   │           │   ├─ UserDTO.java
   │           │   ├─ CourseDTO.java
   │           │   ├─ LessonDTO.java
   │           │   └─ ...
   │           │
   │           ├─ entity                 # JPA Entities / Domain Model
   │           │   ├─ User.java
   │           │   ├─ Course.java
   │           │   ├─ Lesson.java
   │           │   ├─ Enrollment.java
   │           │   ├─ Progress.java
   │           │   └─ Payment.java
   │           │
   │           ├─ repository             # Spring Data JPA Repositories
   │           │   ├─ UserRepository.java
   │           │   ├─ CourseRepository.java
   │           │   └─ ...
   │           │
   │           ├─ service                # Lógica de negocio / Services
   │           │   ├─ UserService.java
   │           │   ├─ CourseService.java
   │           │   ├─ learning.platform.service.LessonService.java
   │           │   └─ ...
   │           │
   │           ├─ mapper                 # MapStruct o ModelMapper
   │           │   ├─ UserMapper.java
   │           │   ├─ CourseMapper.java
   │           │   └─ ...
   │           │
   │           ├─ exception              # Manejo de errores
   │           │   ├─ GlobalExceptionHandler.java
   │           │   └─ CustomExceptions.java
   │           │
   │           ├─ util                   # Utilidades
   │           │   ├─ JWTUtil.java
   │           │   ├─ EmailService.java
   │           │   └─ FileService.java
   │           │
   │           └─ Application.java  # Clase main de Spring Boot
   │
   └─ resources
      ├─ application.properties
      ├─ application-dev.properties
      ├─ application-prod.properties
      └─ static / templates / uploads  # Archivos estáticos, plantillas, uploads locales
```

---

## 🔹 Puntos clave de esta estructura

- **Capas claras:**  
  Controller → Service → Repository → Entity/DB

- **DTO ↔ Mapper:**  
  Separa la persistencia de la API.

- **Seguridad aislada:**  
  JWT, CORS, CSRF, roles → todo en `config`.

- **Documentación:**  
  Swagger/OpenAPI en `config/SwaggerConfig.java`.

- **Archivos y multimedia:**  
  Carpeta `uploads` dentro de `resources` para dev local, o servicio externo (S3/OCI) en producción.

- **Tests:**  
  Unit tests: `src/test/java/...` replicando la misma

## 🚀 Pasos posteriores al clonado
- El archivo `application.properties` ya está configurado en el proyecto.
- Debes crear la base de datos en tu servidor local con el nombre indicado en `application.properties`.
- Actualiza el archivo con tu usuario y contraseña de la base de datos.
