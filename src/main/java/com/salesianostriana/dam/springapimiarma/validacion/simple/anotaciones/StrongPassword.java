package com.salesianostriana.dam.validacion.simple.anotaciones;

import com.salesianostriana.dam.validacion.simple.validadores.StrongPasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StrongPasswordValidator.class)
@Documented
public @interface StrongPassword {

    String message() default "La contraseña no cumple con los requisitos de validación especificados.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int min();
    int max();
    boolean hasUpper();
    boolean hasLower();
    boolean hasNumber();
    boolean hasAlpha();
    boolean hasOthers();
}
