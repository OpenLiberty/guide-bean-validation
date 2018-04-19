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
        targetUrl = "http://localhost:" + port + "/LibertyProject/servlet";
    }

    @Test
    public void testNoFieldLevelConstraintViolations() throws Exception {

    }

    @Test
    public void testFieldLevelConstraintViolation() throws Exception {

    }

    @Test
    public void testNoMethodLevelConstraintViolations() throws Exception {

    }

    @Test
    public void testMethodLevelConstraintViolation() throws Exception {

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
