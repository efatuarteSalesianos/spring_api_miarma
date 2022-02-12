package com.salesianostriana.dam.springapimiarma.users.controllers.dto;

import com.salesianostriana.dam.springapimiarma.users.model.ProfileType;
import com.salesianostriana.dam.springapimiarma.validacion.multiple.anotaciones.FieldsValueMatch;
import com.salesianostriana.dam.springapimiarma.validacion.simple.anotaciones.StrongPassword;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
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

    @NotBlank(message = "{userEntity.nickname.blank}")
    @Size(min = 2, max = 20, message = "{userEntity.nickname.size}")
    private String nickname;

    @URL
    private String avatar;

    @NotEmpty(message = "{userEntity.password.empty}")
    @StrongPassword(message = "{userEntity.password.strong}", min = 5, max = 15, hasUpper = true, hasLower = true, hasNumber = true, hasAlpha = true, hasOthers = true)
    private String password;
    private String verifyPassword;

    @NotBlank(message = "{userEntity.privacidad.blank}")
    private ProfileType privacidad;

}
