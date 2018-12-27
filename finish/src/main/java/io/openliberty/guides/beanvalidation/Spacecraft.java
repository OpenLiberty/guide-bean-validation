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
import java.util.Map;
import java.util.HashMap;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.AssertTrue;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.validation.Valid;

@Named
@RequestScoped
public class Spacecraft implements Serializable {

    private static final long serialVersionUID = 1L;

    @Valid
    private Astronaut astronaut;

    private Map<@NotBlank String, @Positive Integer> destinations;

    @SerialNumber
    private String serialNumber;

    public Spacecraft() {
        destinations = new HashMap<String, Integer>();
    }

    public void setAstronaut(Astronaut astronaut) {
        this.astronaut = astronaut;
    }

    public void setDestinations(Map<String,Integer> destinations) {
        this.destinations = destinations;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Astronaut getAstronaut() {
        return astronaut;
    }

    public Map<String, Integer> getDestinations() {
        return destinations;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    @AssertTrue
    public boolean launchSpacecraft(@NotNull String launchCode) {
        if(launchCode.equals("OpenLiberty"))
            return true;
        return false;
    }
}
