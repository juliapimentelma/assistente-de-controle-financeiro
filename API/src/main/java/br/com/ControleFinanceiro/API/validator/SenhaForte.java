package br.com.ControleFinanceiro.API.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = SenhaForteValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SenhaForte {

    String message() default "Senha muito fraca! Ela deve conter pelo menos 8 caracteres, incluindo letras maiúsculas, minúsculas e números.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
