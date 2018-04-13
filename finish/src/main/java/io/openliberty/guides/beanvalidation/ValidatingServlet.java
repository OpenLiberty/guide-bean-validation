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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.annotation.Resource;
import javax.inject.Inject;

import io.openliberty.guides.beanvalidation.Astronaut;
import io.openliberty.guides.beanvalidation.Spacecraft;

@WebServlet(urlPatterns = "/servlet")
public class ValidatingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Resource
    Validator validator;

    @Inject
    Spacecraft bean;

    public void processInputData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String validationType = request.getParameter("validationType");
        if(validationType.equalsIgnoreCase("FieldLevelValidation")) {
            Spacecraft spacecraft = new Spacecraft();
            Astronaut astronaut = new Astronaut();

            astronaut.setAge(Integer.parseInt(request.getParameter("age")));
            astronaut.setEmailAddress(request.getParameter("emailAddress"));
            astronaut.setName(request.getParameter("name"));

            HashMap<String, Integer> destinations = createDestinationsMap(request.getParameter("destinations"));

            spacecraft.setDestinations(destinations);
            spacecraft.setAstronaut(astronaut);
            spacecraft.setSerialNumber(request.getParameter("serialNumber"));

            Set<ConstraintViolation<Spacecraft>> violations = validator.validate(spacecraft);
            PrintWriter out = response.getWriter();
            if (!violations.isEmpty()) {
                for (ConstraintViolation<Spacecraft> violation : violations) {
                    out.println("Constraint Violation Found: " + violation);
                }
            } else {
                out.println("No constraint violations found.");
            }
            out.close();
        } else if(validationType.equalsIgnoreCase("MethodLevelValidation")) {
            try {
                bean.launchSpacecraft(request.getParameter("launchCode"));
                response.getWriter().println("No constraint violations found.");
            } catch(ConstraintViolationException ex) {
                response.getWriter().println(ex);
            }
        }  else {
            response.getWriter().println("No validation type specified.");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processInputData(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processInputData(request, response);
    }

    //Create a map of destination/distance pairs from a comma delineated string.
    private HashMap<String, Integer> createDestinationsMap(String destinations) {
        HashMap<String, Integer> destinationList = new HashMap<String, Integer>();
        String[] split = destinations.split(",");

        for(int i = 0; i < split.length; i=i+2) {
          destinationList.put(split[i], Integer.valueOf(split[i+1]));
        }
        return destinationList;
      }
}
