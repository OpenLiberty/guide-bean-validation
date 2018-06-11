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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.net.URL;
import java.net.HttpURLConnection;
import org.junit.BeforeClass;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class BeanValidationTest {

    private static String port;
    private static String targetUrl;

    @BeforeClass
        public static void oneTimeSetup() {
        port = System.getProperty("liberty.test.port");
        targetUrl = "http://localhost:" + port + "/Spacecraft/servlet";
    }

    @Test
    public void testNoFieldLevelConstraintViolations() throws Exception {
        String validationType = "FieldLevelValidation";
        String destinations = "Mars,1500,Pluto,10000";
        String age = "25";
        String emailAddress = "libby@openliberty.io";
        String name = "Libby";
        String serialNumber = "Liberty1001";

        String url = targetUrl.concat("?validationType="+ validationType + "&destinations=" +
                                      destinations + "&age=" + age + "&emailAddress=" +
                                      emailAddress + "&name=" + name +"&serialNumber=" +
                                      serialNumber);
        HttpURLConnection con = testRequestHelper(url);
        assertEquals("Incorrect response code from " + url, 200, con.getResponseCode());
        String response = testBufferHelper(con);
        assertTrue("Incorrect response from " + url + ". Response: " + response,
                   response.contains("No constraint violations found."));
    }

    @Test
    public void testFieldLevelConstraintViolation() throws Exception {
        String validationType = "FieldLevelValidation";
        String destinations = "Mars,-100";
        String age = "25";
        String emailAddress = "libby";
        String name = "Libby";
        String serialNumber = "Liberty123";

        String url = targetUrl.concat("?validationType="+ validationType + "&destinations=" +
                                      destinations + "&age=" + age + "&emailAddress=" +
                                      emailAddress + "&name=" + name + "&serialNumber=" +
                                      serialNumber);
        HttpURLConnection con = testRequestHelper(url);
        assertEquals("Incorrect response code from " + url, 200, con.getResponseCode());
        String response = testBufferHelper(con);
        assertTrue("Incorrect response from " + url + ". Response: " + response,
                   response.contains("must be greater than 0"));
        assertTrue("Incorrect response from " + url + ". Response: " + response,
                   response.contains("must be a well-formed email address"));
        assertTrue("Incorrect response from " + url + ". Response: " + response,
                   response.contains("serial number is not valid"));
    }

    @Test
    public void testNoMethodLevelConstraintViolations() throws Exception {
        String validationType = "MethodLevelValidation";
        String launchCode = "OpenLiberty";
        String url = targetUrl.concat("?validationType="+ validationType +
                                      "&launchCode=" + launchCode);
        HttpURLConnection con = testRequestHelper(url);
        assertEquals("Incorrect response code from " + url, 200, con.getResponseCode());
        String response = testBufferHelper(con);
        assertTrue("Incorrect response from " + url + ". Response: " + response,
                   response.contains("No constraint violations found."));
    }

    @Test
    public void testMethodLevelConstraintViolation() throws Exception {
        String validationType = "MethodLevelValidation";
        String launchCode = "incorrectCode";
        String url = targetUrl.concat("?validationType="+ validationType +
                                      "&launchCode=" + launchCode);
        HttpURLConnection con = testRequestHelper(url);
        assertEquals("Incorrect response code from " + url, 200, con.getResponseCode());
        String response = testBufferHelper(con);
        assertTrue("Incorrect response from " + url + ". Response: " + response,
                   response.contains("must be true"));
    }

    private HttpURLConnection testRequestHelper(String url) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        return con;
    }

    private String testBufferHelper(HttpURLConnection con) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}
