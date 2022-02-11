package com.salesianostriana.dam.springapimiarma.users.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor @AllArgsConstructor
@com.salesianostriana.dam.validacion.multiple.anotaciones.FieldsValueMatch.List({
        @com.salesianostriana.dam.validacion.multiple.anotaciones.FieldsValueMatch(
                field = "password",
                fieldMatch = "verifyPassword",
                message = "Las contraseñas no coinciden"
        ),
        @com.salesianostriana.dam.validacion.multiple.anotaciones.FieldsValueMatch(
                field = "email",
                fieldMatch = "verifyEmail",
                message = "Las direcciones de correo electrónico no coinciden"
        )
})
public class CreateUserDto {

    private String full_name;
    private LocalDateTime fecha_nacimiento;
    private String direccion;
    private String telefono;
    private String email;
    private String nickname;
    private String avatar;
    @NotEmpty
    @com.salesianostriana.dam.validacion.simple.anotaciones.StrongPassword(message = "{usuario.password.strong}", min = 5, max = 15, hasUpper = true, hasLower = true, hasNumber = true, hasAlpha = true, hasOthers = true)
    private String password;
    private String password2;

}
