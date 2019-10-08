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

import java.io.Serializable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Email;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
// tag::Astronaut[]
public class Astronaut implements Serializable {

    private static final long serialVersionUID = 1L;
    // tag:NotBlank[]
    @NotBlank
    // end::NotBlank[]
    // tag::name[]
    private String name;
    // end::end[] 
    // tag::Min[]
    @Min(18)
    // end::Min[]
    // tag::Max[]
    @Max(100)
    // end::Max[]
    // tag::age[]
    private Integer age;
    // end::age[]
    // tag::Email[]
    @Email
    // end::Email[]
    // tag::emailAddress[]
    private String emailAddress;
    // end::emailAddress[]

    public Astronaut() {}

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
  }
// end::Astronaut[]