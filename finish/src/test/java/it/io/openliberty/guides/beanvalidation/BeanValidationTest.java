// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2018, 2019 IBM Corporation and others.
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

    // tag::Before[]
    @Before
    // end::Before[]
    // tag::setup[]
    public void setup() {
        // tag::Client[]
        client = ClientBuilder.newClient();
        // end::Client[]
        port = System.getProperty("liberty.test.port");
    }
    // end::setup[]

    @After
    public void teardown() {
        client.close();
    }

    @Test
    // tag::testNoFieldLevelConstraintViolations[]
    public void testNoFieldLevelConstraintViolations() throws Exception {
        // tag::Astronaut[]
        Astronaut astronaut = new Astronaut();
        astronaut.setAge(25);
        astronaut.setEmailAddress("libby@openliberty.io");
        astronaut.setName("Libby");
        // end::Astronaut[]
        // tag::Spacecraft[]
        Spacecraft spacecraft = new Spacecraft();
        spacecraft.setAstronaut(astronaut);
        spacecraft.setSerialNumber("Liberty1001");
        // end::Spacecraft[]
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
    // end::testNoFieldLevelConstraintViolations[]

    @Test
    // tag::testFieldLevelConstraintViolation[]
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
        // tag::Response[]
        Response response = postResponse(getURL(port, "validatespacecraft"), 
                spacecraftJSON, false);
        // end::Response[]
        String actualResponse = response.readEntity(String.class);
        // tag::expectedDestinationResponse[]
        String expectedDestinationResponse = "must be greater than 0";
        // end::expectedDestinationResponse[]
        assertTrue("Expected response to contain: " + expectedDestinationResponse,
                actualResponse.contains(expectedDestinationResponse));
        // tag::expectedEmailResponse[]
        String expectedEmailResponse = "must be a well-formed email address";
        // end::expectedEmailResponse[]
        assertTrue("Expected response to contain: " + expectedEmailResponse,
                actualResponse.contains(expectedEmailResponse));
        // tag::expectedSerialNumberResponse[]
        String expectedSerialNumberResponse = "serial number is not valid";
        // end::expectedSerialNumberResponse[]
        assertTrue("Expected response to contain: " + expectedSerialNumberResponse,
                actualResponse.contains(expectedSerialNumberResponse));
    }
    // end::testFieldLevelConstraintViolation[]

    @Test
    // tag::testNoMethodLevelConstraintViolations[]
    public void testNoMethodLevelConstraintViolations() throws Exception {
        // tag::OpenLiberty[]
        String launchCode = "OpenLiberty";
        // end::OpenLiberty[]
        // tag::launchSpacecraft[]
        Response response = postResponse(getURL(port, "launchspacecraft"), 
                launchCode, true);
        // end::launchSpacecraft[]
        
        String actualResponse = response.readEntity(String.class);
        String expectedResponse = "launched";
        
        assertEquals("Unexpected response from call to launchSpacecraft", 
                expectedResponse, actualResponse);
       
    }
    // end::testNoMethodLevelConstraintViolations[]

    // tag::testMethodLevelConstraintViolation[]
    @Test
    public void testMethodLevelConstraintViolation() throws Exception {
        // tag::incorrectCode[]
        String launchCode = "incorrectCode";
        // end::incorrectCode[]
        Response response = postResponse(getURL(port, "launchspacecraft"), 
                launchCode, true);
        
        String actualResponse = response.readEntity(String.class);
        assertTrue("Unexpected response from call to launchSpacecraft",
        // tag::actualResponse[]
        actualResponse.contains("must be true"));
        // end::actualResponse[]
    }
    // end::testMethodLevelConstraintViolation[]
    
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
