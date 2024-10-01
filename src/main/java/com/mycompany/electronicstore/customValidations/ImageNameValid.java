package com.mycompany.electronicstore.customValidations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ImageNameValidator.class)
public @interface ImageNameValid {

    String message() default "Invalid Image Name";

    // represent group of constraints
    Class<?>[] groups() default {};

    // additional info about annotations
    Class<? extends Payload>[] payload() default {};

}
