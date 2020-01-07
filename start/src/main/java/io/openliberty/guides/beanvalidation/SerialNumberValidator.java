package io.openliberty.guides.beanvalidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SerialNumberValidator 
    implements ConstraintValidator<SerialNumber,Object> {

    @Override
    public boolean isValid(Object arg0, ConstraintValidatorContext arg1) {
        boolean isValid = true;
        return isValid;
    }
}
