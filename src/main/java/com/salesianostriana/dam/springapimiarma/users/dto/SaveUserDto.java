package com.salesianostriana.dam.springapimiarma.users.dto;

import com.salesianostriana.dam.springapimiarma.users.model.ProfileType;
import com.salesianostriana.dam.springapimiarma.validacion.simple.anotaciones.StrongPassword;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@NoArgsConstructor @AllArgsConstructor
public class SaveUserDto {
    @NotBlank(message = "{userEntity.full_name.blank}")
    private String full_name;

    @Past(message = "{userEntity.fecha_nacimiento.past}")
    private LocalDate fecha_nacimiento;

    @NotBlank(message = "{userEntity.direccion.blank}")
    private String direccion;

    @NotBlank(message = "{userEntity.telefono.blank}")
    private String telefono;

    @NotBlank(message = "{userEntity.email.blank}")
    private String email;

    @NotBlank(message = "{userEntity.nickname.blank}")
    @Size(min = 2, max = 20, message = "{userEntity.nickname.size}")
    private String nickname;

    @URL
    private String avatar;

    @NotBlank(message = "{userEntity.privacidad.blank}")
    private ProfileType privacidad;
}
