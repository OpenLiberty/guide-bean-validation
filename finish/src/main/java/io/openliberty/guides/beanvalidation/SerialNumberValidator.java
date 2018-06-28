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

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SerialNumberValidator implements ConstraintValidator<SerialNumber,
                                                                  Object> {

    @Override
    public boolean isValid(Object arg0, ConstraintValidatorContext arg1) {
        //Serial Numbers must start with Liberty followed by four numbers
        boolean isValid = false;
        if (arg0 == null)
            return isValid;
        String serialNumber = arg0.toString();
        isValid = serialNumber.length() == 11 && serialNumber.startsWith("Liberty");
        try {
            Integer.parseInt(serialNumber.substring(7));
        } catch (Exception ex) {
            isValid = false;
        }
        return isValid;
    }
}
