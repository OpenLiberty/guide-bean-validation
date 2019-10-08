// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2018 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
// end::copyright[]
package io.openliberty.guides.beanvalidation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import javax.validation.Constraint;
import javax.validation.Payload;
// tag::Target[]
@Target({ FIELD })
// end::Target[]
// tag::Retention[]
@Retention(RUNTIME)
// end::Retention[]
@Documented
// tag::Constraint[]
@Constraint(validatedBy = { SerialNumberValidator.class })
// end::Constraint[]
// tag::SerialNumber[]
public @interface SerialNumber {
    // tag::message[]
    String message() default "serial number is not valid.";
    // end::message[]
    // tag::groups[]
    Class<?>[] groups() default {};
    // end::groups[]
    // tag::payload[]
    Class<? extends Payload>[] payload() default {};
    // end::payload[]
}
// end::SerialNumber[]