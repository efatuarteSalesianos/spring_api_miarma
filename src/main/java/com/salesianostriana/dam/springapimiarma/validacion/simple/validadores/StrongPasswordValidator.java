package com.salesianostriana.dam.validacion.simple.validadores;

import com.salesianostriana.dam.validacion.simple.anotaciones.StrongPassword;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

    private int min;
    private int max;
    private boolean hasUpper;
    private boolean hasLower;
    private boolean hasNumber;
    private boolean hasAlpha;
    private boolean hasOthers;

    @Override
    public void initialize(StrongPassword constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
        this.hasUpper = constraintAnnotation.hasUpper();
        this.hasLower = constraintAnnotation.hasLower();
        this.hasNumber = constraintAnnotation.hasNumber();
        this.hasAlpha = constraintAnnotation.hasAlpha();
        this.hasOthers = constraintAnnotation.hasOthers();
    }

    @Override
    public boolean isValid(String pass, ConstraintValidatorContext constraintValidatorContext) {
        String passPattern="";

        if (!hasAlpha){
            hasLower = false;
            hasUpper = false;
            passPattern = passPattern+"(?!.[a-z])(?!.[A-Z])";
        }

        if (hasLower) {
            passPattern = passPattern+"(?=.[a-z])";
        }
        if (hasUpper) {
            passPattern = passPattern+"(?=.[A-Z])";
        }
        if (hasNumber) {
            passPattern = passPattern+"(?=.[0-9])";
        }
        if (hasOthers) {
            passPattern = passPattern+"(?=.[.,$_-])";
        }

        passPattern = "^"+passPattern+".{"+min+","+max+"}"+"$";

        Pattern pattern = Pattern.compile(passPattern);
        Matcher matcher = pattern.matcher(pass);

        return matcher.matches();
    }
}