// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2018, 2019 IBM Corporation and others.
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

import java.util.Set;

import javax.inject.Inject;
import javax.validation.Validator;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

@Path("/")
public class BeanValidationEndpoint {
	
    @Inject
    Validator validator;
    
    @Inject
    Spacecraft bean;

    @POST
    @Path("/validatespacecraft")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "POST request to validate your spacecraft bean")
    public String validateSpacecraft(
                @RequestBody(description = "Specify the values to create the "
           		+ "Astronaut and Spacecraft beans.", 
           	    content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = Spacecraft.class)))
                Spacecraft spacecraft) {	
				
        Set<ConstraintViolation<Spacecraft>> violations
        = validator.validate(spacecraft);
		
        if(violations.size() == 0) {
            return "No Constraint Violations";
        }
				
        StringBuilder sb = new StringBuilder();
        for(ConstraintViolation<Spacecraft> violation : violations) {
            sb.append("Constraint Violation Found: ").append(violation.getMessage())
            .append(System.lineSeparator());
        }
        return sb.toString();
    }
	
    @POST
    @Path("/launchspacecraft")
    @Produces(MediaType.TEXT_PLAIN)
    @Operation(summary = "POST request to specify a launch code")
    public String launchSpacecraft(
            @RequestBody(description = "Enter the launch code.  Must not be "
            		+ "null and must equal OpenLiberty for successful launch.",
            content = @Content(mediaType = "text/plain"))
            String launchCode) {	
        try {
            bean.launchSpacecraft(launchCode);
            return "launched";
        } catch(ConstraintViolationException ex) {
            return ex.getMessage();
        }
    }
}
