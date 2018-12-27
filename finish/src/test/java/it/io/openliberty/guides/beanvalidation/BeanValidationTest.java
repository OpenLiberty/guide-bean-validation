// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2018 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
 // end::copyright[]
package it.io.openliberty.guides.beanvalidation;

import org.junit.Test;

import io.openliberty.guides.beanvalidation.Astronaut;
import io.openliberty.guides.beanvalidation.Spacecraft;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.HashMap;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;

public class BeanValidationTest {

	private Client client;
	private static String port;

    @Before
    public void setup() {
    	client = ClientBuilder.newClient();
        port = System.getProperty("liberty.test.port");
    }
    
    @After
    public void teardown() {
        client.close();
    }

    @Test
    public void testNoFieldLevelConstraintViolations() throws Exception {
        Astronaut astronaut = new Astronaut();
        astronaut.setAge(25);
        astronaut.setEmailAddress("libby@openliberty.io");
        astronaut.setName("Libby");
        
        Spacecraft spacecraft = new Spacecraft();
        spacecraft.setAstronaut(astronaut);
        spacecraft.setSerialNumber("Liberty1001");
        
    	HashMap<String, Integer> destinations = new HashMap<String, Integer>();
    	destinations.put("Mars", 1500);
    	destinations.put("Pluto", 10000);
        spacecraft.setDestinations(destinations);
        
        Jsonb jsonb = JsonbBuilder.create();
        String spacecraftJSON = jsonb.toJson(spacecraft);
        Response response = postResponse(getURL(port, "validatespacecraft"), 
                spacecraftJSON, false);
        String actualResponse = response.readEntity(String.class);
        String expectedResponse = "No Constraint Violations";
        
        assertEquals("Unexpected response when validating beans.", 
                expectedResponse, actualResponse);
    }

    @Test
    public void testFieldLevelConstraintViolation() throws Exception {
        Astronaut astronaut = new Astronaut();
        astronaut.setAge(25);
        astronaut.setEmailAddress("libby");
        astronaut.setName("Libby");
        
        Spacecraft spacecraft = new Spacecraft();
        spacecraft.setAstronaut(astronaut);
        spacecraft.setSerialNumber("Liberty123");
        
        HashMap<String, Integer> destinations = new HashMap<String, Integer>();
    	destinations.put("Mars", -100);
        spacecraft.setDestinations(destinations);
        
        Jsonb jsonb = JsonbBuilder.create();
        String spacecraftJSON = jsonb.toJson(spacecraft);
        Response response = postResponse(getURL(port, "validatespacecraft"), 
                spacecraftJSON, false);
        String actualResponse = response.readEntity(String.class);
        
        String expectedDestinationResponse = "must be greater than 0";
        assertTrue("Expected response to contain: " + expectedDestinationResponse,
        		 actualResponse.contains(expectedDestinationResponse));
        
        String expectedEmailResponse = "must be a well-formed email address";
        assertTrue("Expected response to contain: " + expectedEmailResponse,
       		 actualResponse.contains(expectedEmailResponse));

        String expectedSerialNumberResponse = "serial number is not valid";
        assertTrue("Expected response to contain: " + expectedSerialNumberResponse,
          		 actualResponse.contains(expectedSerialNumberResponse));
    }

    @Test
    public void testNoMethodLevelConstraintViolations() throws Exception {
        String launchCode = "OpenLiberty";
        Response response = postResponse(getURL(port, "launchspacecraft"), 
                launchCode, true);
        
        String actualResponse = response.readEntity(String.class);
        String expectedResponse = "launched";
        
        assertEquals("Unexpected response from call to launchSpacecraft", 
                expectedResponse, actualResponse);
       
    }

    @Test
    public void testMethodLevelConstraintViolation() throws Exception {
        String launchCode = "incorrectCode";
        Response response = postResponse(getURL(port, "launchspacecraft"), 
                launchCode, true);
        
        String actualResponse = response.readEntity(String.class);
        assertTrue("Unexpected response from call to launchSpacecraft",
        actualResponse.contains("must be true"));
    }
    
    private Response postResponse(String url, String value, 
                                  boolean isMethodLevel) {
    	if(isMethodLevel)
            return client.target(url).request().post(Entity.text(value));
    	else
    		return client.target(url).request().post(Entity.entity(value, 
    		        MediaType.APPLICATION_JSON));
    }

    private String getURL(String port, String function) {
    	return "http://localhost:" + port + "/Spacecraft/beanvalidation/" + 
                function;
    }
}
