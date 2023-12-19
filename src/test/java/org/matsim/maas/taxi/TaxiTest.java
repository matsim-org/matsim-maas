/* *********************************************************************** *
 * project: org.matsim.*
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2016 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */

package org.matsim.maas.taxi;/*
 * created by jbischoff, 06.09.2018
 */

import org.junit.jupiter.api.Test;

public class TaxiTest {


    @Test
    public void runRulebasedTaxiScenario() {
        RunTaxiExample.run(RunTaxiExample.CONFIG_FILE_RULEBASED, false, 0);
    }


    @Test
    public void runAssignmentTaxiScenario() {
        RunTaxiExample.run(RunTaxiExample.CONFIG_FILE_ASSIGNMENT, false, 0);
    }
}
