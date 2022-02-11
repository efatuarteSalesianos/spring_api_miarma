package com.salesianostriana.dam.springapimiarma.users.dto;

import com.salesianostriana.dam.springapimiarma.validacion.multiple.anotaciones.FieldsValueMatch;
import com.salesianostriana.dam.springapimiarma.validacion.simple.anotaciones.StrongPassword;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor @AllArgsConstructor
@FieldsValueMatch.List({
        @FieldsValueMatch(
                field = "password",
                fieldMatch = "verifyPassword",
                message = "Las contraseñas no coinciden"
        ),
        @FieldsValueMatch(
                field = "email",
                fieldMatch = "verifyEmail",
                message = "Las direcciones de correo electrónico no coinciden"
        )
})
public class CreateUserDto {

    @NotBlank(message = "{userEntity.full_name.blank}")
    private String full_name;

    @Past(message = "{userEntity.fecha_nacimiento.past}")
    private LocalDateTime fecha_nacimiento;

    @NotBlank(message = "{userEntity.direccion.blank}")
    private String direccion;

    @NotBlank(message = "{userEntity.telefono.blank}")
    private String telefono;

    @NotBlank(message = "{userEntity.email.blank}")
    private String email;
    private String verifyEmail;

    private String nickname;

    @URL
    private String avatar;
    @NotEmpty
    @StrongPassword(message = "{usuario.password.strong}", min = 5, max = 15, hasUpper = true, hasLower = true, hasNumber = true, hasAlpha = true, hasOthers = true)
    private String password;
    private String verifyPassword;

}
