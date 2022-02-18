# Miarmagram
## Proyecto Realizado por Ernesto Fatuarte

Miarmagram es una red social para que los más sevillanos puedan subir imágenes y vídeos de sus mejores vivencias en esta nuestra ciudad.

El back-end ha sido diseñado con Java17 y Spring Boot, al cual hemos añadido la dependencia de Spring Security.

## Diagrama de clase
![Diagrama de Clases](https://github.com/efatuarteSalesianos/spring_api_miarma/blob/main/DiagramaClases_MiarmaAPI.png)

## Instalación

Para probar la API de este repositorio, ejecuta los siguientes comandos:
- Clonar el repositorio que se encuentra en la dirección:

  ``` https://github.com/efatuarteSalesianos/spring_api_miarma.git ```
- Entra en el directorio del proyecto:

  ``` cd spring_miarma_app/miarmagram ```
- Lanza el servidor para ver la aplicación en http://localhost:8080/:

  ``` mvn spring-boot:run ```

## Utilidades
Existe en el proyecto una carpeta ```/utils``` donde encontraremos una colección de ficjeros .json 
e imágenes que deberán adjuntarse a las diferentes peticiones:
- Los ficheros .json dentro del directorio ```utils/json/user``` serán utilizados para crear o editar un usuario (según el nombre que tenga dicho fichero)
- Las imágenes dentro del directorio ```utils/media/avatar``` las usaremos como adjunto a las peticiones de crear y editar usuarios.
- Los ficheros .json dentro del directorio ```utils/json/user``` serán utilizados para crear o editar un usuario (según el nombre que tenga dicho fichero)
- Las imágenes dentro del directorio ```utils/media/post``` las usaremos como adjunto a las peticiones de crear y editar posts.

## EndPoints

### Auth

| Verbo | Url            | Descripción                                                                                                                                                                                                                                                                                                                                               |
|-------|----------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| POST  | /auth/register | Un usuario cualquiera podrá registrarse para comenzar a usar nuestra red social. Al hacerlo, los datos (como mínimo) que debería aportar son su nick, email, fecha de nacimiento y un fichero con su foto o avatar. Los usuarios pueden elegir si su perfil de usuario es público o privado. Si es privado, solamente lo podrán visualizar sus seguidores |
| POST  | /auth/signin   | El usuario podrá loguearse, obteniendo un token para poder acceder al resto de endpoints                                                                                                                                                                                                                                                                  |
| GET   | /users/me      | Obtener los datos del usuario logueado, incluyendo la URL del avatar                                                                                                                                                                                                                                                                                      |

### Post

| Verbo  | Url          | Descripción                                                                                                                                                                                                                          | 
|--------|--------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| POST   | /post/       | Crear una nueva publicación. Una publicación tendrá al menos un título, un texto, y un fichero adjunto (imagen o vídeo). También habrá que indicar si nuestra publicación es pública o solamente accesible para nuestros seguidores. |
| PUT    | /post/{id}   | Modificar una publicación. Se podrán cambiar tanto la imagen o vídeo (eliminando por tanto la anterior), como los campos textuales.                                                                                                  |
| DELETE | /post/{id}   | Eliminar una publicación, eliminando a su vez el fichero adjunto.                                                                                                                                                                    |
| GET    | /post/public | Obtener todas las publicaciones públicas de todos los usuarios                                                                                                                                                                       |
| GET    | /post/{id}   | Obtener una publicación por su ID. Solamente la podremos ver si está marcada como pública o si nuestro usuario sigue al autor de la publicación.                                                                                     |
| GET    | /post/{nick} | Obtener todas las publicaciones de un usuario. Si no seguimos al usuario, solamente podremos ver las publicaciones guardadas como públicas. Si seguimos al usuario, podremos ver todas las publicaciones.                            |
| GET    | /post/me     | Obtener todas las publicaciones del usuario logueado                                                                                                                                                                                 |

### Usuarios

| Verbo | Url                  | Descripción                                                                   | 
|-------|----------------------|-------------------------------------------------------------------------------|
| GET   | /profile/{id}        | Update user (If profile belongs to logged in user or logged in user is admin) |
| PUT   | /profile/{me}        | Delete user (For logged in user or admin)                                     |
| POST  | /follow/{nick}       | Create new post (By logged in user)                                           |
| POST  | /follow/accept/{id}  | Update post (If post belongs to logged in user or logged in user is admin)    | 
| POST  | /follow/decline/{id} | Delete post (If post belongs to logged in user or logged in user is admin)    |
| GET   | /follow/list         | Delete post (If post belongs to logged in user or logged in user is admin)    |

## Lenguajes

Para este proyecto hemos usado:
- Java
- Spring Boot
- SQL y JPQL

## Dependencias

Para este proyecto hemos instalado las siguientes dependencias:
- Spring JPA
- H2 Database
- Lombok
- Spring Web
- Spring Security
- Spring Validation